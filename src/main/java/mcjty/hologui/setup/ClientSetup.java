package mcjty.hologui.setup;


import mcjty.hologui.ModEntities;
import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
    }

    public static void setupSpriteUploader() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            HoloGuiSpriteUploader.INSTANCE = new HoloGuiSpriteUploader(Minecraft.getInstance().getTextureManager());
            ((IReloadableResourceManager) resourceManager).addReloadListener(HoloGuiSpriteUploader.INSTANCE);
        }
    }
}
