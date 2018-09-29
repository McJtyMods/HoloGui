package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IIconButton;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class HoloButton extends AbstractHoloComponent implements IIconButton {

    private static final ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");

    private int normal_u;
    private int normal_v;
    private int hover_u;
    private int hover_v;
    private IEvent hitEvent;
    private IEvent hitClientEvent;

    HoloButton(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public HoloButton image(int u, int v) {
        this.normal_u = u;
        this.normal_v = v;
        return this;
    }

    @Override
    public HoloButton hover(int u, int v) {
        this.hover_u = u;
        this.hover_v = v;
        return this;
    }

    @Override
    public HoloButton hitEvent(IEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public HoloButton hitClientEvent(IEvent event) {
        this.hitClientEvent = event;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        int u;
        int v;
        if (isInside(cursorX, cursorY)) {
            u = hover_u;
            v = hover_v;
        } else {
            u = normal_u;
            v = normal_v;
        }
        HoloGuiRenderTools.renderImage(x, y, u, v, 16, 16, 256, 256, image);
//        HoloGuiRenderTools.renderText(x, y, "x", 0xffffff);
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
        if (hitClientEvent != null) {
            hitClientEvent.hit(this, player, entity, cursorX, cursorY);
        }
    }
}
