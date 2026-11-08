package mcjty.hologui.api.components;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.world.item.ItemStack;

public interface IStackIcon extends IGuiComponent<IStackIcon> {

    IStackIcon itemStack(@Nonnull ItemStack stack);

    IStackIcon itemStack(Supplier<ItemStack> supplier);

    IStackIcon scale(float scale);

    IStackIcon tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler);
}
