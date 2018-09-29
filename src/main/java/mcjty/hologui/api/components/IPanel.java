package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;

public interface IPanel extends IGuiComponent {
    IPanel add(IGuiComponent... components);
}
