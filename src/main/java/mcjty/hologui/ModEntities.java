package mcjty.hologui;

import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiEntityRender;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModEntities {

    public static void init() {
        int id = 1;
        // @todo 1.14
//        EntityRegistry.registerModEntity(new ResourceLocation(HoloGui.MODID, "hologui_hologui"), HoloGuiEntity.class,
//                "hologui_hologui", id++, HoloGui.instance, 64, 1, false);
    }

    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(HoloGuiEntity.class, new HoloGuiEntityRender.Factory());
    }
}
