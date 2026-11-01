package mcjty.hologui.api.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IButton extends IGuiComponent<IButton> {

    IButton text(String text);

    IButton scale(float scale);

    /** Show a framed color-picker button with a live color sample beside the label. */
    IButton swatch(IColor color);

    @Deprecated
    IButton color(int color);
    IButton color(IColor color);

    @Deprecated
    IButton hoverColor(int color);
    IButton hoverColor(IColor color);

    // -1 to disable border
    @Deprecated
    IButton borderColor(int color);
    IButton borderColor(IColor color);

    IButton hitEvent(IEvent event);

    IButton hitClientEvent(IEvent event);
}
