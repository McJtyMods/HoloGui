package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.Icons;
import mcjty.hologui.api.components.IIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class HoloIcon extends AbstractHoloComponent<IIcon> implements IIcon {

    private ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");
    private int image_w = 256;
    private int image_h = 256;

    private int normal_u;
    private int normal_v;

    HoloIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIcon image(ResourceLocation resource, int w, int h) {
        this.image = resource;
        this.image_w = w;
        this.image_h = h;
        return this;
    }

    @Override
    public IIcon icon(int u, int v) {
        this.normal_u = u;
        this.normal_v = v;
        return this;
    }

    @Override
    public IIcon icon(Icons icon) {
        return icon(icon.getU(), icon.getV());
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderImage(x, y, normal_u, normal_v, 16, 16, image_w, image_h, image);
//        HoloGuiRenderTools.renderText(x, y, "x", 0xffffff);
    }
}
