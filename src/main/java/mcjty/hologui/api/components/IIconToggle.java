package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.Icons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public interface IIconToggle extends IGuiComponent<IIconToggle> {

    /**
     * The resource location and dimension to use for this icon.
     * If you don't do this then you'll get the default icon set (from Icons)
     */
    IIconToggle image(ResourceLocation resource, int w, int h);

    IIconToggle getter(Function<EntityPlayer, Boolean> getter);

    IIconToggle icon(int u, int v);

    IIconToggle icon(Icons icon);

    IIconToggle selected(int u, int v);

    IIconToggle selected(Icons icon);

    IIconToggle hitEvent(IEvent event);
}
