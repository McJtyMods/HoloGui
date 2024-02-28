package mcjty.hologui.setup;

import mcjty.hologui.ForgeEventHandlers;
import mcjty.hologui.HoloGui;
import mcjty.hologui.commands.ConfigurationGui;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultModSetup;
import net.neoforged.neoforge.common.MinecraftForge;
import net.neoforged.neoforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        //        HoloMessages.registerNetworkMessages("hologui");

        HoloGui.guiHandler.getGuiRegistry().registerGui(ConfigurationGui.GUI_CONFIGURATION, ConfigurationGui::create);
    }

    @Override
    protected void setupModCompat() {
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }
}
