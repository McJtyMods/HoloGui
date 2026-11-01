package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;

import java.util.function.BooleanSupplier;

public interface IPanel extends IGuiComponent<IPanel> {

    IPanel add(IGuiComponent<?>... components);

    /** Hidden panels neither render nor expose their children for interaction. */
    IPanel visible(BooleanSupplier visible);
}
