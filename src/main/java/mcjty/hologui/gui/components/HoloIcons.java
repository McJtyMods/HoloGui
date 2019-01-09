package mcjty.hologui.gui.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconChoice;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoloIcons extends AbstractHoloComponent<IIconChoice> implements IIconChoice {

    private List<IImage> images = new ArrayList<>();

    private Function<EntityPlayer, Integer> currentValue;
    private IEvent hitEvent;

    HoloIcons(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconChoice addImage(IImage image) {
        images.add(image);
        return this;
    }

    @Override
    public IIconChoice getter(Function<EntityPlayer, Integer> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconChoice hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Integer value = currentValue.apply(player);
        if (value < 0 || value >= images.size()) {
            return;
        }
        IImage i = images.get(value);
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
