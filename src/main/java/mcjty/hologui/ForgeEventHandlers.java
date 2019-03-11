package mcjty.hologui;

import mcjty.hologui.config.ConfigSetup;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        ConfigSetup.init(ConfigSetup.mainConfig);

        if (ConfigSetup.mainConfig.hasChanged()) {
            ConfigSetup.mainConfig.save();
        }
    }
}
