package mcjty.hologui.setup;

import mcjty.hologui.HoloGui;
import mcjty.hologui.commands.ConfigurationGui;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultModSetup;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init() {
        super.init();


        HoloGui.guiHandler.getGuiRegistry().registerGui(ConfigurationGui.GUI_CONFIGURATION, ConfigurationGui::create);
    }

    @Override
    protected void setupModCompat() {
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }
}
