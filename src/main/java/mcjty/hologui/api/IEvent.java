package mcjty.hologui.api;

import net.minecraft.world.entity.player.Player;

public interface IEvent {

    void hit(IGuiComponent component, Player player, IHoloGuiEntity entity, double x, double y);
}
