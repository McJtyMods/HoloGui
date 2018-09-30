package mcjty.hologui.api;

/**
 * This represents the gui for a block in the world. You can return instances of these
 * in your IHoloGuiProvider implementation.
 */
public interface IGuiTile {

    /**
     * Create the actual gui. The 'tag' represents different modes for the gui. This
     * is useful if you have support for more types of guis on the same block
     */
    IGuiComponent createGui(String tag, IGuiComponentRegistry registry);

    /**
     * This is regularly called server-side to give this gui tile a chance to
     * sync data to the client while a holo gui is open
     */
    void syncToClient();
}
