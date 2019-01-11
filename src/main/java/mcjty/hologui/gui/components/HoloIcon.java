package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.Icons;
import mcjty.hologui.api.components.IIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class HoloIcon extends AbstractHoloComponent<IIcon> implements IIcon {

    private IImage image;

    HoloIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIcon icon(IImage icon) {
        this.image = icon;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        GlStateManager.color(1, 1, 1);
        HoloGuiRenderTools.renderImage(x, y, image.getU(), image.getV(), 16, 16, image.getWidth(), image.getHeight(), image.getImage());
//        HoloGuiRenderTools.renderText(x, y, "x", 0xffffff);
    }
}
