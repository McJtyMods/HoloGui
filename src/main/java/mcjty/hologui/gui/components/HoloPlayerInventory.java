package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.*;
import mcjty.hologui.api.components.IPlayerInventory;
import mcjty.hologui.gui.ColorFromStyle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
    protected List<Pair<ItemStack, Integer>> getStacks(Player player) {
        List<Pair<ItemStack, Integer>> stacks = new ArrayList<>();
        for (int i = 9 ; i < 9 + 3*9 ; i++) {
            ItemStack stack = player.inventory.getItem(i);
            stacks.add(Pair.of(stack, i));
        }
        for (int i = 0 ; i < 9 ; i++) {
            ItemStack stack = player.inventory.getItem(i);
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
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IColor color = new ColorFromStyle(StyledColor.BORDER);
        int bc = color.getColor();
        if (bc != -1) {
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, x, y+3, w, 0.02, bc & 255, (bc >> 8) & 255, (bc >> 16) & 255, (bc >> 24) & 255);
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, x, y+1, w, 0.02, (bc & 255) / 2, ((bc >> 8) & 255) / 2, ((bc >> 16) & 255) / 2, ((bc >> 24) & 255) / 2);
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, x, y+2, w, 0.02, (bc & 255) / 2, ((bc >> 8) & 255) / 2, ((bc >> 16) & 255) / 2, ((bc >> 24) & 255) / 2);
            for (int i = 1 ; i < 9 ; i++) {
                HoloGuiRenderTools.renderBorder(matrixStack, buffer, x+i, y, 0.01, h, (bc & 255) / 2, ((bc >> 8) & 255) / 2, ((bc >> 16) & 255) / 2, ((bc >> 24) & 255) / 2);
            }
        }
        super.render(matrixStack, buffer, player, holo, cursorX, cursorY);
    }
}
