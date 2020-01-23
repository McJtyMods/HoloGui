package mcjty.hologui.setup;


import mcjty.hologui.ModEntities;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiEntityRender;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRegistration {

    public static void init() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
//        RenderingRegistry.registerEntityRenderingHandler(HoloGuiEntity.class, new HoloGuiEntityRender.Factory());
    }

}
