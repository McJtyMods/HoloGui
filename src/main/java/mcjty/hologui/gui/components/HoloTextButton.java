package mcjty.hologui.gui.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IButton;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

public class HoloTextButton extends AbstractHoloComponent<IButton> implements IButton {

    private IEvent hitEvent;
    private IEvent hitClientEvent;
    private int color;
    private int hoverColor;
    private String text;

    HoloTextButton(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.color = 0x777777;
        this.hoverColor = 0xffffff;
    }

    @Override
    public IButton text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public IButton color(int color) {
        this.color = color;
        return this;
    }

    public IButton hover(int hoverColor) {
        this.hoverColor = hoverColor;
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
            color = this.hoverColor;
        } else {
            color = this.color;
        }
        RenderHelper.disableStandardItemLighting();
        HoloGuiRenderTools.renderText(x, y, text, color);
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
