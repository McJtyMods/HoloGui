package mcjty.hologui.api;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Register this interface with IHoloGuiHandler to enable support for holo gui's on
 * blocks (usually tile entities)
 */
public interface IHoloGuiProvider {

    @Nullable
    IGuiTile getTile(Level world, BlockPos pos);
}
