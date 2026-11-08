package mcjty.hologui.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final String CATEGORY_GUI = "gui";

    public static ModConfigSpec.ConfigValue<GuiStyle> GUI_STYLE;
    public static ModConfigSpec.ConfigValue<GuiTextStyle> GUI_TEXT_STYLE;

    static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    static {
        CLIENT_BUILDER.comment("GUI settings").push(CATEGORY_GUI);

        GUI_STYLE = CLIENT_BUILDER
                .comment("The gui style")
                .defineEnum("guiStyle", GuiStyle.TRANSP_BLUE_WHITE_SHARP, GuiStyle.values());
        GUI_TEXT_STYLE = CLIENT_BUILDER
                .comment("The gui text style")
                .defineEnum("guiTextStyle", GuiTextStyle.DEFAULT, GuiTextStyle.values());

        CLIENT_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static ModConfigSpec CLIENT_CONFIG;

    public static void setGuiStyle(GuiStyle style) {
        GUI_STYLE.set(style);
    }

    public static void setGuiTextStyle(GuiTextStyle style) {
        GUI_TEXT_STYLE.set(style);
    }

}
