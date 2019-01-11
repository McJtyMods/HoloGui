package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HoloStackToggle extends AbstractHoloComponent<IStackToggle> implements IStackToggle {

    private ItemStack stack;
    private Function<EntityPlayer, Boolean> currentValue;
    private IEvent hitEvent;
    double scale = 1.0;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    public static final ResourceLocation DARKEN = new ResourceLocation(HoloGui.MODID, "textures/gui/darken.png");
    public static final ResourceLocation INVALID = new ResourceLocation(HoloGui.MODID, "textures/gui/darken_red.png");

    HoloStackToggle(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IStackToggle scale(double scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public IStackToggle itemStack(@Nonnull ItemStack stack) {
        this.stack = stack;
        return this;
    }

    @Override
    public IStackToggle getter(Function<EntityPlayer, Boolean> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IStackToggle tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler) {
        this.tooltipHandler = tooltipHandler;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Boolean state = currentValue.apply(player);
        ResourceLocation lightmap = null;
        boolean border = false;
        if (state == null) {
            lightmap = DARKEN;
        } else if (!state) {
        } else {
            border = true;
        }
        HoloGuiRenderTools.renderItem(x, y, stack, lightmap, border, scale);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void renderTooltip(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.01, 0.01, 0.01);
        GlStateManager.rotate(180, 0, 1, 0);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.translate(0, 0, -10);
        GlStateManager.scale(0.4, 0.4, 0.0);
        HoloGuiRenderTools.renderToolTip(stack, (int) (x * 30 - 120), (int) (y * 30 - 120), tooltipHandler);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public IStackToggle hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void hit(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (hitEvent != null) {
            hitEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }

    @Override
    public void hitClient(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Entity ent = entity.getEntity();
        player.world.playSound(ent.posX, ent.posY, ent.posZ, HoloGuiSounds.guiclick, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
    }


}
