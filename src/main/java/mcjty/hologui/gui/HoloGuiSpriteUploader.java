package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

/** The game's atlas manager owns loading, animation and resource reloads. */
public final class HoloGuiSpriteUploader {

    public static final HoloGuiSpriteUploader INSTANCE = new HoloGuiSpriteUploader();
    public static final Identifier HOLOGUI_ATLAS = Identifier.fromNamespaceAndPath(HoloGui.MODID, "textures/atlas/hologui.png");
    public static final Identifier ATLAS_ID = Identifier.fromNamespaceAndPath(HoloGui.MODID, "hologui");

    private HoloGuiSpriteUploader() {
    }

    public TextureAtlasSprite getSprite(Identifier location) {
        return Minecraft.getInstance().getAtlasManager().get(new SpriteId(HOLOGUI_ATLAS, location));
    }
}
