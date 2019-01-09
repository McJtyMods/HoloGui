package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.IStackEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface ISlots extends IGuiComponent<ISlots> {

    // Which items from the inventory should be shown (item and index)
    ISlots filter(BiPredicate<ItemStack, Integer> matcher);

    ISlots hitEvent(IStackEvent event);

    ISlots doubleClickEvent(IStackEvent event);

    ISlots itemHandler(IItemHandler handler);

    ISlots withAmount();

    ISlots overlay(BiFunction<ItemStack, Integer, IImage> overlay);

    /**
     * If exactView is enabled empty slots will be shown as well as non-empty slots.
     * By default empty slots are skipped
     */
    ISlots exactView();

    // Get the index (in the itemhandler) of the selected stack. -1 if nothing is selected
    int getSelected();

    // Set the selection. Use -1 to clear the selection
    void setSelection(int index);
}
