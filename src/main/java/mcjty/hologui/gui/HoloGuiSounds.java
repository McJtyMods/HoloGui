package mcjty.hologui.gui;

import java.util.function.Supplier;
import mcjty.hologui.HoloGui;
import mcjty.hologui.setup.Registration;
import mcjty.lib.varia.SoundTools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class HoloGuiSounds {

    public static final Supplier<SoundEvent> GUICLICK = Registration.SOUNDS.register("guiclick", () -> SoundTools.createSoundEvent(ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "guiclick")));
    public static final Supplier<SoundEvent> GUIOPEN = Registration.SOUNDS.register("guiopen", () -> SoundTools.createSoundEvent(ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "guiopen")));

    public static void init() {
    }
}
