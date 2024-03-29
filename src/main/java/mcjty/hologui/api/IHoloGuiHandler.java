package mcjty.hologui.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IHoloGuiHandler {

    /**
     * Register a provider to find out if a block needs a gui (IGuiTile)
     * @param provider
     */
    void registerProvider(IHoloGuiProvider provider);

    /**
     * Using all the registered providers, find out if a given block needs a gui (IGuiTile)
     */
    @Nullable
    IGuiTile getGuiTile(Level world, BlockPos pos);

    /**
     * Using the IHoloGuiProvider implementations that you registered this will
     * try to open the holo gui for a given block in the world. You would typically
     * call this function from your onBlockActivated() implementation.
     * On the server this returns false if no holo gui was found. On the
     * client this will always return true.
     */
    boolean openHoloGui(Level world, BlockPos pos, Player player);

    /**
     * Open a holo gui for a given block (using the IGuiTile gotten from
     * your IHoloGuiProvider) but with a specific tag and distance from the player. On the client
     * this will always return null (and just play a sound)
     */
    @Nullable
    IHoloGuiEntity openHoloGuiEntity(Level world, BlockPos pos, Player player, String tag, double distance);

    /**
     * Open a specific gui (registered with the IGuiRegistry). This is not tied
     * to a specific block in the world.
     */
    IHoloGuiEntity openHoloGui(Player player, String guiId, double distance);

    /**
     * Open a holo gui relative to another entity (the holo gui will be
     * 'riding' the other entity). Note that a hologui that is a child of another
     * entity will not render itself. It is up to the renderer of the parent to
     * make sure the hologui renders.
     */
    IHoloGuiEntity openHoloGuiRelative(Entity parent, Vec3 offset, String guiId);

    // @todo, generalize, move into Ariente?
    IGuiComponent<?> createNoAccessPanel();

    // Client side only. Use this to render your holo gui from some other renderer
    void render(IHoloGuiEntity entity, double x, double y, double z, float entityYaw);

    // This can be used in case you want to find all nearby holo gui entities in the world
    Class<? extends Entity> getHoloEntityClass();

    IGuiRegistry getGuiRegistry();

    IGuiComponentRegistry getComponentRegistry();
}
