package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;

import java.util.function.Supplier;

public interface IModeToggle extends IGuiComponent {
    IModeToggle getter(Supplier<Integer> getter);

    IModeToggle hitEvent(IEvent event);

    IModeToggle choice(int u, int v);
}
