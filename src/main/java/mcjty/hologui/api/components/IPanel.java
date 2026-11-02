package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IEvent;

import javax.annotation.Nullable;

import java.util.function.BooleanSupplier;

public interface IPanel extends IGuiComponent<IPanel> {

    IPanel add(IGuiComponent<?>... components);

    /** Hidden panels neither render nor expose their children for interaction. */
    IPanel visible(BooleanSupplier visible);

    /** Supply an optional manual action for the standard window controls. */
    IPanel help(IEvent event);

    @Nullable
    IEvent getHelpEvent();
}
