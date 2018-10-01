package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public interface IIconToggle extends IGuiComponent {

    /// The resource location and dimension to use for this icon
    IIconToggle image(ResourceLocation resource, int w, int h);

    IIconToggle getter(Function<EntityPlayer, Boolean> getter);

    IIconToggle icon(int u, int v);

    IIconToggle selected(int u, int v);

    IIconToggle hitEvent(IEvent event);
}
