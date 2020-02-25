package mcjty.hologui.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.INumber;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;
import java.util.function.Function;

public class HoloNumber extends AbstractHoloComponent<INumber> implements INumber {

    private IColor color;
    private BiFunction<PlayerEntity, IHoloGuiEntity, Integer> getter;
    private Function<PlayerEntity, Integer> colorGetter = player -> color.getColor();

    HoloNumber(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public INumber getter(BiFunction<PlayerEntity, IHoloGuiEntity, Integer> getter) {
        this.getter = getter;
        return this;
    }

    @Override
    public INumber color(int color) {
        this.color = () -> color;
        return this;
    }

    @Override
    public INumber color(IColor color) {
        this.color = color;
        return this;
    }

    @Override
    public INumber colorGetter(Function<PlayerEntity, Integer> getter) {
        colorGetter = getter;
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        String text = Integer.toString(getter.apply(player, holo));
        HoloGuiRenderTools.renderText(matrixStack, buffer, x, y+.1, text, colorGetter.apply(player), 1.0f);
    }
}
