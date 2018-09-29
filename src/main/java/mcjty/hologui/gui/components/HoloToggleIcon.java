package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IIconToggle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.function.Function;

public class HoloToggleIcon extends AbstractHoloComponent implements IIconToggle {

    private static final ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");

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
    public HoloToggleIcon getter(Function<EntityPlayer, Boolean> getter) {
        this.currentValue = getter;
        return this;
    }

    @Override
    public HoloToggleIcon image(int u, int v) {
        this.normal_u = u;
        this.normal_v = v;
        return this;
    }

    @Override
    public HoloToggleIcon selected(int u, int v) {
        this.selected_u = u;
        this.selected_v = v;
        return this;
    }

    @Override
    public HoloToggleIcon hitEvent(IEvent event) {
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
        HoloGuiRenderTools.renderImage(x, y, u, v, 16, 16, 256, 256, image);
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
