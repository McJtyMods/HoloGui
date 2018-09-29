package mcjty.hologui.api.components;

import mcjty.hologui.api.IGuiComponent;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IStackIcon extends IGuiComponent {

    IStackIcon itemStack(@Nonnull ItemStack stack);
}
