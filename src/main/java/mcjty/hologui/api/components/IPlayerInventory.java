package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.IStackEvent;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface IPlayerInventory extends IGuiComponent<IPlayerInventory> {

    IPlayerInventory hitEvent(IStackEvent event);

    IPlayerInventory doubleClickEvent(IStackEvent event);

    IPlayerInventory overlay(BiFunction<ItemStack, Integer, IImage> overlay);

    IPlayerInventory tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler);

    // Get the index (in the player inventory) of the selected stack. -1 if nothing is selected
    int getSelected();

    // Set the selection. Use -1 to clear the selection
    void setSelection(int index);
}
