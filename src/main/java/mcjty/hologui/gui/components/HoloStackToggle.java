package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
// @todo 1.18 import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HoloStackToggle extends AbstractHoloComponent<IStackToggle> implements IStackToggle {

    private ItemStack stack;
    private Function<Player, Boolean> currentValue;
    private IEvent hitEvent;
    float scale = 1.0f;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    public static final ResourceLocation DARKEN = new ResourceLocation(HoloGui.MODID, "textures/gui/darken.png");
    public static final ResourceLocation INVALID = new ResourceLocation(HoloGui.MODID, "textures/gui/darken_red.png");

    HoloStackToggle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IStackToggle scale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public IStackToggle itemStack(@Nonnull ItemStack stack) {
        this.stack = stack;
        return this;
    }

    @Override
    public IStackToggle getter(Function<Player, Boolean> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IStackToggle tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler) {
        this.tooltipHandler = tooltipHandler;
        return this;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Boolean state = currentValue.apply(player);
        ResourceLocation lightmap = null;
        boolean border = false;
        if (state == null) {
            lightmap = DARKEN;
        } else if (!state) {
        } else {
            border = true;
        }
        HoloGuiRenderTools.renderItem(matrixStack, buffer, x, y, stack, lightmap, border, scale);
        // @todo 1.18 RenderHelper.turnBackOn();
    }

    @Override
    public void renderTooltip(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStack.translate(0, 0, -10);
        matrixStack.scale(0.4f, 0.4f, 0.0f);
        HoloGuiRenderTools.renderToolTip(matrixStack, buffer, stack, (int) (x * 30 - 120), (int) (y * 30 - 120), tooltipHandler);
        matrixStack.popPose();
    }

    @Override
    public IStackToggle hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (hitEvent != null) {
            hitEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Entity ent = entity.getEntity();
        player.level.playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }


}
