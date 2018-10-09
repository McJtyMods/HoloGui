package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IStackEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public interface ISlots extends IGuiComponent {

    // Which items from the inventory should be shown
    ISlots filter(Predicate<ItemStack> matcher);

    ISlots hitEvent(IStackEvent event);

    ISlots itemHandler(IItemHandler handler);

}
