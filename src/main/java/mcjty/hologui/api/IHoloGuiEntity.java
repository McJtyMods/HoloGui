package mcjty.hologui.api;

import net.minecraft.entity.Entity;

public interface IHoloGuiEntity {

    /**
     * Get the actual entity representing this hologui
     */
    Entity getEntity();

    /**
     * Set the maximum timeout that will be used as the timeout whenever
     * the player looks at the gui (in case CloseStrategy.TIMEOUT_RESET is active)
     */
    void setMaxTimeout(int maxTimeout);

    /**
     * Set the timeout before this gui closes (in ticks)
     * This is only used in case CloseStrategy.TIMEOUT is active
     */
    void setTimeout(int timeout);

    /**
     * Set the strategy for closing this hologui. This is a combination of flags from CloseStrategy. Not all combination
     * make sense though. The default is TIMEOUT_IDLE + RIGHTCLICK
     * @param strategy
     */
    void setCloseStrategy(int strategy);
    int getCloseStrategy();

    /**
     * Test if this gui has a specific strategy
     */
    boolean hasCloseStrategy(int strategy);

    float getScale();

    /**
     * Scale to render this hologui at. Default is 1.0f
     */
    void setScale(float scale);

    String getGuiId();

    String getTag();

    /**
     * Switch to another registered gui (using IGuiRegistry)
     */
    void switchGui(String guiId);

    /**
     * Switch the gui to another tag. Mainly used in combination with IGuiTile
     */
    void switchTag(String tag);
}

