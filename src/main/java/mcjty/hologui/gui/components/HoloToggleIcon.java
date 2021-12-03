package mcjty.hologui.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;

import java.util.function.Function;

public class HoloToggleIcon extends AbstractHoloComponent<IIconToggle> implements IIconToggle {

    private IImage image;
    private IImage selected;

    private Function<PlayerEntity, Boolean> currentValue;
    private IEvent hitEvent;

    HoloToggleIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconToggle getter(Function<PlayerEntity, Boolean> getter) {
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
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IImage i = image;
        if (selected != null && currentValue.apply(player)) {
            i = selected;
        }
        HoloGuiRenderTools.renderImage(matrixStack, buffer, x, y, i.getU(), i.getV(), 16, 16, i.getWidth(), i.getHeight(), i.getImage());
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
        player.level.playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.guiclick, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

}
