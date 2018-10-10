package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;

public interface IPanel extends IGuiComponent<IPanel> {
    IPanel add(IGuiComponent<?>... components);
}
