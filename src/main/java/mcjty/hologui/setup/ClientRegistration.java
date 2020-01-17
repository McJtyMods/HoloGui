package mcjty.hologui.setup;


import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiEntityRender;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRegistration {

    public static void init() {
        RenderingRegistry.registerEntityRenderingHandler(HoloGuiEntity.class, HoloGuiEntityRender::new);
//        RenderingRegistry.registerEntityRenderingHandler(HoloGuiEntity.class, new HoloGuiEntityRender.Factory());
    }

}
