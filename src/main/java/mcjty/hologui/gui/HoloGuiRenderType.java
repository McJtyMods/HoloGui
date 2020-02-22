package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HoloGuiRenderType extends RenderType {

    // Dummy
    public HoloGuiRenderType(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable setup, Runnable clear) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, setup, clear);
    }


    public static final ResourceLocation HOLOGUI_ATLAS = new ResourceLocation(HoloGui.MODID, "textures/atlas/hologui.png");

    public static final RenderType HOLOGUI_BACKGROUND = get("hologui_background", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 262144, true, true,
            State.builder().shadeModel(SHADE_ENABLED)
                    .lightmap(RenderState.LIGHTMAP_DISABLED)
                    .texture(new TextureState(HOLOGUI_ATLAS, false, false))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .writeMask(COLOR_WRITE)
                    .build(true));
}
