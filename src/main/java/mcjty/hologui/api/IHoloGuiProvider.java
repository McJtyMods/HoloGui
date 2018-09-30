package mcjty.hologui.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Register this interface with IHoloGuiHandler to enable support for holo gui's on
 * blocks (usually tile entities)
 */
public interface IHoloGuiProvider {

    @Nullable
    IGuiTile getTile(World world, BlockPos pos);
}
