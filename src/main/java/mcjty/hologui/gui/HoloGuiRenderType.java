package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class HoloGuiRenderType extends RenderType {

    // Dummy
    public HoloGuiRenderType(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable setup, Runnable clear) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, setup, clear);
    }

//    private static final RenderType TRANSLUCENT = create("translucent", DefaultVertexFormats.BLOCK, 7, 262144, true, true, getTranslucentState());
//      return RenderType.State.getBuilder().shadeModel(SHADE_ENABLED).lightmap(LIGHTMAP_ENABLED).texture(BLOCK_SHEET_MIPPED)
//    .transparency(TRANSLUCENT_TRANSPARENCY).build(true);


    public static final RenderType HOLOGUI_BACKGROUND = create("hologui_background", DefaultVertexFormat.POSITION_COLOR_TEX, GL11.GL_QUADS, 262144, true, true,
            State.builder().setShadeModelState(SMOOTH_SHADE)
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setTextureState(new TextureState(HoloGuiSpriteUploader.HOLOGUI_ATLAS, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//                    .writeMask(COLOR_WRITE)
                    .createCompositeState(true));

    public static final RenderType HOLOGUI_ICONS = create("hologui_icons", DefaultVertexFormat.POSITION_TEX, GL11.GL_QUADS, 262144, true, true,
            State.builder().setShadeModelState(SMOOTH_SHADE)
                    .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setTextureState(new TextureState(HoloGuiSpriteUploader.HOLOGUI_ATLAS, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
//                    .writeMask(COLOR_WRITE)
                    .createCompositeState(true));
}
