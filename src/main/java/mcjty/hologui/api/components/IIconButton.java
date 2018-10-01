package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.util.ResourceLocation;

public interface IIconButton extends IGuiComponent {

    /// The resource location and dimension to use for this icon
    IIconButton image(ResourceLocation resource, int w, int h);

    IIconButton icon(int u, int v);

    IIconButton hover(int u, int v);

    IIconButton hitEvent(IEvent event);

    IIconButton hitClientEvent(IEvent event);
}
