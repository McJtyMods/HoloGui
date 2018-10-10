package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public interface IIconChoice extends IGuiComponent<IIconChoice> {

    /// The resource location and dimension to use for this icon
    IIconChoice image(ResourceLocation resource, int w, int h);

    IIconChoice getter(Function<EntityPlayer, Integer> getter);

    IIconChoice icon(int u, int v);

    IIconChoice hitEvent(IEvent event);
}
