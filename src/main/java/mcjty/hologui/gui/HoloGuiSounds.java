package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import mcjty.hologui.setup.Registration;
import mcjty.lib.varia.SoundTools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public class HoloGuiSounds {

    public static final RegistryObject<SoundEvent> GUICLICK = Registration.SOUNDS.register("guiclick", () -> SoundTools.createSoundEvent(new ResourceLocation(HoloGui.MODID, "guiclick")));
    public static final RegistryObject<SoundEvent> GUIOPEN = Registration.SOUNDS.register("guiopen", () -> SoundTools.createSoundEvent(new ResourceLocation(HoloGui.MODID, "guiopen")));

    public static void init() {
    }
}
