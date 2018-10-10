package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IStackEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Predicate;

public interface ISlots extends IGuiComponent<ISlots> {

    // Which items from the inventory should be shown
    ISlots filter(Predicate<ItemStack> matcher);

    ISlots hitEvent(IStackEvent event);

    ISlots itemHandler(IItemHandler handler);

    // Get the index (in the itemhandler) of the selected stack. -1 if nothing is selected
    int getSelected();

    // Set the selection. Use -1 to clear the selection
    void setSelection(int index);
}
