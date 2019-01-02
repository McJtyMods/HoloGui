package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IStackEvent;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface IPlayerSlots extends IGuiComponent<IPlayerSlots> {

    // Which items from the player inventory should be shown (item and index)
    IPlayerSlots filter(BiPredicate<ItemStack, Integer> matcher);

    IPlayerSlots hitEvent(IStackEvent event);

    IPlayerSlots withAmount();

    // Get the index (in the player inventory) of the selected stack. -1 if nothing is selected
    int getSelected();

    // Set the selection. Use -1 to clear the selection
    void setSelection(int index);
}
