package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.Icons;
import net.minecraft.util.ResourceLocation;

public interface IIconButton extends IGuiComponent<IIconButton> {

    /**
     * The resource location and dimension to use for this icon.
     * If you don't do this then you'll get the default icon set (from Icons)
     */
    IIconButton image(ResourceLocation resource, int w, int h);

    IIconButton icon(int u, int v);

    IIconButton icon(Icons icon);

    IIconButton hover(int u, int v);

    IIconButton hover(Icons icon);

    IIconButton hitEvent(IEvent event);

    IIconButton hitClientEvent(IEvent event);
}
