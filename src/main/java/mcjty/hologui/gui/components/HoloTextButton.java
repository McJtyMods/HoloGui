package mcjty.hologui.gui.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.StyledColor;
import mcjty.hologui.api.components.IButton;
import mcjty.hologui.gui.ColorFromStyle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class HoloTextButton extends AbstractHoloComponent<IButton> implements IButton {

    private IEvent hitEvent;
    private IEvent hitClientEvent;
    private IColor color;
    private IColor hoverColor;
    private IColor borderColor;
    private String text;

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
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        int color;
        if (isInside(cursorX, cursorY)) {
            color = this.hoverColor.getColor();
        } else {
            color = this.color.getColor();
        }
        RenderHelper.disableStandardItemLighting();
        int bc = borderColor.getColor();
        if (bc != -1) {
            HoloGuiRenderTools.renderBorder(x, y, w, h, bc & 255, (bc >> 8) & 255, (bc >> 16) & 255, (bc >> 24) & 255);
        }
        HoloGuiRenderTools.renderText(x, y, text, color, 1.0f);
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
        if (hitClientEvent != null) {
            hitClientEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }
}
