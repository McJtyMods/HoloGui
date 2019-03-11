package mcjty.hologui.proxy;

import mcjty.hologui.ForgeEventHandlers;
import mcjty.hologui.ModEntities;
import mcjty.hologui.config.ConfigSetup;
import mcjty.lib.compat.MainCompatHandler;
import mcjty.lib.setup.DefaultCommonSetup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by jorrit on 16.12.16.
 */
public class CommonSetup extends DefaultCommonSetup {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

//        HoloMessages.registerNetworkMessages("hologui");

        ConfigSetup.init();
        ModEntities.init();
    }

    @Override
    protected void setupModCompat() {
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    @Override
    public void createTabs() {
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
    }
}
