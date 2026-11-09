package mcjty.hologui.gui;

import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

public final class HoloGuiRenderType {

    private static final RenderPipeline IMAGE = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("hologui", "pipeline/image"))
            .withVertexShader("core/position_tex_color").withFragmentShader("core/position_tex_color")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR).withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withCull(false).build();
    private static final RenderPipeline COLOR = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath("hologui", "pipeline/color"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false).build();
    private static final OitPipelineSet IMAGE_OIT = OitPipelineSet.builder(Identifier.parse("hologui:image"),
            RenderPipeline.builder()
                    .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS).withBindGroupLayout(BindGroupLayouts.SAMPLER0)
                    .withVertexShader("core/position_tex_color").withFragmentShader(Identifier.parse("hologui:image"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR).withPrimitiveTopology(PrimitiveTopology.QUADS)
                    .withCull(false)).build();
    private static final Function<Identifier, RenderType> IMAGES = Util.memoize(texture -> RenderType.create("hologui_image",
            RenderSetup.builder(IMAGE).setOitPipelines(IMAGE_OIT).withTexture("Sampler0", texture).createRenderSetup()));
    public static final RenderType HOLOGUI_BACKGROUND = image(HoloGuiSpriteUploader.HOLOGUI_ATLAS);
    public static final RenderType HOLOGUI_COLOR = RenderType.create("hologui_color", RenderSetup.builder(COLOR).setOitPipelines(RenderPipelines.OIT_DEBUG_QUADS).createRenderSetup());

    private HoloGuiRenderType() {
    }

    public static RenderType image(Identifier texture) {
        return IMAGES.apply(texture);
    }

    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(IMAGE);
        event.registerPipeline(COLOR);
        event.registerOitPipelineSet(IMAGE_OIT);
    }
}
