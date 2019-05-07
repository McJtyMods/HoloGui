package mcjty.hologui.gui;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.StyledColor;

public class ColorFromStyle implements IColor {

    private final StyledColor styledColor;

    public ColorFromStyle(StyledColor styledColor) {
        this.styledColor = styledColor;
    }

    @Override
    public int getColor() {
        return TextStyleSupport.resolveColor(styledColor);
    }
}
