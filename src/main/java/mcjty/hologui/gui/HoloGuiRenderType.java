package mcjty.hologui.gui;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderPipelines;
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
    private static final Function<Identifier, RenderType> IMAGES = Util.memoize(texture -> RenderType.create("hologui_image",
            RenderSetup.builder(IMAGE).withTexture("Sampler0", texture).createRenderSetup()));
    public static final RenderType HOLOGUI_BACKGROUND = image(HoloGuiSpriteUploader.HOLOGUI_ATLAS);
    public static final RenderType HOLOGUI_COLOR = RenderType.create("hologui_color", RenderSetup.builder(COLOR).createRenderSetup());

    private HoloGuiRenderType() {
    }

    public static RenderType image(Identifier texture) {
        return IMAGES.apply(texture);
    }

    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(IMAGE);
        event.registerPipeline(COLOR);
    }
}
