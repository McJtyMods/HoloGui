package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IPlayerSlots;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HoloPlayerSlots extends AbstractHoloComponent implements IPlayerSlots {

    private Predicate<ItemStack> filter = itemStack -> false;

    public HoloPlayerSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderBorder(x, y, w, h, 255, 255, 255, 255);
        double yy = y;
        double xx = x;

        for (ItemStack stack : getStacks(player)) {
            HoloGuiRenderTools.renderItem(xx, yy, stack, null, false, 0.9);
            xx++;
            if (xx >= x+w) {
                yy++;
                xx = x;
                if (yy >= y+h) {
                    break;
                }
            }
        }
    }

    private List<ItemStack> getStacks(EntityPlayer player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (filter.test(stack)) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    @Override
    public IPlayerSlots filter(Predicate<ItemStack> filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public void renderTooltip(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        List<ItemStack> stacks = getStacks(player);

        int xx = (int) (cursorX - x);
        int yy = (int) (cursorY - y);
        int ww = (int) w;

        int index = yy * ww + xx % ww;

        if (index >= 0 && index < stacks.size()) {
            ItemStack stack = stacks.get(index);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.01, 0.01, 0.01);
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.translate(0, 0, -10);
            GlStateManager.scale(0.4, 0.4, 0.0);
            HoloGuiRenderTools.renderToolTip(stack, (int) ((xx+x) * 30 - 120), (int) ((yy+y+.5) * 30 - 120));
            GlStateManager.popMatrix();
        }
    }

}
