package mcjty.hologui.gui.components;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconButton;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;

public class HoloButton extends AbstractHoloComponent<IIconButton> implements IIconButton {

    private IImage image;
    private IImage hover;

    private IEvent hitEvent;
    private IEvent hitClientEvent;

    HoloButton(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconButton icon(IImage image) {
        this.image = image;
        return this;
    }

    @Override
    public IIconButton hover(IImage image) {
        this.hover = image;
        return this;
    }

    @Override
    public IIconButton hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public IIconButton hitClientEvent(IEvent event) {
        this.hitClientEvent = event;
        return this;
    }

    @Override
    public void render(PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IImage i;
        if (hover != null && isInside(cursorX, cursorY)) {
            i = hover;
        } else {
            i = image;
        }
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
        if (hitClientEvent != null) {
            hitClientEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }
}
