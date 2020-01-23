package mcjty.hologui.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.lib.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class HoloGuiRenderTools {

    public static void renderText(double x, double y, String text, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.01*scale, 0.01*scale, 0.01);
        GlStateManager.rotatef(180, 0, 1, 0);
        GlStateManager.rotatef(180, 0, 0, 1);
        Minecraft.getInstance().fontRenderer.drawString(text, (int) (x * 10 / scale - 40 / scale), (int) (y * 10 / scale - 40 / scale), color);
        GlStateManager.popMatrix();
    }

    public static void renderTextShadow(double x, double y, String text, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.01*scale, 0.01*scale, 0.01);
        GlStateManager.rotatef(180, 0, 1, 0);
        GlStateManager.rotatef(180, 0, 0, 1);
        Minecraft.getInstance().fontRenderer.drawStringWithShadow(text, (int) (x * 10 - 40), (int) (y * 10 - 40), color);
        GlStateManager.popMatrix();
    }

    public static void renderImage(double x, double y, int u, int v, int w, int h, int txtw, int txth, ResourceLocation image) {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.01, 0.01, 0.01);
        GlStateManager.rotatef(180, 0, 1, 0);
        GlStateManager.rotatef(180, 0, 0, 1);
        GlStateManager.scaled(1, 1, 0.01);
        Minecraft.getInstance().getTextureManager().bindTexture(image);
        RenderHelper.drawTexturedModalRect((int) (x * 10 - 46), (int) (y * 10 - 44), u, v, w, h, txtw, txth);
        GlStateManager.popMatrix();
    }

    public static void renderBox(double x, double y, double w, double h, int color) {
        GlStateManager.pushMatrix();

        GlStateManager.scaled(0.10, 0.10, 0.10);
        GlStateManager.rotatef(180, 0, 1, 0);

        RenderHelper.drawFlatBox(3 - (int) x, 3 - (int) y, (int) (3 - x + w), (int) (3 - y + h), color, color);
//        RenderHelper.drawHorizontalLine((int) x, (int) y, (int) (x + w), color);
//        RenderHelper.drawHorizontalLine((int) x, (int) (y + h), (int) (x + w), color);
//        RenderHelper.drawVerticalLine((int) x, (int) y, (int) (y + h), color);
//        RenderHelper.drawVerticalLine((int) (x + w), (int) y, (int) (y + h), color);

        GlStateManager.popMatrix();
    }

    public static void renderBorder(double x, double y, double w, double h, int r, int g, int b, int a) {

        y += h-1;

        x *= 1.05;
        y *= 0.85;
        y += .45;

        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.1, 0.1, 0.1);
        GlStateManager.translated(x * 0.95 - 3.7, 4.2 - y * 1.2, 0);
        GlStateManager.scaled(1, 1, 0.1);

        GlStateManager.disableLighting();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        x /= 200;
        x -= 0.47;
        y /= 100;
        y -= 0.5;

        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
        GlStateManager.lineWidth(2.0F);
        GlStateManager.disableTexture();

        double z = 0.3;
        builder.pos(x, y, z).color(r, g, b, a).endVertex();
        builder.pos(x + w, y, z).color(r, g, b, a).endVertex();
        builder.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        builder.pos(x, y + h, z).color(r, g, b, a).endVertex();
        builder.pos(x, y, z).color(r, g, b, a).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture();
        GlStateManager.popMatrix();
    }

    public static void renderItem(double x, double y, ItemStack stack, @Nullable ResourceLocation lightmap, boolean border, double scale) {

        x *= 1.05;
        y *= 0.85;
        y += .45;

        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.1 * scale, 0.1 * scale, 0.1 * scale);
        GlStateManager.translated((x * 0.95 - 3.7) / scale, (4.2 - y * 1.2) / scale, 0);
        GlStateManager.scaled(1, 1, 0.1);
        if (!stack.isEmpty()) {
            GlStateManager.disableLighting();
            ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
            IBakedModel ibakedmodel = renderItem.getItemModelWithOverrides(stack, null, null);
            renderItemModel(stack, ibakedmodel, ItemCameraTransforms.TransformType.GUI, lightmap);
            if (border) {
                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder builder = tessellator.getBuffer();
                builder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
                x /= 200;
                x -= 0.47;
                y /= 100;
                y -= 0.5;

                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
                GlStateManager.lineWidth(2.0F);
                GlStateManager.disableTexture();

                double z = 0.3;
                double w = 0.9;
                builder.pos(x, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.pos(x + w, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.pos(x + w, y + w, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.pos(x, y + w, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                builder.pos(x, y, z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                tessellator.draw();

                GlStateManager.disableBlend();
                GlStateManager.enableTexture();
            }
        }
        GlStateManager.popMatrix();


    }

    private static void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, @Nullable ResourceLocation lightmap) {
        ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);

        if (lightmap != null) {
            Minecraft.getInstance().gameRenderer.getLightTexture().enableLightmap();
            // @todo 1.15
//            GlStateManager.activeTexture(GLX.GL_TEXTURE1);

//            Minecraft.getInstance()().getTextureManager().bindTexture(new ResourceLocation(Ariente.MODID, "textures/gui/darken.png"));
            Minecraft.getInstance().getTextureManager().bindTexture(lightmap);
            // @todo 1.15
//            GlStateManager.activeTexture(GLX.GL_TEXTURE0);
        }

        GlStateManager.pushMatrix();
        // TODO: check if negative scale is a thing
        // @todo 1.15
//        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, transform, false);

        // @todo 1.15
//        renderItem.renderItem(stack, bakedmodel);
        // @todo 1.15
//        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(false);
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getInstance().getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

        if (lightmap != null) {
            Minecraft.getInstance().gameRenderer.getLightTexture().disableLightmap();
        }
    }


    public static void renderToolTip(ItemStack stack, int x, int y, BiConsumer<ItemStack, List<String>> tooltipHandler) {
        GuiUtils.preItemToolTip(stack);

        net.minecraft.client.renderer.RenderHelper.setupGuiFlatDiffuseLighting();

        Minecraft mc = Minecraft.getInstance();
        ITooltipFlag flag = mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;
        // @todo 1.14 check?
        List<String> list = stack.getTooltip(mc.player, flag).stream().map(ITextComponent::getFormattedText).collect(Collectors.toList());

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
//                list.set(i, stack.getRarity().rarityColor + list.get(i));
                // @todo 1.14
                list.set(i, list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + list.get(i));
            }
        }
        tooltipHandler.accept(stack, list);

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        FontRenderer font1 = (font == null ? mc.fontRenderer : font);
        GuiUtils.drawHoveringText(list, x, y, 600, 500, -1, font1);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GuiUtils.postItemToolTip();
    }

}
