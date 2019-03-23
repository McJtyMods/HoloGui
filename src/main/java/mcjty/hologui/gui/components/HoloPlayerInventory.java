package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IImage;
import mcjty.hologui.api.IStackEvent;
import mcjty.hologui.api.components.IPlayerInventory;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class HoloPlayerInventory extends AbstractSlots<IPlayerInventory> implements IPlayerInventory {

    public HoloPlayerInventory(double y) {
        super(-0.3, y, 9, 4);
        exactView();
        withAmount();
        fullBright();
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
    protected List<Pair<ItemStack, Integer>> getStacks(EntityPlayer player) {
        List<Pair<ItemStack, Integer>> stacks = new ArrayList<>();
        for (int i = 9 ; i < 9 + 3*9 ; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            stacks.add(Pair.of(stack, i));
        }
        for (int i = 0 ; i < 9 ; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            stacks.add(Pair.of(stack, i));
        }
        return stacks;
    }

    @Override
    public IPlayerInventory overlay(BiFunction<ItemStack, Integer, IImage> overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public IPlayerInventory hitEvent(IStackEvent event) {
        this.hitEvent = event;
        return this;
    }

    @Override
    public IPlayerInventory doubleClickEvent(IStackEvent event) {
        this.doubleClickEvent = event;
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderBorder(x, y+3, w, 0.02, 128, 200, 255, 255);
        HoloGuiRenderTools.renderBorder(x, y+1, w, 0.02, 60, 100, 150, 128);
        HoloGuiRenderTools.renderBorder(x, y+2, w, 0.02, 60, 100, 150, 128);
        for (int i = 1 ; i < 9 ; i++) {
            HoloGuiRenderTools.renderBorder(x+i, y, 0.01, h, 60, 100, 150, 128);
        }
        super.render(player, holo, cursorX, cursorY);
    }
}
