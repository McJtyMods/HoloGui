package mcjty.hologui.config;

import mcjty.lib.thirteen.ConfigSpec;
import net.minecraftforge.common.config.Configuration;

public class GuiConfiguration {

    public static final String CATEGORY_GUI = "gui";

    public static ConfigSpec.ConfigValue<GuiStyle> GUI_STYLE;

    private static final ConfigSpec.Builder CLIENT_BUILDER = new ConfigSpec.Builder();

    static {
        CLIENT_BUILDER.comment("GUI settings").push(CATEGORY_GUI);

        GUI_STYLE = CLIENT_BUILDER
                .comment("The gui style")
                .defineEnum("guiStyle", GuiStyle.TRANSP_BLUE_WHITE_SHARP, GuiStyle.values());

        CLIENT_BUILDER.pop();
    }

    public static ConfigSpec CLIENT_CONFIG;

    public static void init(Configuration cfg) {
        CLIENT_CONFIG = CLIENT_BUILDER.build(cfg);
    }
}
