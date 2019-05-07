package mcjty.hologui.gui.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.StyledColor;
import mcjty.hologui.api.components.ITextChoice;
import mcjty.hologui.gui.ColorFromStyle;
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
    private IColor color;
    private IColor hoverColor;
    private IColor borderColor;
    private float scale = 1.0f;

    private Function<EntityPlayer, Integer> currentValue;
    private IEvent hitEvent;
    private IEvent hitEventClient;

    HoloTextChoice(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.color = new ColorFromStyle(StyledColor.BUTTON);
        this.hoverColor = new ColorFromStyle(StyledColor.BUTTON_HOVER);
        this.borderColor = new ColorFromStyle(StyledColor.BUTTON_BORDER);
    }

    @Override
    public ITextChoice addText(String text) {
        texts.add(text);
        return this;
    }

    @Override
    public ITextChoice color(int color) {
        this.color = () -> color;
        return this;
    }

    @Override
    public ITextChoice hoverColor(int color) {
        this.hoverColor = () -> color;
        return this;
    }

    @Override
    public ITextChoice borderColor(int color) {
        this.borderColor = () -> color;
        return this;
    }

    @Override
    public ITextChoice color(IColor color) {
        this.color = color;
        return this;
    }

    @Override
    public ITextChoice hoverColor(IColor color) {
        this.hoverColor = color;
        return this;
    }

    @Override
    public ITextChoice borderColor(IColor color) {
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
    public ITextChoice hitEventClient(IEvent event) {
        this.hitEventClient = event;
        return null;
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
            color = this.hoverColor.getColor();
        } else {
            color = this.color.getColor();
        }
        int bc = borderColor.getColor();
        if (bc != -1) {
            HoloGuiRenderTools.renderBorder(x, y, w, h, bc & 255, (bc >> 8) & 255, (bc >> 16) & 255, (bc >> 24) & 255);
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
        if (hitEventClient != null) {
            hitEventClient.hit(this, player, entity, cursorX, cursorY);
        }
    }

}
