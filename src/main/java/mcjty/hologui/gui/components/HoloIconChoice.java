package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIconChoice;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoloIconChoice extends AbstractHoloComponent<IIconChoice> implements IIconChoice {

    private List<IImage> images = new ArrayList<>();

    private Function<Player, Integer> currentValue;
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
    public IIconChoice getter(Function<Player, Integer> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconChoice hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Integer value = currentValue.apply(player);
        if (value < 0 || value >= images.size()) {
            return;
        }
        IImage i = images.get(value);
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
        player.level().playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
    }

}
