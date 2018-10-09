package mcjty.hologui.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IStackEvent {

    // ItemStack can be empty in case the player hits an empty slot
    void hit(IGuiComponent component, EntityPlayer player, IHoloGuiEntity entity, double x, double y, @Nonnull ItemStack stack, int index);
}
