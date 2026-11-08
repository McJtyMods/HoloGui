package mcjty.hologui.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.LinkedHashMap;
import java.util.Map;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class HoloGuiEntityRender extends EntityRenderer<HoloGuiEntity> {

    public static final ResourceLocation GUI_BACKGROUND_1 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_blue_softwhite");
    public static final ResourceLocation GUI_BACKGROUND_2 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_blue");
    public static final ResourceLocation GUI_BACKGROUND_3 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_blue_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_4 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_blue_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_5 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_blue_softblack");
    public static final ResourceLocation GUI_BACKGROUND_6 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_gray_sharpblack");
    public static final ResourceLocation GUI_BACKGROUND_7 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_gray_sharpwhite");
    public static final ResourceLocation GUI_BACKGROUND_8 = ResourceLocation.fromNamespaceAndPath(HoloGui.MODID, "hologui_gray_softblack");

    // Foil combines a glint consumer with the item's consumer. They need distinct builders:
    // the fallback builder is reused between render types and would cause Duplicate delegates.
    private static final MultiBufferSource.BufferSource GUI_BUFFERS = MultiBufferSource.immediateWithBuffers(new LinkedHashMap<>(Map.of(
            RenderType.glint(), new ByteBufferBuilder(256),
            RenderType.glintTranslucent(), new ByteBufferBuilder(256),
            RenderType.entityGlint(), new ByteBufferBuilder(256),
            RenderType.entityGlintDirect(), new ByteBufferBuilder(256),
            RenderType.armorEntityGlint(), new ByteBufferBuilder(256))), new ByteBufferBuilder(4096));

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
    public void render(HoloGuiEntity entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
        doActualRender(entity, partialTicks, stack, buffer);
    }

    public static void doActualRender(HoloGuiEntity entity, PoseStack matrixStack, MultiBufferSource buffer) {
        doActualRender(entity, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true), matrixStack, buffer);
    }

    private static void doActualRender(HoloGuiEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer) {

        // Keep the panel and its widgets ordered independently of the world's deferred buffers.
        // All GUI passes target the main framebuffer, including translucent item icons.
        MultiBufferSource guiBuffer = type -> GUI_BUFFERS.getBuffer(HoloGuiRenderTools.guiRenderType(type));
        Lighting.setupForFlatItems();
        matrixStack.pushPose();
        try {
            renderPanel(entity, partialTicks, matrixStack, guiBuffer);
            GUI_BUFFERS.endBatch();
        } finally {
            matrixStack.popPose();
            if (Minecraft.getInstance().level.effects().constantAmbientLight()) {
                Lighting.setupNetherLevel();
            } else {
                Lighting.setupLevel();
            }
        }
    }

    private static void renderPanel(HoloGuiEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer) {
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getViewYRot(partialTicks)));
        matrixStack.translate(0, .5, 0);
        float scale = entity.getScale();
        scale = 1f - (1f-scale) * (.4f / .25f);

        matrixStack.scale(scale, scale, scale);

        int style = Config.GUI_STYLE.get().ordinal();
        if (style > 8) {
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

        VertexConsumer builder = buffer.getBuffer(HoloGuiRenderType.HOLOGUI_BACKGROUND);
        renderQuadColor(builder, matrixStack.last().pose(), -.5f, .5f, -.5f, .5f, 255, 255, 255, 255, sprite);
        GUI_BUFFERS.endBatch();

        // The transformed panel's +Z points toward the player. Widgets must stay in front of it.
        matrixStack.translate(0, 0, .01);
        entity.updateCursor(Minecraft.getInstance().player, partialTicks);
        float cursorX = (float) entity.getCursorX();
        float cursorY = (float) entity.getCursorY();

        if (cursorX >= 0 && cursorX <= 10 && cursorY >= 0 && cursorY <= 10) {
            builder = buffer.getBuffer(HoloGuiRenderType.HOLOGUI_BACKGROUND);
            float offset = .01f;
            renderQuadColor(builder, matrixStack.last().pose(), (cursorX / 10.0f) - .42f - offset, (cursorX / 10.0f) - .42f + offset,
                    - ((cursorY / 10) -.42f - offset),  - ((cursorY / 10) -.42f + offset),
                    60, 255, 128, 100, sprite);
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
    }

    private static void renderQuadColor(VertexConsumer builder, Matrix4f matrix, float minX, float maxX, float minY, float maxY, int r, int g, int b, int a,
                                        TextureAtlasSprite sprite) {
        builder.addVertex(matrix, minX, minY, 0).setColor(r, g, b, a).setUv(sprite.getU0(), sprite.getV0()).setLight(LightTexture.FULL_BRIGHT);
        builder.addVertex(matrix, maxX, minY, 0).setColor(r, g, b, a).setUv(sprite.getU1(), sprite.getV0()).setLight(LightTexture.FULL_BRIGHT);
        builder.addVertex(matrix, maxX, maxY, 0).setColor(r, g, b, a).setUv(sprite.getU1(), sprite.getV1()).setLight(LightTexture.FULL_BRIGHT);
        builder.addVertex(matrix, minX, maxY, 0).setColor(r, g, b, a).setUv(sprite.getU0(), sprite.getV1()).setLight(LightTexture.FULL_BRIGHT);
    }

    @Override
    public ResourceLocation getTextureLocation(HoloGuiEntity pEntity) {
        return null;
    }
}
