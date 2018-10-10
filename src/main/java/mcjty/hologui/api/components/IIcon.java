package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import net.minecraft.util.ResourceLocation;

public interface IIcon extends IGuiComponent<IIcon> {

    /// The resource location and dimension to use for this icon
    IIcon image(ResourceLocation resource, int w, int h);

    IIcon icon(int u, int v);
}
