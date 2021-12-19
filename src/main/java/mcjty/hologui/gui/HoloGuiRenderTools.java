package mcjty.hologui.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mcjty.lib.client.CustomRenderTypes;
import mcjty.lib.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraft.world.inventory.InventoryMenu;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class HoloGuiRenderTools {

    public static void renderText(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, String text, int color, float scale) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f*scale, 0.01f*scale, 0.01f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        Minecraft.getInstance().font.drawInBatch(text, (float) (x * 10 / scale - 40 / scale), (float) (y * 10 / scale - 40 / scale), color,
                false, matrixStack.last().pose(), buffer, false, 0, 0xf000f0);  // @todo 1.15 or 140?
        matrixStack.popPose();
    }

    public static void renderTextShadow(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, String text, int color, float scale) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f*scale, 0.01f*scale, 0.01f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        Minecraft.getInstance().font.drawInBatch(text, (float) (x * 10 / scale - 40 / scale), (float) (y * 10 / scale - 40 / scale), color,
                true, matrixStack.last().pose(), buffer, false, 0, 0xf000f0);  // @todo 1.15 or 140?
        matrixStack.popPose();
    }

    public static void renderImage(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, int u, int v, int w, int h, int txtw, int txth, ResourceLocation image) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStack.scale(1, 1, 0.01f);

        float zLevel = Minecraft.getInstance().getItemRenderer().blitOffset + 250F;

        GuiUtils.drawContinuousTexturedBox(matrixStack, image, (int) (x * 10 - 46), (int) (y * 10 - 44), u, v, w, h, txtw, txth, 0, zLevel);
        matrixStack.popPose();
    }

    public static void renderBox(double x, double y, double w, double h, int color) {
//        GlStateManager.pushMatrix();
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

        y += h-1;

        x *= 1.05;
        y *= 0.85;
        y += .45;

        matrixStack.pushPose();
        matrixStack.scale(0.1f, 0.1f, 0.1f);
        matrixStack.translate(x * 0.95 - 3.7, 4.2 - y * 1.2, 0);
        matrixStack.scale(1, 1, 0.1f);

        VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.LINESTRIP);
        x /= 200;
        x -= 0.47;
        y /= 100;
        y -= 0.5;


        double z = 0.3;
        builder.vertex(x, y, z).color(r, g, b, a).endVertex();
        builder.vertex(x + w, y, z).color(r, g, b, a).endVertex();
        builder.vertex(x + w, y + h, z).color(r, g, b, a).endVertex();
        builder.vertex(x, y + h, z).color(r, g, b, a).endVertex();
        builder.vertex(x, y, z).color(r, g, b, a).endVertex();

        matrixStack.popPose();
    }

    public static void renderItem(PoseStack matrixStack, MultiBufferSource buffer, double x, double y, ItemStack stack, @Nullable ResourceLocation lightmap, boolean border, float scale) {

        x *= 1.05;
        y *= 0.85;
        y += .45;

        matrixStack.pushPose();
        matrixStack.scale(0.1f * scale, 0.1f * scale, 0.1f * scale);
        matrixStack.translate((x * 0.95 - 3.7) / scale, (4.2 - y * 1.2) / scale, 0);
        matrixStack.scale(1, 1, 0.1f);
        if (!stack.isEmpty()) {

            ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
            BakedModel ibakedmodel = renderItem.getModel(stack, null, null, 1);
            renderItemModel(matrixStack, buffer, stack, ibakedmodel, ItemTransforms.TransformType.NONE, lightmap);
            if (border) {
                VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.LINESTRIP);
                x /= 200;
                x -= 0.47;
                y /= 100;
                y -= 0.5;

                double z = 0.3;
                double w = 0.9;
                builder.vertex(x, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.vertex(x + w, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.vertex(x + w, y + w, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.vertex(x, y + w, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.vertex(x, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            }
        }
        matrixStack.popPose();
    }

    private static void renderItemModel(PoseStack matrixStack, MultiBufferSource buffer, ItemStack stack, BakedModel bakedmodel, ItemTransforms.TransformType transform, @Nullable ResourceLocation lightmap) {
        ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();

        // @todo 1.15
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.bindForSetup(InventoryMenu.BLOCK_ATLAS);
        textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // @todo 1.18 GlStateManager._enableRescaleNormal();
        // @todo 1.18 GlStateManager._alphaFunc(516, 0.1F);
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);

        if (lightmap != null) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            // @todo 1.15
//            GlStateManager.activeTexture(GLX.GL_TEXTURE1);

//            Minecraft.getInstance()().getTextureManager().bind(new ResourceLocation(Ariente.MODID, "textures/gui/darken.png"));
            textureManager.bindForSetup(lightmap);
            // @todo 1.15
//            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
        }

        matrixStack.pushPose();
        // TODO: check if negative scale is a thing

        int lightmapValue = RenderHelper.MAX_BRIGHTNESS;
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, bakedmodel, transform, false);
        renderItem.render(stack, transform, false, matrixStack, buffer, lightmapValue, OverlayTexture.NO_OVERLAY, bakedmodel);

        // @todo 1.15
//        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        matrixStack.popPose();
        // @todo 1.18 GlStateManager._disableRescaleNormal();
        GlStateManager._disableBlend();
        GlStateManager._depthMask(false);
        textureManager.bindForSetup(InventoryMenu.BLOCK_ATLAS);
        textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).restoreLastBlurMipmap();

        if (lightmap != null) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
        }
    }


    public static void renderToolTip(PoseStack matrixStack, MultiBufferSource buffer, ItemStack stack, int x, int y, BiConsumer<ItemStack, List<String>> tooltipHandler) {
        // @todo 1.18 GuiUtils.preItemToolTip(stack);

        // @todo 1.18 net.minecraft.client.renderer.RenderHelper.setupForFlatItems();

        Minecraft mc = Minecraft.getInstance();
        TooltipFlag flag = mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
        // @todo 1.14 check?
        List<String> list = stack.getTooltipLines(mc.player, flag).stream().map(Component::getString).collect(Collectors.toList());

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
//                list.set(i, stack.getRarity().rarityColor + list.get(i));
                // @todo 1.14
                list.set(i, list.get(i));
            } else {
//                list.set(i, TextFormatting.GRAY + list.get(i));
                // @todo 1.16
                list.set(i, list.get(i));
            }
        }
        tooltipHandler.accept(stack, list);

        Font font = mc.font;
        // @todo 1.18 GuiUtils.drawHoveringText(matrixStack, list, x, y, 600, 500, -1, font);

        // @todo 1.15 need to begone!
        // @todo 1.18 net.minecraft.client.renderer.RenderHelper.turnOff();

        // @todo 1.18 GuiUtils.postItemToolTip();
    }

}
