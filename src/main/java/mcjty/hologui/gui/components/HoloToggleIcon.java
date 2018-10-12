package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.Icons;
import mcjty.hologui.api.components.IIconToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.function.Function;

public class HoloToggleIcon extends AbstractHoloComponent<IIconToggle> implements IIconToggle {

    private ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");
    private int image_w = 256;
    private int image_h = 256;

    private int normal_u;
    private int normal_v;
    private int selected_u;
    private int selected_v;
    private Function<EntityPlayer, Boolean> currentValue;
    private IEvent hitEvent;

    HoloToggleIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIconToggle image(ResourceLocation resource, int w, int h) {
        this.image = resource;
        this.image_w = w;
        this.image_h = h;
        return this;
    }

    @Override
    public IIconToggle getter(Function<EntityPlayer, Boolean> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public IIconToggle icon(int u, int v) {
        this.normal_u = u;
        this.normal_v = v;
        return this;
    }

    @Override
    public IIconToggle icon(Icons icon) {
        return icon(icon.getU(), icon.getV());
    }

    @Override
    public IIconToggle selected(int u, int v) {
        this.selected_u = u;
        this.selected_v = v;
        return this;
    }

    @Override
    public IIconToggle selected(Icons icon) {
        return selected(icon.getU(), icon.getV());
    }

    @Override
    public IIconToggle hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }


    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        int u = normal_u;
        int v = normal_v;
        if (currentValue.apply(player)) {
            u = selected_u;
            v = selected_v;
        }
        GlStateManager.color(1, 1, 1, 1);
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
