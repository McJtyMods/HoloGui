package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;

public interface IIconButton extends IGuiComponent<IIconButton> {

    IIconButton icon(IImage image);

    IIconButton hover(IImage image);

    IIconButton hitEvent(IEvent event);

    IIconButton hitClientEvent(IEvent event);
}
