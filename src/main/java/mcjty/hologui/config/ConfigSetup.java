package mcjty.hologui.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSetup {

    public static final String CATEGORY_GUI = "gui";

    public static ForgeConfigSpec.ConfigValue<GuiStyle> GUI_STYLE;
    public static ForgeConfigSpec.ConfigValue<GuiTextStyle> GUI_TEXT_STYLE;

    static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

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

    private static ForgeConfigSpec CLIENT_CONFIG;

    // @todo 1.14
//    public static Configuration mainConfig;

    public static void init() {
//        mainConfig = new Configuration(new File(HoloGui.setup.getModConfigDir().getPath(), "hologui.cfg"));
//        init(mainConfig);
    }

    public static void postInit() {
//        if (mainConfig.hasChanged()) {
//            mainConfig.save();
//        }
    }

    public static void setGuiStyle(GuiStyle style) {
        GUI_STYLE.set(style);
//        Configuration cfg = mainConfig;
//        cfg.get(CATEGORY_GUI, "guiStyle", GuiStyle.TRANSP_BLUE_WHITE_SHARP.name()).set(style.name());
//        cfg.save();
    }

    public static void setGuiTextStyle(GuiTextStyle style) {
        GUI_TEXT_STYLE.set(style);
//        Configuration cfg = mainConfig;
//        cfg.get(CATEGORY_GUI, "guiTextStyle", GuiTextStyle.DEFAULT.name()).set(style.name());
//        cfg.save();
    }

//    public static void init(Configuration cfg) {
//        CLIENT_CONFIG = CLIENT_BUILDER.build(cfg);
//    }
}
