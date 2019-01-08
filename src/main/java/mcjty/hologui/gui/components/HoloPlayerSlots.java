package mcjty.hologui.gui.components;

import mcjty.hologui.api.IStackEvent;
import mcjty.hologui.api.components.IPlayerSlots;
import mcjty.hologui.api.components.ISlots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class HoloPlayerSlots extends AbstractSlots<IPlayerSlots> implements IPlayerSlots {

    public HoloPlayerSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public int getSelected() {
        return selected;
    }

    @Override
    public void setSelection(int index) {
        selected = index;
    }

    @Override
    public IPlayerSlots withAmount() {
        return super.withAmount();
    }

    @Override
    protected List<Pair<ItemStack, Integer>> getStacks(EntityPlayer player) {
        List<Pair<ItemStack, Integer>> stacks = new ArrayList<>();
        for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (filter.test(stack, i)) {
                stacks.add(Pair.of(stack, i));
            }
        }
        return stacks;
    }

    @Override
    public IPlayerSlots filter(BiPredicate<ItemStack, Integer> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public IPlayerSlots overlay(BiFunction<ItemStack, Integer, ResourceLocation> overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public IPlayerSlots hitEvent(IStackEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public IPlayerSlots doubleClickEvent(IStackEvent event) {
        this.doubleClickEvent = event;
        return this;
    }
}
