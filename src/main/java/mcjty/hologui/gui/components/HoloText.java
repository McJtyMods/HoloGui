package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IText;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Supplier;

public class HoloText extends AbstractHoloComponent<IText> implements IText {

    private Supplier<String> text;
    private int color;

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
        this.color = color;
        return this;
    }


    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        RenderHelper.disableStandardItemLighting();
        HoloGuiRenderTools.renderText(x, y, text.get(), color);
    }
}
