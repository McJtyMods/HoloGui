package mcjty.hologui.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Register this interface with IHoloGuiHandler to enable support for holo gui's on
 * blocks (usually tile entities)
 */
public interface IHoloGuiProvider {

    @Nullable
    IGuiTile getTile(Level world, BlockPos pos);
}
