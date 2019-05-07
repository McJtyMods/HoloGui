package mcjty.hologui.api.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IButton extends IGuiComponent<IButton> {

    IButton text(String text);

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
