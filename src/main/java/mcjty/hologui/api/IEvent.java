package mcjty.hologui.api;

import net.minecraft.entity.player.PlayerEntity;

public interface IEvent {

    void hit(IGuiComponent component, PlayerEntity player, IHoloGuiEntity entity, double x, double y);
}
