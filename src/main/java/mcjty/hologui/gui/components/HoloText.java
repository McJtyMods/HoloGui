package mcjty.hologui.gui.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IText;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Supplier;

public class HoloText extends AbstractHoloComponent<IText> implements IText {

    private Supplier<String> text;
    private IColor color;
    private float scale = 1.0f;

    HoloText(double x, double y, double w, double h) {
        super(x, y, w, h);
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
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        RenderHelper.disableStandardItemLighting();
        // @todo fix!
        HoloGuiRenderTools.renderText(x, y, text.get(), color.getColor(), scale);
    }
}
