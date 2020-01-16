package mcjty.hologui.setup;

import mcjty.hologui.ForgeEventHandlers;
import mcjty.hologui.HoloGui;
import mcjty.hologui.ModEntities;
import mcjty.hologui.commands.ConfigurationGui;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup extends DefaultModSetup {

    @Override
    public void init(FMLCommonSetupEvent e) {
        super.init(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        //        HoloMessages.registerNetworkMessages("hologui");

        ModEntities.init();

        HoloGui.guiHandler.getGuiRegistry().registerGui(ConfigurationGui.GUI_CONFIGURATION, ConfigurationGui::create);
    }

    @Override
    protected void setupModCompat() {
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    // @todo 1.14
//    @Override
//    protected void setupConfig() {
//        ConfigSetup.init();
//    }

//    @Override
//    public void postInit(FMLPostInitializationEvent e) {
//        ConfigSetup.postInit();
//    }
}
