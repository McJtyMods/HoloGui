package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IButton extends IGuiComponent<IButton> {

    IButton text(String text);

    IButton color(int color);

    IButton hoverColor(int color);

    // -1 to disable border
    IButton borderColor(int color);

    IButton hitEvent(IEvent event);

    IButton hitClientEvent(IEvent event);
}
