package mcjty.hologui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class HoloGuiEntityRender extends EntityRenderer<HoloGuiEntity> {

    public static final ResourceLocation GUI_BACKGROUND_1 = new ResourceLocation(HoloGui.MODID, "gui/hologui_blue_softwhite");
    public static final ResourceLocation GUI_BACKGROUND_2 = new ResourceLocation(HoloGui.MODID, "gui/hologui_blue");
    public static final ResourceLocation GUI_BACKGROUND_3 = new ResourceLocation(HoloGui.MODID, "gui/hologui_blue_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_4 = new ResourceLocation(HoloGui.MODID, "gui/hologui_blue_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_5 = new ResourceLocation(HoloGui.MODID, "gui/hologui_blue_softblack");
    public static final ResourceLocation GUI_BACKGROUND_6 = new ResourceLocation(HoloGui.MODID, "gui/hologui_gray_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_7 = new ResourceLocation(HoloGui.MODID, "gui/hologui_gray_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_8 = new ResourceLocation(HoloGui.MODID, "gui/hologui_gray_softblack");

    public HoloGuiEntityRender(EntityRendererManager renderManager) {
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
    public void render(HoloGuiEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer buffer, int p_225623_6_) {
//        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);

    // @todo 1.15
//    @Override
//    public void doRender(HoloGuiEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.isPassenger()) {
            return;
        }

        doActualRender(entity, stack, buffer);
    }

    public static void doActualRender(HoloGuiEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer) {

        IVertexBuilder builder = buffer.getBuffer(HoloGuiRenderType.HOLOGUI_BACKGROUND);


//        Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();

        matrixStack.push();
//        GlStateManager.translated(x, y, z);

//        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.translate(0, .5, 0);
        float scale = entity.getScale();
        scale = 1f - (1f-scale) * (.4f / .25f);

        matrixStack.scale(scale, scale, scale);

//        GlStateManager.enableTexture();
//        GlStateManager.disableLighting();

        int style = Config.GUI_STYLE.get().ordinal();

        if (style <= 8) {
            GlStateManager.enableBlend();
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 0.8f);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
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
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureGetter(HoloGuiSpriteUploader.HOLOGUI_ATLAS).apply(background);


        float min = -.5f;
        float max = .5f;
        renderQuadColor(builder, matrixStack.getLast().getPositionMatrix(), min, max, min, max, 255, 255, 255, 255, sprite);

//        GlStateManager.disableDepthTest();

        float cursorX = (float) entity.getCursorX();
        float cursorY = (float) entity.getCursorY();

        IGuiComponent gui = entity.getGui(Minecraft.getInstance().player);
        if (gui != null) {
            gui.render(Minecraft.getInstance().player, entity, cursorX, cursorY);
            IGuiComponent hovering = gui.findHoveringWidget(cursorX, cursorY);
            if (hovering != entity.tooltipComponent) {
                entity.tooltipComponent = hovering;
                entity.tooltipTimeout = 10;
            } else {
                if (entity.tooltipTimeout > 0) {
                    entity.tooltipTimeout--;
                } else {
                    if (hovering != null) {
                        hovering.renderTooltip(Minecraft.getInstance().player, entity, cursorX, cursorY);
                    }
                }
            }
        }

        if (cursorX >= 0 && cursorX <= 10 && cursorY >= 0 && cursorY <= 10) {
//            GlStateManager.disableTexture();
//            GlStateManager.enableBlend();
            float offset = .01f;
            renderQuadColor(builder, matrixStack.getLast().getPositionMatrix(), (cursorX / 10.0f) - .42f - offset, (cursorX / 10.0f) - .42f + offset,
                     - ((cursorY / 10) -.42f - offset),  - ((cursorY / 10) -.42f + offset),
                    60, 255, 128, 100, sprite);
//            t.draw();
        }
        matrixStack.pop();


//        GlStateManager.enableTexture();
//        GlStateManager.enableDepthTest();
//        GlStateManager.enableLighting();
//        Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
    }

    private static void renderQuad(IVertexBuilder builder, double minX, double maxX, double minY, double maxY) {
        builder.pos(minX, minY, 0).tex(0, 0).endVertex(); //1
        builder.pos(maxX, minY, 0).tex(1, 0).endVertex();
        builder.pos(maxX, maxY, 0).tex(1, 1).endVertex();
        builder.pos(minX, maxY, 0).tex(0, 1).endVertex();
        builder.pos(minX, maxY, 0).tex(0, 1).endVertex(); //2
        builder.pos(maxX, maxY, 0).tex(1, 1).endVertex();
        builder.pos(maxX, minY, 0).tex(1, 0).endVertex();
        builder.pos(minX, minY, 0).tex(0, 0).endVertex();
    }

    private static void renderQuadColor(IVertexBuilder builder, Matrix4f matrix, float minX, float maxX, float minY, float maxY, int r, int g, int b, int a,
                                        TextureAtlasSprite sprite) {
        builder.pos(matrix, minX, minY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMinV()).endVertex(); //1
        builder.pos(matrix, maxX, minY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
        builder.pos(matrix, maxX, maxY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
        builder.pos(matrix, minX, maxY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
        builder.pos(matrix, minX, maxY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMaxV()).endVertex(); //2
        builder.pos(matrix, maxX, maxY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
        builder.pos(matrix, maxX, minY, 0).color(r, g, b, a).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
        builder.pos(matrix, minX, minY, 0).color(r, g, b, a).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
    }

    private void renderDebugOutline(HoloGuiEntity entity, Tessellator t, BufferBuilder builder) {
        AxisAlignedBB box = entity.getRenderBoundingBox();
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        double minX = box.minX - entity.getPosX();
        double minY = box.minY - entity.getPosY();
        double minZ = box.minZ - entity.getPosZ();
        double maxX = box.maxX - entity.getPosX();
        double maxY = box.maxY - entity.getPosY();
        double maxZ = box.maxZ - entity.getPosZ();

        renderDebugOutline(builder, minX, minY, minZ, maxX, maxY, maxZ);
        t.draw();
    }

    private void renderDebugOutline(BufferBuilder builder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        builder.pos(minX, minY, minZ).color(255, 255, 255, 128).endVertex();
        builder.pos(maxX, minY, minZ).color(255, 255, 255, 128).endVertex();

        builder.pos(minX, minY, minZ).color(255, 255, 255, 128).endVertex();
        builder.pos(minX, maxY, minZ).color(255, 255, 255, 128).endVertex();

        builder.pos(minX, minY, minZ).color(255, 255, 255, 128).endVertex();
        builder.pos(minX, minY, maxZ).color(255, 255, 255, 128).endVertex();

        builder.pos(maxX, maxY, maxZ).color(255, 0, 0, 128).endVertex();
        builder.pos(minX, maxY, maxZ).color(255, 0, 0, 128).endVertex();

        builder.pos(maxX, maxY, maxZ).color(255, 0, 0, 128).endVertex();
        builder.pos(maxX, minY, maxZ).color(255, 0, 0, 128).endVertex();

        builder.pos(maxX, maxY, maxZ).color(255, 0, 0, 128).endVertex();
        builder.pos(maxX, maxY, minZ).color(255, 0, 0, 128).endVertex();
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
    public ResourceLocation getEntityTexture(HoloGuiEntity entity) {
        return null;
    }
}
