package mcjty.hologui.api.components;

import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

public interface IPanel extends IGuiComponent<IPanel> {

    IPanel add(IGuiComponent<?>... components);

    /** Hidden panels neither render nor expose their children for interaction. */
    IPanel visible(BooleanSupplier visible);

    /** Supply an optional manual action for the standard window controls. */
    IPanel help(IEvent event);

    @Nullable
    IEvent getHelpEvent();
}
