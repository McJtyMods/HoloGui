package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexFormat;

public class HoloGuiRenderType extends RenderType {

    // Dummy
    public HoloGuiRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable setup, Runnable clear) {
        super(name, format, mode, p_173181_, p_173182_, p_173183_, setup, clear);
    }

//    private static final RenderType TRANSLUCENT = create("translucent", DefaultVertexFormats.BLOCK, 7, 262144, true, true, getTranslucentState());
//      return RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED)
//    .transparency(TRANSLUCENT_TRANSPARENCY).build(true);


    public static final RenderType HOLOGUI_BACKGROUND = create("hologui_background", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 262144, true, true,
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_SHADOW_SHADER)
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setTextureState(new TextureStateShard(HoloGuiSpriteUploader.HOLOGUI_ATLAS, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(true));

    public static final RenderType HOLOGUI_ICONS = create("hologui_icons", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 262144, true, true,
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_SHADOW_SHADER)
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setTextureState(new TextureStateShard(HoloGuiSpriteUploader.HOLOGUI_ATLAS, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(true));
}
