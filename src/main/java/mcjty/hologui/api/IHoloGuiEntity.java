package mcjty.hologui.api;

import net.minecraft.entity.Entity;

public interface IHoloGuiEntity {

    Entity getEntity();

    /**
     * Set the maximum timeout that will be used as the timeout whenever
     * the player looks at the gui (in case CloseStrategy.TIMEOUT_RESET is active)
     */
    void setMaxTimeout(int maxTimeout);

    /**
     * Set the timeout before this gui closes (in ticks)
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

    void setScale(float scale);

    String getGuiId();

    String getTag();

    void switchGui(String guiId);
}

