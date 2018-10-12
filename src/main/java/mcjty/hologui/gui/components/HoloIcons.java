package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.Icons;
import mcjty.hologui.api.components.IIconChoice;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HoloIcons extends AbstractHoloComponent<IIconChoice> implements IIconChoice {

    private ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");
    private int image_w = 256;
    private int image_h = 256;

    private List<Pair<Integer, Integer>> icons = new ArrayList<>();
    private Function<EntityPlayer, Integer> currentValue;
    private IEvent hitEvent;

    HoloIcons(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconChoice image(ResourceLocation resource, int w, int h) {
        this.image = resource;
        this.image_w = w;
        this.image_h = h;
        return this;
    }

    @Override
    public IIconChoice getter(Function<EntityPlayer, Integer> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconChoice icon(int u, int v) {
        icons.add(Pair.of(u, v));
        return this;
    }

    @Override
    public IIconChoice icon(Icons icon) {
        return icon(icon.getU(), icon.getV());
    }

    @Override
    public IIconChoice hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        Integer value = currentValue.apply(player);
        if (value < 0 || value >= icons.size()) {
            return;
        }
        int u = icons.get(value).getLeft();
        int v = icons.get(value).getRight();
        HoloGuiRenderTools.renderImage(x, y, u, v, 16, 16, image_w, image_h, image);
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
