package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IIconButton extends IGuiComponent {

    IIconButton image(int u, int v);

    IIconButton hover(int u, int v);

    IIconButton hitEvent(IEvent event);

    IIconButton hitClientEvent(IEvent event);
}
