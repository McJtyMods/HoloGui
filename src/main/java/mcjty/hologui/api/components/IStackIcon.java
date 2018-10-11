package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface IStackIcon extends IGuiComponent<IStackIcon> {

    IStackIcon itemStack(@Nonnull ItemStack stack);

    IStackIcon itemStack(Supplier<ItemStack> supplier);

    IStackIcon scale(double scale);
}
