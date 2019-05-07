package mcjty.hologui.config;

public enum GuiStyle {
    NONE(""),
    TRANSP_BLUE_WHITE_SOFT("TrBlueWSoft"),
    TRANSP_BLUE("TrBlue"),
    TRANSP_BLUE_WHITE_SHARP("TrBlueWSharp"),
    TRANSP_BLUE_BLACK_SHARP("TrBlueBSharp"),
    TRANSP_BLUE_BLACK_SOFT("TrBlueBSoft"),
    TRANSP_GRAY_BLACK_SHARP("TrGrayBSharp"),
    TRANSP_GRAY_WHITE_SHARP("TrGrayWSharp"),
    TRANSP_GRAY_BLACK_SOFT("TrGrayBSoft"),
    OPAQUE_BLUE_WHITE_SOFT("OpBlueWSoft"),
    OPAQUE_BLUE("OpBlue"),
    OPAQUE_BLUE_WHITE_SHARP("OpBlueWSharp"),
    OPAQUE_BLUE_BLACK_SHARP("OpBlueBSharp"),
    OPAQUE_BLUE_BLACK_SOFT("OpBlueBSoft"),
    OPAQUE_GRAY_BLACK_SHARP("OpGrayBSharp"),
    OPAQUE_GRAY_WHITE_SHARP("OpGrayWSharp"),
    OPAQUE_GRAY_BLACK_SOFT("OpGrayBSoft");

    private final String name;

    GuiStyle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
