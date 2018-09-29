package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Function;

public interface IIconChoice extends IGuiComponent {

    IIconChoice getter(Function<EntityPlayer, Integer> getter);

    IIconChoice icon(int u, int v);

    IIconChoice hitEvent(IEvent event);
}
