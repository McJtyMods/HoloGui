package mcjty.hologui.network;

import mcjty.hologui.gui.HoloGuiEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Only the drag intent is transmitted; the server computes the value from its own player ray. */
public record PacketSliderInput(int entityId, String guiId, String tag, String component, boolean finish, float yaw, float pitch) implements CustomPacketPayload {

    public static final Type<PacketSliderInput> TYPE = new Type<>(Identifier.fromNamespaceAndPath("hologui", "packet_slider_input"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketSliderInput> CODEC = StreamCodec.ofMember(PacketSliderInput::toBytes, PacketSliderInput::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public PacketSliderInput(RegistryFriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(128), buffer.readBoolean(), buffer.readFloat(), buffer.readFloat());
    }

    public void toBytes(RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(guiId, 128);
        buffer.writeUtf(tag, 128);
        buffer.writeUtf(component, 128);
        buffer.writeBoolean(finish);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (Float.isFinite(yaw) && Float.isFinite(pitch) && pitch >= -90 && pitch <= 90
                    && player != null && !player.isSpectator() && player.level().getEntity(entityId) instanceof HoloGuiEntity holo
                    && holo.isAlive() && player.distanceToSqr(holo) < 64
                    && guiId.equals(holo.getGuiId()) && tag.equals(holo.getTag())) {
                holo.dragSlider(player, component, finish, Vec3.directionFromRotation(pitch, Mth.wrapDegrees(yaw)));
            }
        });
    }
}
