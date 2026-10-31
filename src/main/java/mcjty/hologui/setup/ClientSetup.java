package mcjty.hologui.setup;

import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.config.HoloGuiModConfigGui;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class ClientSetup {

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new HoloGuiModConfigGui(parent)));
    }

    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
    }

    public static void setupSpriteUploader(RegisterColorHandlersEvent.Block event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            HoloGuiSpriteUploader.INSTANCE = new HoloGuiSpriteUploader(Minecraft.getInstance().getTextureManager());
            ((ReloadableResourceManager) resourceManager).registerReloadListener(HoloGuiSpriteUploader.INSTANCE);
        }
    }
}
