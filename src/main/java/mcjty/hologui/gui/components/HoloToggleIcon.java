package mcjty.hologui.gui.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

import java.util.function.Function;

public class HoloToggleIcon extends AbstractHoloComponent<IIconToggle> implements IIconToggle {

    private IImage image;
    private IImage selected;

    private Function<EntityPlayer, Boolean> currentValue;
    private IEvent hitEvent;

    HoloToggleIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconToggle getter(Function<EntityPlayer, Boolean> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconToggle icon(IImage icon) {
        this.image = icon;
        return this;
    }

    @Override
    public IIconToggle selected(IImage icon) {
        this.selected = icon;
        return this;
    }

    @Override
    public IIconToggle hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }


    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IImage i = image;
        if (selected != null && currentValue.apply(player)) {
            i = selected;
        }
        GlStateManager.color(1, 1, 1, 1);
        HoloGuiRenderTools.renderImage(x, y, i.getU(), i.getV(), 16, 16, i.getWidth(), i.getHeight(), i.getImage());
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
