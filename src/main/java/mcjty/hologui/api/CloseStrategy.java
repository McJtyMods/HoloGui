package mcjty.hologui.api;

/**
 * This interface holds constants. You're not supposed to implement this interface.
 * The constants in this interface can be added together
 */
public interface CloseStrategy {
    int NEVER = 0;              // This hologui doesn't close automatically
    int TIMEOUT_RESET = 1;      // Default strategy, reset timeout when player looks
    int TIMEOUT = 2;            // Default strategy, close after a timeout
    int RIGHTCLICK = 4;         // Default strategy, Close when the player right clicks
}
