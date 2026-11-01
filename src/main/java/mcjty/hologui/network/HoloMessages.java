package mcjty.hologui.network;

import mcjty.hologui.HoloGui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class HoloMessages {

    public static SimpleChannel INSTANCE;

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(HoloGui.MODID, "input"),
                () -> "3", "3"::equals, "3"::equals);
        INSTANCE.registerMessage(1, PacketSliderInput.class, PacketSliderInput::toBytes, PacketSliderInput::new,
                PacketSliderInput::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(0, PacketTextEntry.class, PacketTextEntry::toBytes, PacketTextEntry::new,
                PacketTextEntry::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
