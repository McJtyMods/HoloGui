package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.Icons;
import net.minecraft.util.ResourceLocation;

public interface IIcon extends IGuiComponent<IIcon> {

    /**
     * The resource location and dimension to use for this icon.
     * If you don't do this then you'll get the default icon set (from Icons)
     */
    IIcon image(ResourceLocation resource, int w, int h);

    IIcon icon(int u, int v);

    IIcon icon(Icons icon);
}
