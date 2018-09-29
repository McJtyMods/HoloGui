package mcjty.hologui.api;

import net.minecraft.entity.player.EntityPlayer;

public interface IEvent {

    void hit(IGuiComponent component, EntityPlayer player, IHoloGuiEntity entity, double x, double y);
}
