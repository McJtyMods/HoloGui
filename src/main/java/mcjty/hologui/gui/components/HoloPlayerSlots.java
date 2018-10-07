package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IPlayerSlots;
import mcjty.hologui.api.components.ISlots;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class HoloPlayerSlots extends AbstractHoloComponent implements IPlayerSlots {

    private Predicate<ItemStack> filter = itemStack -> false;

    public HoloPlayerSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        double yy = y;
        double xx = x;
        for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (filter.test(stack)) {
                HoloGuiRenderTools.renderItem(xx * 1.05, yy * 0.85 + .45, stack, null, false);
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
    }

    @Override
    public IPlayerSlots filter(Predicate<ItemStack> filter) {
        this.filter = filter;
        return this;
    }
}
