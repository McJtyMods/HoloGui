package mcjty.hologui.setup;

import mcjty.hologui.config.HoloGuiModConfigGui;
import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientSetup {

    public static void registerConfigScreen(ModContainer mod) {
        mod.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> new HoloGuiModConfigGui(parent));
    }

    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
    }

    public static void setupSpriteUploader(RegisterTextureAtlasesEvent event) {
        event.register(new AtlasManager.AtlasConfig(HoloGuiSpriteUploader.HOLOGUI_ATLAS,
                HoloGuiSpriteUploader.ATLAS_ID, false));
    }
}
