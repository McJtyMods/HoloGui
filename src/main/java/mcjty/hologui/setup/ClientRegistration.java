package mcjty.hologui.setup;


import mcjty.hologui.HoloGui;
import mcjty.hologui.ModEntities;
import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HoloGui.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    public static void init() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
    }

    @SubscribeEvent
    public static void onColorHandler(ColorHandlerEvent event) {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            HoloGuiSpriteUploader spriteUploader = new HoloGuiSpriteUploader(Minecraft.getInstance().getTextureManager());
            ((IReloadableResourceManager) resourceManager).addReloadListener(spriteUploader);
        }

    }

//    @SubscribeEvent
//    public static void onTextureStitch(TextureStitchEvent.Pre event) {
//        if (!event.getMap().getBasePath().equals(HoloGuiSpriteUploader.HOLOGUI_ATLAS)) {
//            return;
//        }
//        event.addSprite(GUI_BACKGROUND_1);
//        event.addSprite(GUI_BACKGROUND_2);
//        event.addSprite(GUI_BACKGROUND_3);
//        event.addSprite(GUI_BACKGROUND_4);
//        event.addSprite(GUI_BACKGROUND_5);
//        event.addSprite(GUI_BACKGROUND_6);
//        event.addSprite(GUI_BACKGROUND_7);
//        event.addSprite(GUI_BACKGROUND_8);
//    }
//
}
