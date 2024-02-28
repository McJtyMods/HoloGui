package mcjty.hologui.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.neoforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class Config {

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

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static ForgeConfigSpec CLIENT_CONFIG;

    public static void setGuiStyle(GuiStyle style) {
        GUI_STYLE.set(style);
    }

    public static void setGuiTextStyle(GuiTextStyle style) {
        GUI_TEXT_STYLE.set(style);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
