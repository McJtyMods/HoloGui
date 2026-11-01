package mcjty.hologui.network;

import mcjty.hologui.gui.HoloGuiEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Only the drag intent is transmitted; the server computes the value from its own player ray. */
public record PacketSliderInput(int entityId, String guiId, String tag, String component, boolean finish, float yaw, float pitch) {

    public PacketSliderInput(FriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(128), buffer.readBoolean(), buffer.readFloat(), buffer.readFloat());
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(guiId, 128);
        buffer.writeUtf(tag, 128);
        buffer.writeUtf(component, 128);
        buffer.writeBoolean(finish);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (Float.isFinite(yaw) && Float.isFinite(pitch) && pitch >= -90 && pitch <= 90
                    && player != null && !player.isSpectator() && player.level().getEntity(entityId) instanceof HoloGuiEntity holo
                    && holo.isAlive() && player.distanceToSqr(holo) < 64
                    && guiId.equals(holo.getGuiId()) && tag.equals(holo.getTag())) {
                holo.dragSlider(player, component, finish, Vec3.directionFromRotation(pitch, Mth.wrapDegrees(yaw)));
            }
        });
        context.setPacketHandled(true);
    }
}
