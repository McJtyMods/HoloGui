package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;

import java.util.function.Function;

public class HoloGuiRenderType extends RenderType {

    // Dummy
    public HoloGuiRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable setup, Runnable clear) {
        super(name, format, mode, p_173181_, p_173182_, p_173183_, setup, clear);
    }

    private static final Function<ResourceLocation, RenderType> IMAGES = Util.memoize(HoloGuiRenderType::createImageType);

    public static final RenderType HOLOGUI_BACKGROUND = image(HoloGuiSpriteUploader.HOLOGUI_ATLAS);

    public static final RenderType HOLOGUI_COLOR = create("hologui_color", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false,
            CompositeState.builder()
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setCullState(NO_CULL)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false));

    public static RenderType image(ResourceLocation texture) {
        return IMAGES.apply(texture);
    }

    private static RenderType createImageType(ResourceLocation texture) {
        // Unlit textured quads with depth writes, including the panel behind the widgets.
        return create("hologui_image", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true,
                CompositeState.builder()
                        .setShaderState(RENDERTYPE_TEXT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setLightmapState(LIGHTMAP)
                        .setCullState(NO_CULL)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .createCompositeState(false));
    }
}
