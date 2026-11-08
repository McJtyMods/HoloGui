package mcjty.hologui.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class HoloMessages {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("hologui").versioned("1");
        registrar.playToServer(PacketSliderInput.TYPE, PacketSliderInput.CODEC, PacketSliderInput::handle);
        registrar.playToServer(PacketTextEntry.TYPE, PacketTextEntry.CODEC, PacketTextEntry::handle);
    }
}
