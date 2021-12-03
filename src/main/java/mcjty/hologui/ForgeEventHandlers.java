package mcjty.hologui;

import mcjty.hologui.commands.ModCommands;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
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
