package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Function;

public interface IIconToggle extends IGuiComponent<IIconToggle> {

    IIconToggle getter(Function<EntityPlayer, Boolean> getter);

    IIconToggle icon(IImage icon);

    IIconToggle selected(IImage icon);

    IIconToggle hitEvent(IEvent event);
}
