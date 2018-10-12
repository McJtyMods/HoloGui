package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.Icons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public interface IIconChoice extends IGuiComponent<IIconChoice> {

    /**
     * The resource location and dimension to use for this icon.
     * If you don't do this then you'll get the default icon set (from Icons)
     */
    IIconChoice image(ResourceLocation resource, int w, int h);

    IIconChoice getter(Function<EntityPlayer, Integer> getter);

    IIconChoice icon(int u, int v);

    IIconChoice icon(Icons icon);

    IIconChoice hitEvent(IEvent event);
}
