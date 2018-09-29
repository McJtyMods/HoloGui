package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class HoloGuiSounds {

    public static final SoundEvent guiclick = new SoundEvent(new ResourceLocation(HoloGui.MODID, "guiclick")).setRegistryName(new ResourceLocation(HoloGui.MODID, "guiclick"));
    public static final SoundEvent guiopen = new SoundEvent(new ResourceLocation(HoloGui.MODID, "guiopen")).setRegistryName(new ResourceLocation(HoloGui.MODID, "guiopen"));

    public static void init(IForgeRegistry<SoundEvent> registry) {
        registry.register(guiclick);
        registry.register(guiopen);
    }
}
