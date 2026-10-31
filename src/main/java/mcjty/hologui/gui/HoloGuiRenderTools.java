package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.client.model.pipeline.VertexConsumerWrapper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.FormattedCharSequence;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class HoloGuiRenderTools {

    public static void renderText(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, String text, int color, float scale) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f*scale, 0.01f*scale, 0.01f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
        Minecraft.getInstance().font.drawInBatch(text, (float) (x * 10 / scale - 40 / scale), (float) (y * 10 / scale - 40 / scale), color,
                false, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 0xf000f0);  // @todo 1.15 or 140?
        matrixStack.popPose();
    }

    public static void renderTextShadow(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, String text, int color, float scale) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f*scale, 0.01f*scale, 0.01f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
        Minecraft.getInstance().font.drawInBatch(text, (float) (x * 10 / scale - 40 / scale), (float) (y * 10 / scale - 40 / scale), color,
                true, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 0xf000f0);  // @todo 1.15 or 140?
        matrixStack.popPose();
    }

    public static void renderImage(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, int u, int v, int w, int h, int txtw, int txth, ResourceLocation image) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180));

        Matrix4f pose = matrixStack.last().pose();
        VertexConsumer vertices = buffer.getBuffer(HoloGuiRenderType.image(image));
        float left = (float) (x * 10 - 46);
        float top = (float) (y * 10 - 44);
        float u0 = (float) u / txtw;
        float v0 = (float) v / txth;
        float u1 = (float) (u + w) / txtw;
        float v1 = (float) (v + h) / txth;
        vertices.vertex(pose, left, top + h, 0).color(255, 255, 255, 255).uv(u0, v1)
                .uv2(LightTexture.FULL_BRIGHT).endVertex();
        vertices.vertex(pose, left + w, top + h, 0).color(255, 255, 255, 255).uv(u1, v1)
                .uv2(LightTexture.FULL_BRIGHT).endVertex();
        vertices.vertex(pose, left + w, top, 0).color(255, 255, 255, 255).uv(u1, v0)
                .uv2(LightTexture.FULL_BRIGHT).endVertex();
        vertices.vertex(pose, left, top, 0).color(255, 255, 255, 255).uv(u0, v0)
                .uv2(LightTexture.FULL_BRIGHT).endVertex();
        matrixStack.popPose();
    }

    public static void renderBox(double x, double y, double w, double h, int color) {
//        GlStateManaSger.pushMatrix();
//
//        GlStateManager.scaled(0.10, 0.10, 0.10);
//        GlStateManager.rotatef(180, 0, 1, 0);

//        RenderHelper.drawFlatBox(3 - (int) x, 3 - (int) y, (int) (3 - x + w), (int) (3 - y + h), color, color);
//        RenderHelper.drawHorizontalLine((int) x, (int) y, (int) (x + w), color);
//        RenderHelper.drawHorizontalLine((int) x, (int) (y + h), (int) (x + w), color);
//        RenderHelper.drawVerticalLine((int) x, (int) y, (int) (y + h), color);
//        RenderHelper.drawVerticalLine((int) (x + w), (int) y, (int) (y + h), color);

//        GlStateManager.popMatrix();
    }

    public static void renderBorder(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, double w, double h, int r, int g, int b, int a) {
        float left = (float) (x * .1 - .42);
        float top = (float) (.42 - y * .1);
        float right = left + (float) w * .1f;
        float bottom = top - (float) h * .1f;
        float thickness = .0015f;
        VertexConsumer vertices = buffer.getBuffer(HoloGuiRenderType.HOLOGUI_COLOR);
        Matrix4f pose = matrixStack.last().pose();
        coloredQuad(vertices, pose, left, right, top - thickness, top, r, g, b, a);
        coloredQuad(vertices, pose, left, right, bottom, bottom + thickness, r, g, b, a);
        coloredQuad(vertices, pose, left, left + thickness, bottom, top, r, g, b, a);
        coloredQuad(vertices, pose, right - thickness, right, bottom, top, r, g, b, a);
    }

    private static void coloredQuad(VertexConsumer vertices, Matrix4f pose, float left, float right, float bottom, float top, int r, int g, int b, int a) {
        vertices.vertex(pose, left, bottom, .001f).color(r, g, b, a).endVertex();
        vertices.vertex(pose, right, bottom, .001f).color(r, g, b, a).endVertex();
        vertices.vertex(pose, right, top, .001f).color(r, g, b, a).endVertex();
        vertices.vertex(pose, left, top, .001f).color(r, g, b, a).endVertex();
    }

    public static void renderItem(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, ItemStack stack, @Nullable ResourceLocation lightmap, boolean border, float scale) {
        boolean invalid = lightmap != null && lightmap.getPath().equals("textures/gui/darken_red.png");
        renderItem(matrixStack, buffer, x, y, stack, lightmap == null ? 1 : .4f, invalid, border, scale);
    }

    public static void renderItem(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, ItemStack stack, float brightness, boolean border, float scale) {
        renderItem(matrixStack, buffer, x, y, stack, brightness, false, border, scale);
    }

    private static void renderItem(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, ItemStack stack, float brightness, boolean invalid, boolean border, float scale) {
        if (stack.isEmpty()) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.translate(x * .09975 - .37, .366 - y * .102, 0);
        // GUI item transforms keep blocks and flat items readable at every viewing angle.
        // Flatten depth so even large item models stay in front of the panel.
        matrixStack.scale(.1f * scale, .1f * scale, .0001f * scale);
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = renderer.getModel(stack, null, null, 1);
        MultiBufferSource itemBuffer = buffer;
        if (brightness < 1 || invalid) {
            // Disabled icons used to bind a fake lightmap, leaking state into other widgets.
            itemBuffer = type -> new VertexConsumerWrapper(buffer.getBuffer(type)) {

                @Override
                public VertexConsumer color(int r, int g, int b, int a) {
                    return super.color(invalid ? r : (int) (r * brightness), (int) (g * brightness), (int) (b * brightness), a);
                }
            };
        }
        renderer.render(stack, ItemDisplayContext.GUI, false, matrixStack, itemBuffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, model);
        matrixStack.popPose();
        if (border) {
            renderBorder(matrixStack, buffer, x, y, 1, 1, 255, 255, 255, 255);
        }
    }

    public static RenderType guiRenderType(RenderType type) {
        // Fabulous normally sends translucent item icons to a separate world framebuffer.
        // Keep GUI items with the panel so they share its depth and draw order.
        return type == Sheets.translucentItemSheet() ? Sheets.translucentCullBlockSheet() : type;
    }


    public static void renderToolTip(PoseStack matrixStack, MultiBufferSource buffer, ItemStack stack, int x, int y, BiConsumer<ItemStack, List<String>> tooltipHandler) {
        if (stack.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        TooltipFlag flag = mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
        List<String> text = stack.getTooltipLines(mc.player, flag).stream().map(Component::getString).collect(Collectors.toList());
        tooltipHandler.accept(stack, text);
        Font font = mc.font;
        List<FormattedCharSequence> lines = new ArrayList<>();
        for (String line : text) {
            lines.addAll(font.split(Component.literal(line), 210));
        }
        if (lines.isEmpty()) {
            return;
        }
        int width = lines.stream().mapToInt(font::width).max().orElse(0);
        int height = Math.min(22, lines.size()) * (font.lineHeight + 1);
        int left = Math.max(-118, Math.min(x + 8, 118 - width));
        int top = Math.max(-118, Math.min(y + 8, 118 - height));
        coloredQuad(buffer.getBuffer(HoloGuiRenderType.HOLOGUI_COLOR), matrixStack.last().pose(),
                left - 3, left + width + 3, top + height + 2, top - 3, 12, 8, 24, 245);
        matrixStack.pushPose();
        matrixStack.translate(0, 0, -.5);
        for (int i = 0; i < Math.min(22, lines.size()); i++) {
            font.drawInBatch(lines.get(i), left, top + i * (font.lineHeight + 1), i == 0 ? 0xffffff : 0xcccccc,
                    false, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        }
        matrixStack.popPose();
    }
}
