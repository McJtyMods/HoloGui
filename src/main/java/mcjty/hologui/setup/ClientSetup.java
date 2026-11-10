package mcjty.hologui.setup;

import mcjty.hologui.gui.HoloGuiEntityRender;
import mcjty.hologui.gui.HoloGuiSpriteUploader;
import mcjty.hologui.gui.SliderInput;
import mcjty.lib.client.ClientStartup;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.InteractionResult;

public class ClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Registration.HOLOGUI_ENTITY_TYPE.get(), HoloGuiEntityRender::new);
        ClientStartup.register(client -> {
            HoloGuiSpriteUploader.INSTANCE = new HoloGuiSpriteUploader(client.getTextureManager());
            ((ReloadableResourceManager) client.getResourceManager()).registerReloadListener(HoloGuiSpriteUploader.INSTANCE);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> SliderInput.tick());
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) ->
                level.isClientSide() && SliderInput.isDragging() ? InteractionResult.FAIL : InteractionResult.PASS);
    }
}
