package mcjty.hologui.api;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * The GUI Registry can be used to register gui's that are not tied to a specific
 * block (i.e. don't use IGuiTile). For gui's that are created using an IGuiTile
 * this is not required
 */
public interface IGuiRegistry {

    void registerGui(String id, Function<Player, IGuiComponent> factory);

    @Nullable
    IGuiComponent createGui(String id, Player player);
}
