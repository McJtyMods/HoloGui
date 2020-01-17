package mcjty.hologui;

import mcjty.hologui.commands.ModCommands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.register(event.getCommandDispatcher());
    }

    // @todo 1.14
//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
//        ConfigSetup.init(ConfigSetup.mainConfig);
//
//        if (ConfigSetup.mainConfig.hasChanged()) {
//            ConfigSetup.mainConfig.save();
//        }
//    }
}
