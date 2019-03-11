package mcjty.hologui.setup;


import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Registration {

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> sounds) {
        HoloGuiSounds.init(sounds.getRegistry());
    }


}
