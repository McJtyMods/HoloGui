package mcjty.hologui.network;

import mcjty.hologui.gui.HoloGuiEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PacketTextEntry(int entityId, String guiId, String tag, String component, String text, boolean commit) {

    public PacketTextEntry(int entityId, String guiId, String tag, String component, String text) {
        this(entityId, guiId, tag, component, text, true);
    }

    public PacketTextEntry(FriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(2048), buffer.readBoolean());
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(guiId, 128);
        buffer.writeUtf(tag, 128);
        buffer.writeUtf(component, 128);
        buffer.writeUtf(text, 2048);
        buffer.writeBoolean(commit);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.level().getEntity(entityId) instanceof HoloGuiEntity holo
                    && holo.isAlive() && player.distanceToSqr(holo) < 64
                    && guiId.equals(holo.getGuiId()) && tag.equals(holo.getTag())) {
                if (commit) {
                    holo.commitTextEntry(player, component, text);
                } else {
                    holo.renewTextEntry(player, component);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
