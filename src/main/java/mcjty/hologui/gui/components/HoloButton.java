package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconButton;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

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
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IImage i;
        if (hover != null && isInside(cursorX, cursorY)) {
            i = hover;
        } else {
            i = image;
        }
        HoloGuiRenderTools.renderImage(matrixStack, buffer, x, y, i.getU(), i.getV(), 16, 16, i.getWidth(), i.getHeight(), i.getImage());
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
        player.level.playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        if (hitClientEvent != null) {
            hitClientEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }
}
