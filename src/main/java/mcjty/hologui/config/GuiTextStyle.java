package mcjty.hologui.config;

public enum GuiTextStyle {
    DEFAULT(Constants.DEFAULT_TITLE,
            Constants.DEFAULT_ERROR,
            Constants.DEFAULT_WARNING,
            Constants.DEFAULT_LABEL,
            Constants.DEFAULT_INFO,
            Constants.DEFAULT_BUTTON,
            Constants.DEFAULT_BUTTON_HOVER,
            Constants.DEFAULT_BUTTON_BORDER,
            Constants.DEFAULT_BORDER_COLOR),
    BLACK(Constants.BLACK_TITLE,
            Constants.BLACK_ERROR,
            Constants.BLACK_WARNING,
            Constants.BLACK_LABEL,
            Constants.BLACK_INFO,
            Constants.BLACK_BUTTON,
            Constants.BLACK_BUTTON_HOVER,
            Constants.BLACK_BUTTON_BORDER,
            Constants.BLACK_BORDER_COLOR),
    WHITE(Constants.WHITE_TITLE,
            Constants.WHITE_ERROR,
            Constants.WHITE_WARNING,
            Constants.WARNING_LABEL,
            Constants.WARNING_INFO,
            Constants.WHITE_BUTTON,
            Constants.WHITE_BUTTON_HOVER,
            Constants.WHITE_BUTTON_BORDER,
            Constants.WHITE_BORDER_COLOR);

    private final int titleColor;
    private final int errorColor;
    private final int warningColor;
    private final int labelColor;
    private final int infoColor;
    private final int buttonColor;
    private final int buttonHoverColor;
    private final int buttonBorderColor;
    private final int borderColor;

    GuiTextStyle(int titleColor, int errorColor, int warningColor, int labelColor, int infoColor, int buttonColor, int buttonHoverColor, int buttonBorderColor, int borderColor) {
        this.titleColor = titleColor;
        this.errorColor = errorColor;
        this.warningColor = warningColor;
        this.labelColor = labelColor;
        this.infoColor = infoColor;
        this.buttonColor = buttonColor;
        this.buttonHoverColor = buttonHoverColor;
        this.buttonBorderColor = buttonBorderColor;
        this.borderColor = borderColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public int getWarningColor() {
        return warningColor;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public int getInfoColor() {
        return infoColor;
    }

    public int getButtonColor() {
        return buttonColor;
    }

    public int getButtonHoverColor() {
        return buttonHoverColor;
    }

    public int getButtonBorderColor() {
        return buttonBorderColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    private static class Constants {
        private static final int DEFAULT_TITLE = 0xffaaccff;
        public static final int DEFAULT_ERROR = 0xffff0000;
        public static final int DEFAULT_WARNING = 0xffdd9900;
        public static final int DEFAULT_LABEL = 0xff888888;
        public static final int DEFAULT_INFO = 0xfffffffe;        // Not ffffffff because that's -1
        public static final int BLACK_TITLE = 0xff000000;
        public static final int BLACK_ERROR = 0xffff0000;
        public static final int BLACK_WARNING = 0xffdd9900;
        public static final int BLACK_LABEL = 0xff555555;
        public static final int BLACK_INFO = 0xff0000ff;
        public static final int WHITE_TITLE = 0xfffffffe;        // Not ffffffff because that's -1
        public static final int WHITE_ERROR = 0xffff0000;
        public static final int WHITE_WARNING = 0xffdd9900;
        public static final int WARNING_LABEL = 0xffdddddd;
        public static final int WARNING_INFO = 0xfffffffe;        // Not ffffffff because that's -1
        public static final int DEFAULT_BUTTON = 0xff88ffff;
        public static final int BLACK_BUTTON = 0xff88ffff;
        public static final int WHITE_BUTTON = 0xff88ffff;
        public static final int DEFAULT_BUTTON_HOVER = 0xfffffffe;
        public static final int BLACK_BUTTON_HOVER = 0xfffffffe;
        public static final int WHITE_BUTTON_HOVER = 0xfffffffe;        // Not ffffffff because that's -1
        public static final int DEFAULT_BUTTON_BORDER = -1;
        public static final int BLACK_BUTTON_BORDER = -1;
        public static final int WHITE_BUTTON_BORDER = -1;
        public static final int DEFAULT_BORDER_COLOR = 0xff80c8ff;
        public static final int BLACK_BORDER_COLOR = 0xff80c8ff;
        public static final int WHITE_BORDER_COLOR = 0xff80c8ff;
    }
}
