package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IButton extends IGuiComponent {
    IButton text(String text);

    IButton color(int color);

    IButton hitEvent(IEvent event);

    IButton hitClientEvent(IEvent event);
}
