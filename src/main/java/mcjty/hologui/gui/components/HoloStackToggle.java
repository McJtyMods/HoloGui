package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import mcjty.lib.client.RenderBufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// @todo 1.18 import RenderHelper;

public class HoloStackToggle extends AbstractHoloComponent<IStackToggle> implements IStackToggle {

    private ItemStack stack;
    private Function<Player, Boolean> currentValue;
    private IEvent hitEvent;
    float scale = 1.0f;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    public static final Identifier DARKEN = Identifier.fromNamespaceAndPath(HoloGui.MODID, "textures/gui/darken.png");
    public static final Identifier INVALID = Identifier.fromNamespaceAndPath(HoloGui.MODID, "textures/gui/darken_red.png");

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
    public void render(PoseStack matrixStack, RenderBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Boolean state = currentValue.apply(player);
        // Installed is bright; inventory-only is dim; unavailable is darker still.
        float brightness = state == null ? .25f : state ? 1 : .55f;
        HoloGuiRenderTools.renderItem(matrixStack, buffer, x, y, stack, brightness, false, scale);
        // @todo 1.18 RenderHelper.turnBackOn();
    }

    @Override
    public void renderTooltip(PoseStack matrixStack, RenderBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
        matrixStack.translate(0, 0, -10);
        matrixStack.scale(0.4f, 0.4f, 1.0f);
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
        player.level().playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

}
