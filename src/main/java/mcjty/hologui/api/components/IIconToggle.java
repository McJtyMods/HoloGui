package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Function;

public interface IIconToggle extends IGuiComponent {
    IIconToggle getter(Function<EntityPlayer, Boolean> getter);

    IIconToggle image(int u, int v);

    IIconToggle selected(int u, int v);

    IIconToggle hitEvent(IEvent event);
}
