package mcjty.hologui.gui.components;

import mcjty.hologui.api.IImage;
import mcjty.hologui.api.IStackEvent;
import mcjty.hologui.api.components.ISlots;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class HoloSlots extends AbstractSlots<ISlots> implements ISlots {

    private IItemHandler handler;

    public HoloSlots(double x, double y, double w, double h) {
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
    protected List<Pair<ItemStack, Integer>> getStacks(Player player) {
        List<Pair<ItemStack, Integer>> stacks = new ArrayList<>();
        for (int i = 0 ; i < handler.getSlots() ; i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (filter.test(stack, i)) {
                stacks.add(Pair.of(stack, i));
            }
        }
        return stacks;
    }

    @Override
    public ISlots itemHandler(IItemHandler handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public ISlots filter(BiPredicate<ItemStack, Integer> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public ISlots overlay(BiFunction<ItemStack, Integer, IImage> overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public ISlots hitEvent(IStackEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public ISlots doubleClickEvent(IStackEvent event) {
        this.doubleClickEvent = event;
        return this;
    }

}
