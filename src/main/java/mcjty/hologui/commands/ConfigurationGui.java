package mcjty.hologui.commands;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IGuiComponentRegistry;
import mcjty.hologui.api.StyledColor;
import mcjty.hologui.api.components.ITextChoice;
import mcjty.hologui.config.ConfigSetup;
import mcjty.hologui.config.GuiStyle;
import mcjty.hologui.config.GuiTextStyle;
import net.minecraft.entity.player.EntityPlayer;

public class ConfigurationGui {

    public static final String GUI_CONFIGURATION = "hologui.configuration";

    public static IGuiComponent<?> create(EntityPlayer player) {
        IGuiComponentRegistry registry = HoloGui.guiHandler.getComponentRegistry();
        ITextChoice styleChoices = createStyleChoices(registry);
        ITextChoice textStyleChoices = createTextStyleChoices(registry);
        return registry.panel(0, 0, 8, 8)
                .add(registry.text(0, 0, 7, 1).color(registry.color(StyledColor.TITLE)).text("Configuration"))
                .add(styleChoices)
                .add(textStyleChoices);
    }

    private static ITextChoice createStyleChoices(IGuiComponentRegistry registry) {
        ITextChoice styleChoices = registry
                .textChoice(0, 1, 4, 1)
                .getter(plr -> {
                    GuiStyle guiStyle = ConfigSetup.GUI_STYLE.get();
                    return guiStyle.ordinal()-1;
                })
                .hitEventClient((component, plr, entity, x, y) -> {
                    GuiStyle guiStyle = ConfigSetup.GUI_STYLE.get();
                    int ordinal = guiStyle.ordinal();
                    ordinal++;
                    if (ordinal >= GuiStyle.values().length) {
                        ordinal = 1;
                    }
                    ConfigSetup.setGuiStyle(GuiStyle.values()[ordinal]);
                });
        for (GuiStyle value : GuiStyle.values()) {
            if (value != GuiStyle.NONE) {
                styleChoices.addText(value.name());
            }
        }
        return styleChoices;
    }

    private static ITextChoice createTextStyleChoices(IGuiComponentRegistry registry) {
        ITextChoice styleChoices = registry
                .textChoice(0, 1, 4, 1)
                .getter(plr -> {
                    GuiTextStyle guiStyle = ConfigSetup.GUI_TEXT_STYLE.get();
                    return guiStyle.ordinal();
                })
                .hitEventClient((component, plr, entity, x, y) -> {
                    GuiTextStyle guiStyle = ConfigSetup.GUI_TEXT_STYLE.get();
                    int ordinal = guiStyle.ordinal();
                    ordinal++;
                    if (ordinal >= GuiStyle.values().length) {
                        ordinal = 1;
                    }
                    ConfigSetup.setGuiTextStyle(GuiTextStyle.values()[ordinal]);
                });
        for (GuiTextStyle value : GuiTextStyle.values()) {
            styleChoices.addText(value.name());
        }
        return styleChoices;
    }

}
