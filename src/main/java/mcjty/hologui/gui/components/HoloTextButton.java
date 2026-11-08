package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.StyledColor;
import mcjty.hologui.api.components.IButton;
import mcjty.hologui.gui.ColorFromStyle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class HoloTextButton extends AbstractHoloComponent<IButton> implements IButton {

    private IEvent hitEvent;
    private IEvent hitClientEvent;
    private IColor color;
    private IColor hoverColor;
    private IColor borderColor;
    private IColor swatch;
    private String text;
    private float scale = 1;

    HoloTextButton(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.color = new ColorFromStyle(StyledColor.BUTTON);
        this.hoverColor = new ColorFromStyle(StyledColor.BUTTON_HOVER);
        this.borderColor = new ColorFromStyle(StyledColor.BUTTON_BORDER);
    }

    @Override
    public IButton text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public IButton scale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public IButton swatch(IColor color) {
        swatch = color;
        return this;
    }

    @Override
    public IButton color(int color) {
        this.color = () -> color;
        return this;
    }

    @Override
    public IButton hoverColor(int hoverColor) {
        this.hoverColor = () -> hoverColor;
        return this;
    }

    @Override
    public IButton borderColor(int borderColor) {
        this.borderColor = () -> borderColor;
        return this;
    }

    @Override
    public IButton color(IColor color) {
        this.color = color;
        return this;
    }

    @Override
    public IButton hoverColor(IColor color) {
        this.hoverColor = color;
        return this;
    }

    @Override
    public IButton borderColor(IColor color) {
        this.borderColor = color;
        return this;
    }

    @Override
    public IButton hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public IButton hitClientEvent(IEvent event) {
        this.hitClientEvent = event;
        return this;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        int color;
        if (isInside(cursorX, cursorY)) {
            color = this.hoverColor.getColor();
        } else {
            color = this.color.getColor();
        }
        if (swatch != null) {
            HoloGuiRenderTools.renderControl(matrixStack, buffer, x, y, w, h, isInside(cursorX, cursorY));
            double size = Math.min(.5, h - .24);
            double sx = x + w - .18 - size;
            double sy = y + (h - size) / 2;
            HoloGuiRenderTools.renderFill(matrixStack, buffer, sx, sy, size, size, swatch.getColor());
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, sx, sy, size, size, 192, 231, 240, 255);
            HoloGuiRenderTools.renderControlText(matrixStack, buffer, x + .18, y, w - size - .55, h, text, color, scale);
        } else {
            HoloGuiRenderTools.renderText(matrixStack, buffer, x, y, text, color, scale);
        }
        int bc = borderColor.getColor();
        if (bc != -1) {
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, x, y, w, h, bc & 255, (bc >> 8) & 255, (bc >> 16) & 255, (bc >> 24) & 255);
        }
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
        if (hitClientEvent != null) {
            hitClientEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }
}
