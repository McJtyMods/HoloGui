package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static mcjty.hologui.gui.HoloGuiEntityRender.*;
import static mcjty.hologui.gui.HoloGuiEntityRender.GUI_BACKGROUND_8;

public class HoloGuiSpriteUploader extends SpriteUploader {

    public static final ResourceLocation HOLOGUI_ATLAS = new ResourceLocation(HoloGui.MODID, "textures/atlas/hologui.png");

    private final List<ResourceLocation> textures = new ArrayList<>();

    public HoloGuiSpriteUploader(TextureManager textureManager) {
        super(textureManager, HOLOGUI_ATLAS, "gui");

        textures.add(GUI_BACKGROUND_1);
        textures.add(GUI_BACKGROUND_2);
        textures.add(GUI_BACKGROUND_3);
        textures.add(GUI_BACKGROUND_4);
        textures.add(GUI_BACKGROUND_5);
        textures.add(GUI_BACKGROUND_6);
        textures.add(GUI_BACKGROUND_7);
        textures.add(GUI_BACKGROUND_8);
    }

    @Override
    protected Stream<ResourceLocation> getResourceLocations() {
        return textures.stream();
    }
}
