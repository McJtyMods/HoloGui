package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class HoloIcon extends AbstractHoloComponent implements IIcon {

    private static final ResourceLocation image = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");

    private int normal_u;
    private int normal_v;

    HoloIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public HoloIcon image(int u, int v) {
        this.normal_u = u;
        this.normal_v = v;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderImage(x, y, normal_u, normal_v, 16, 16, 256, 256, image);
//        HoloGuiRenderTools.renderText(x, y, "x", 0xffffff);
    }
}
