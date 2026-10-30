package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;



public class HoloGuiSpriteUploader extends TextureAtlasHolder {

    public static HoloGuiSpriteUploader INSTANCE;

    public static final ResourceLocation HOLOGUI_ATLAS = new ResourceLocation(HoloGui.MODID, "textures/atlas/hologui.png");

    public HoloGuiSpriteUploader(TextureManager textureManager) {
        super(textureManager, HOLOGUI_ATLAS, new ResourceLocation(HoloGui.MODID, "hologui"));
    }

    @Override
    public TextureAtlasSprite getSprite(ResourceLocation location) {
        return super.getSprite(location);
    }
}
