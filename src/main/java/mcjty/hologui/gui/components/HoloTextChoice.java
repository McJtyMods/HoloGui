package mcjty.hologui.gui.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.ITextChoice;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoloTextChoice extends AbstractHoloComponent<ITextChoice> implements ITextChoice {

    private List<String> texts = new ArrayList<>();
    private int color;
    private int hoverColor;
    private int borderColor;
    private float scale = 1.0f;

    private Function<EntityPlayer, Integer> currentValue;
    private IEvent hitEvent;

    HoloTextChoice(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.color = 0x888888;
        this.hoverColor = 0xffffff;
        this.borderColor = 0xff7fc9ff;
    }

    @Override
    public ITextChoice addText(String text) {
        texts.add(text);
        return this;
    }

    @Override
    public ITextChoice color(int color) {
        this.color = color;
        return this;
    }

    @Override
    public ITextChoice hoverColor(int color) {
        this.hoverColor = color;
        return this;
    }

    @Override
    public ITextChoice borderColor(int color) {
        this.borderColor = color;
        return this;
    }

    @Override
    public ITextChoice scale(float scale) {
        this.scale = scale;
        return this;
    }


    @Override
    public ITextChoice getter(Function<EntityPlayer, Integer> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public ITextChoice hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Integer value = currentValue.apply(player);
        if (value < 0 || value >= texts.size()) {
            return;
        }
        String i = texts.get(value);
        RenderHelper.disableStandardItemLighting();
        int color;
        if (isInside(cursorX, cursorY)) {
            color = this.hoverColor;
        } else {
            color = this.color;
        }
        if (borderColor != -1) {
            HoloGuiRenderTools.renderBorder(x, y, w, h, borderColor & 255, (borderColor >> 8) & 255, (borderColor >> 16) & 255, (borderColor >> 24) & 255);
        }
        HoloGuiRenderTools.renderText(x, y, i, color, scale);
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
