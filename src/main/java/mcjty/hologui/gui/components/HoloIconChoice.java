package mcjty.hologui.gui.components;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconChoice;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoloIconChoice extends AbstractHoloComponent<IIconChoice> implements IIconChoice {

    private List<IImage> images = new ArrayList<>();

    private Function<PlayerEntity, Integer> currentValue;
    private IEvent hitEvent;

    HoloIconChoice(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconChoice addImage(IImage image) {
        images.add(image);
        return this;
    }

    @Override
    public IIconChoice getter(Function<PlayerEntity, Integer> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconChoice hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void render(PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Integer value = currentValue.apply(player);
        if (value < 0 || value >= images.size()) {
            return;
        }
        IImage i = images.get(value);
        GlStateManager.color4f(1, 1, 1, 1);
        HoloGuiRenderTools.renderImage(x, y, i.getU(), i.getV(), 16, 16, i.getWidth(), i.getHeight(), i.getImage());
    }

    @Override
    public void hit(PlayerEntity player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (hitEvent != null) {
            hitEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }

    @Override
    public void hitClient(PlayerEntity player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Entity ent = entity.getEntity();
        player.world.playSound(ent.getPosX(), ent.getPosY(), ent.getPosZ(), HoloGuiSounds.guiclick, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
    }

}
