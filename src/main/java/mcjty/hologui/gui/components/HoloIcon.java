package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.components.IIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

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
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderImage(matrixStack, buffer, x, y, image.getU(), image.getV(), 16, 16, image.getWidth(), image.getHeight(), image.getImage());
    }
}
