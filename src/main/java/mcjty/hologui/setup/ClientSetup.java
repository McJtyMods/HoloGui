package mcjty.hologui.setup;

import mcjty.hologui.config.HoloGuiModConfigGui;
import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientSetup {

    public static void registerConfigScreen(ModContainer mod) {
        mod.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> new HoloGuiModConfigGui(parent));
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
