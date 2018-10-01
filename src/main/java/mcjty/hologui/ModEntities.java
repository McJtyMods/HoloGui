package mcjty.hologui;

import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiEntityRender;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

    public static void init() {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(HoloGui.MODID, "hologui_hologui"), HoloGuiEntity.class,
                "hologui_hologui", id++, HoloGui.instance, 64, 1, false);
    }

    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(HoloGuiEntity.class, new HoloGuiEntityRender.Factory());
    }
}
