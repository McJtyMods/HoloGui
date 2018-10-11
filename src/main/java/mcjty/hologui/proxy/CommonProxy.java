package mcjty.hologui.proxy;

import mcjty.hologui.ForgeEventHandlers;
import mcjty.hologui.ModEntities;
import mcjty.hologui.config.HoloGuiConfiguration;
import mcjty.lib.base.GeneralConfig;
import mcjty.lib.proxy.AbstractCommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Created by jorrit on 16.12.16.
 */
public abstract class CommonProxy extends AbstractCommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

//        SimpleNetworkWrapper network = PacketHandler.registerMessages(Ariente.MODID, "ariente");
        GeneralConfig.preInit(e);
//        ArienteMessages.registerNetworkMessages(network);

        mainConfig = new Configuration(new File(modConfigDir.getPath(), "hologui.cfg"));
        HoloGuiConfiguration.init(mainConfig);

        ModEntities.init();
    }

    public Configuration getConfig() {
        return mainConfig;
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
//        ConfigSetup.postInit();
//        ModBlocks.postInit();
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
    }

    public abstract boolean isJumpKeyDown();
}