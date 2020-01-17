package mcjty.hologui.gui;

import mcjty.hologui.api.StyledColor;
import mcjty.hologui.config.Config;
import mcjty.hologui.config.GuiTextStyle;

public class TextStyleSupport {

    public static int resolveColor(StyledColor styledColor) {
        GuiTextStyle textStyle = Config.GUI_TEXT_STYLE.get();
        switch (styledColor) {
            case DISABLED:
                return -1;
            case TITLE:
                return textStyle.getTitleColor();
            case ERROR:
                return textStyle.getErrorColor();
            case WARNING:
                return textStyle.getWarningColor();
            case LABEL:
                return textStyle.getLabelColor();
            case INFORMATION:
                return textStyle.getInfoColor();
            case BUTTON:
                return textStyle.getButtonColor();
            case BUTTON_HOVER:
                return textStyle.getButtonHoverColor();
            case BUTTON_BORDER:
                return textStyle.getButtonBorderColor();
            case BORDER:
                return textStyle.getBorderColor();
        }
        return 0;
    }
}
