package mcjty.hologui.api;

public interface IGuiTile {

    IGuiComponent createGui(String tag, IGuiComponentRegistry registry);

    void syncToClient();
}
