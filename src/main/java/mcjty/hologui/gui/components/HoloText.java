package mcjty.hologui.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.StyledColor;
import mcjty.hologui.api.components.IText;
import mcjty.hologui.gui.ColorFromStyle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Supplier;

public class HoloText extends AbstractHoloComponent<IText> implements IText {

    private Supplier<String> text = () -> "";
    private IColor color;
    private float scale = 1.0f;

    HoloText(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.color = new ColorFromStyle(StyledColor.LABEL);
    }

    @Override
    public IText text(String text) {
        this.text = () -> text;
        return this;
    }

    @Override
    public IText text(Supplier<String> text) {
        this.text = text;
        return this;
    }

    @Override
    public IText color(int color) {
        this.color = () -> color;
        return this;
    }

    @Override
    public IText color(IColor color) {
        this.color = color;
        return this;
    }

    @Override
    public IText scale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        RenderHelper.disableStandardItemLighting();
        // @todo fix!
        HoloGuiRenderTools.renderText(matrixStack, buffer, x, y, text.get(), color.getColor(), scale);
    }
}
