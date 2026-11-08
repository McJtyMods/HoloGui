package mcjty.hologui.api;

import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IStackEvent {

    // ItemStack can be empty in case the player hits an empty slot
    void hit(IGuiComponent component, Player player, IHoloGuiEntity entity, double x, double y, @Nonnull ItemStack stack, int index);
}
