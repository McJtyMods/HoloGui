package mcjty.hologui.network;

import mcjty.lib.network.PayloadContext;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class HoloMessages {

    public static void register() {
        PayloadTypeRegistry.playC2S().register(PacketSliderInput.TYPE, PacketSliderInput.CODEC);
        PayloadTypeRegistry.playC2S().register(PacketTextEntry.TYPE, PacketTextEntry.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PacketSliderInput.TYPE,
                (packet, context) -> packet.handle(new PayloadContext(context.player(), context.server())));
        ServerPlayNetworking.registerGlobalReceiver(PacketTextEntry.TYPE,
                (packet, context) -> packet.handle(new PayloadContext(context.player(), context.server())));
    }
}
