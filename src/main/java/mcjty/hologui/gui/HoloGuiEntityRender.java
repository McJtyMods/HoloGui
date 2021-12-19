package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class HoloGuiEntityRender extends EntityRenderer<HoloGuiEntity> {

    public static final ResourceLocation GUI_BACKGROUND_1 = new ResourceLocation(HoloGui.MODID, "hologui_blue_softwhite");
    public static final ResourceLocation GUI_BACKGROUND_2 = new ResourceLocation(HoloGui.MODID, "hologui_blue");
    public static final ResourceLocation GUI_BACKGROUND_3 = new ResourceLocation(HoloGui.MODID, "hologui_blue_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_4 = new ResourceLocation(HoloGui.MODID, "hologui_blue_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_5 = new ResourceLocation(HoloGui.MODID, "hologui_blue_softblack");
    public static final ResourceLocation GUI_BACKGROUND_6 = new ResourceLocation(HoloGui.MODID, "hologui_gray_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_7 = new ResourceLocation(HoloGui.MODID, "hologui_gray_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_8 = new ResourceLocation(HoloGui.MODID, "hologui_gray_softblack");

    public HoloGuiEntityRender(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    // Option 1: blue with sharp white: hologui2
    // Option 2: just blue: hologui1
    // Option 3: blue with soft dark border: hologui4
    // Option 4: gray with soft dark: hologui7
    // Option 5: gray with sharp white: hologui6
    // Option 6: blue with sharp dark border: hologui3
    // Option 7: gray with sharp dark border: hologui5
    // Option 8: current: hologui


    @Override
    public void render(HoloGuiEntity entity, float p_225623_2_, float p_225623_3_, PoseStack stack, MultiBufferSource buffer, int p_225623_6_) {
//        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);

    // @todo 1.15
//    @Override
//    public void doRender(HoloGuiEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.isPassenger()) {
            return;
        }

        doActualRender(entity, stack, buffer);
    }

    public static void doActualRender(HoloGuiEntity entity, PoseStack matrixStack, MultiBufferSource buffer) {

        VertexConsumer builder = buffer.getBuffer(HoloGuiRenderType.HOLOGUI_BACKGROUND);


//        Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
        com.mojang.blaze3d.platform.Lighting.setupForFlatItems();

        matrixStack.pushPose();
//        GlStateManager.translated(x, y, z);

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entity.getViewYRot(0)));
        matrixStack.translate(0, .5, 0);
        float scale = entity.getScale();
        scale = 1f - (1f-scale) * (.4f / .25f);

        matrixStack.scale(scale, scale, scale);

//        GlStateManager.enableTexture();
//        GlStateManager.disableLighting();

        int style = Config.GUI_STYLE.get().ordinal();

        if (style <= 8) {
            // @todo 1.15
//            GlStateManager.enableBlend();
//            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 0.8f);
        } else {
//            GlStateManager.disableBlend();
//            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            style -= 8;
        }

        ResourceLocation background = GUI_BACKGROUND_1;

        switch (style) {
            case 1: background = GUI_BACKGROUND_1; break;
            case 2: background = GUI_BACKGROUND_2; break;
            case 3: background = GUI_BACKGROUND_3; break;
            case 4: background = GUI_BACKGROUND_4; break;
            case 5: background = GUI_BACKGROUND_5; break;
            case 6: background = GUI_BACKGROUND_6; break;
            case 7: background = GUI_BACKGROUND_7; break;
            case 8: background = GUI_BACKGROUND_8; break;
        }

        TextureAtlasSprite sprite = HoloGuiSpriteUploader.INSTANCE.getSprite(background);


        float min = -.5f;
        float max = .5f;
        renderQuadColor(builder, matrixStack.last().pose(), min, max, min, max, 255, 255, 255, 255, sprite);

//        GlStateManager.disableDepthTest();

        float cursorX = (float) entity.getCursorX();
        float cursorY = (float) entity.getCursorY();

        if (cursorX >= 0 && cursorX <= 10 && cursorY >= 0 && cursorY <= 10) {
//            GlStateManager.disableTexture();
//            GlStateManager.enableBlend();
            float offset = .01f;
            renderQuadColor(builder, matrixStack.last().pose(), (cursorX / 10.0f) - .42f - offset, (cursorX / 10.0f) - .42f + offset,
                    - ((cursorY / 10) -.42f - offset),  - ((cursorY / 10) -.42f + offset),
                    60, 255, 128, 100, sprite);
//            t.draw();
        }

        IGuiComponent<?> gui = entity.getGui(Minecraft.getInstance().player);
        if (gui != null) {
            gui.render(matrixStack, buffer, Minecraft.getInstance().player, entity, cursorX, cursorY);
            IGuiComponent<?> hovering = gui.findHoveringWidget(cursorX, cursorY);
            if (hovering != entity.tooltipComponent) {
                entity.tooltipComponent = hovering;
                entity.tooltipTimeout = 10;
            } else {
                if (entity.tooltipTimeout > 0) {
                    entity.tooltipTimeout--;
                } else {
                    if (hovering != null) {
                        hovering.renderTooltip(matrixStack, buffer, Minecraft.getInstance().player, entity, cursorX, cursorY);
                    }
                }
            }
        }

        matrixStack.popPose();


//        GlStateManager.enableTexture();
//        GlStateManager.enableDepthTest();
//        GlStateManager.enableLighting();
//        Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
        com.mojang.blaze3d.platform.Lighting.setupFor3DItems();
    }

    private static void renderQuadColor(VertexConsumer builder, Matrix4f matrix, float minX, float maxX, float minY, float maxY, int r, int g, int b, int a,
                                        TextureAtlasSprite sprite) {
//   public static final VertexFormat BLOCK = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
// .add(POSITION_3F).add(COLOR_4UB).add(TEX_2F).add(TEX_2SB).add(NORMAL_3B).add(PADDING_1B).build());

//        builder.pos(matrix, minX, minY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex(); //1
//        builder.pos(matrix, maxX, minY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
//        builder.pos(matrix, maxX, maxY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
//        builder.pos(matrix, minX, maxY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
//        builder.pos(matrix, minX, maxY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex(); //2
//        builder.pos(matrix, maxX, maxY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
//        builder.pos(matrix, maxX, minY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
//        builder.pos(matrix, minX, minY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0xf000f0).normal(1, 1, 1).endVertex();
        builder.vertex(matrix, minX, minY, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).endVertex(); //1
        builder.vertex(matrix, maxX, minY, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).endVertex();
        builder.vertex(matrix, maxX, maxY, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).endVertex();
        builder.vertex(matrix, minX, maxY, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).endVertex();
        builder.vertex(matrix, minX, maxY, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).endVertex(); //2
        builder.vertex(matrix, maxX, maxY, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).endVertex();
        builder.vertex(matrix, maxX, minY, 0).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).endVertex();
        builder.vertex(matrix, minX, minY, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).endVertex();
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
            ;
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + partialTicks * f;
    }

    @Override
    public ResourceLocation getTextureLocation(HoloGuiEntity pEntity) {
        return null;
    }
}
