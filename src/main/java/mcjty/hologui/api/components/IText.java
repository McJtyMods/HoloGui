package mcjty.hologui.api.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IGuiComponent;

import java.util.function.Supplier;

public interface IText extends IGuiComponent<IText> {
    IText text(String text);

    IText text(Supplier<String> text);

    @Deprecated
    IText color(int color);
    IText color(IColor color);

    IText scale(float scale);
}
