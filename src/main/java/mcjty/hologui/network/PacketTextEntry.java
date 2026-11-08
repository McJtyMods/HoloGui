package mcjty.hologui.network;

import mcjty.hologui.gui.HoloGuiEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketTextEntry(int entityId, String guiId, String tag, String component, String text, boolean commit) implements CustomPacketPayload {

    public static final Type<PacketTextEntry> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("hologui", "packet_text_entry"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketTextEntry> CODEC = StreamCodec.ofMember(PacketTextEntry::toBytes, PacketTextEntry::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public PacketTextEntry(int entityId, String guiId, String tag, String component, String text) {
        this(entityId, guiId, tag, component, text, true);
    }

    public PacketTextEntry(RegistryFriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(128), buffer.readUtf(2048), buffer.readBoolean());
    }

    public void toBytes(RegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(guiId, 128);
        buffer.writeUtf(tag, 128);
        buffer.writeUtf(component, 128);
        buffer.writeUtf(text, 2048);
        buffer.writeBoolean(commit);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
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
    }
}
