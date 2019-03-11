package mcjty.hologui.config;

import mcjty.hologui.HoloGui;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class HoloGuiConfiguration {

    public static Configuration mainConfig;

    public static void init() {
        mainConfig = new Configuration(new File(HoloGui.setup.getModConfigDir().getPath(), "hologui.cfg"));
        GuiConfiguration.init(mainConfig);
    }

}
