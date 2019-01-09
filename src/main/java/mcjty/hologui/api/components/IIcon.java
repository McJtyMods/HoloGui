package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;

public interface IIcon extends IGuiComponent<IIcon> {

    IIcon icon(IImage icon);
}
