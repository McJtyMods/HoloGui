package mcjty.hologui.config;

import mcjty.hologui.HoloGui;
import mcjty.lib.thirteen.ConfigSpec;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigSetup {

    public static final String CATEGORY_GUI = "gui";

    public static ConfigSpec.ConfigValue<GuiStyle> GUI_STYLE;
    public static ConfigSpec.ConfigValue<GuiTextStyle> GUI_TEXT_STYLE;

    static final ConfigSpec.Builder CLIENT_BUILDER = new ConfigSpec.Builder();

    static {
        CLIENT_BUILDER.comment("GUI settings").push(CATEGORY_GUI);

        GUI_STYLE = CLIENT_BUILDER
                .comment("The gui style")
                .defineEnum("guiStyle", GuiStyle.TRANSP_BLUE_WHITE_SHARP, GuiStyle.values());
        GUI_TEXT_STYLE = CLIENT_BUILDER
                .comment("The gui text style")
                .defineEnum("guiTextStyle", GuiTextStyle.DEFAULT, GuiTextStyle.values());

        CLIENT_BUILDER.pop();
    }

    private static ConfigSpec CLIENT_CONFIG;

    public static Configuration mainConfig;

    public static void init() {
        mainConfig = new Configuration(new File(HoloGui.setup.getModConfigDir().getPath(), "hologui.cfg"));
        init(mainConfig);
    }

    public static void postInit() {
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
    }

    public static void init(Configuration cfg) {
        CLIENT_CONFIG = CLIENT_BUILDER.build(cfg);
    }
}
