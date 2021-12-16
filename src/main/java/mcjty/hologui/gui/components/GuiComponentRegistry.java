package mcjty.hologui.gui.components;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.*;
import mcjty.hologui.api.components.*;
import mcjty.hologui.gui.ColorFromStyle;
import net.minecraft.resources.ResourceLocation;

public class GuiComponentRegistry implements IGuiComponentRegistry {

    private ResourceLocation STANDARD = new ResourceLocation(HoloGui.MODID, "textures/gui/guielements.png");


    @Override
    public IIconButton iconButton(double x, double y, double w, double h) {
        return new HoloButton(x, y, w, h);
    }

    @Override
    public IIcon icon(double x, double y, double w, double h) {
        return new HoloIcon(x, y, w, h);
    }

    @Override
    public IIconChoice iconChoice(double x, double y, double w, double h) {
        return new HoloIconChoice(x, y, w, h);
    }

    @Override
    public IIconToggle iconToggle(double x, double y, double w, double h) {
        return new HoloToggleIcon(x, y, w, h);
    }

    @Override
    public INumber number(double x, double y, double w, double h) {
        return new HoloNumber(x, y, w, h);
    }

    @Override
    public IPanel panel(double x, double y, double w, double h) {
        return new HoloPanel(x, y, w, h);
    }

    @Override
    public IStackIcon stackIcon(double x, double y, double w, double h) {
        return new HoloStackIcon(x, y, w, h);
    }

    @Override
    public IStackToggle stackToggle(double x, double y, double w, double h) {
        return new HoloStackToggle(x, y, w, h);
    }

    @Override
    public IText text(double x, double y, double w, double h) {
        return new HoloText(x, y, w, h);
    }

    @Override
    public ITextChoice textChoice(double x, double y, double w, double h) {
        return new HoloTextChoice(x, y, w, h);
    }

    @Override
    public IButton button(double x, double y, double w, double h) {
        return new HoloTextButton(x, y, w, h);
    }

    @Override
    public ISlots slots(double x, double y, double w, double h) {
        return new HoloSlots(x, y, w, h);
    }

    @Override
    public IPlayerSlots playerSlots(double x, double y, double w, double h) {
        return new HoloPlayerSlots(x, y, w, h);
    }

    @Override
    public IPlayerInventory playerInventory(double y) {
        return new HoloPlayerInventory(y);
    }

    @Override
    public IImage image(Icons icon) {
        return new IImage() {
            @Override
            public ResourceLocation getImage() {
                return STANDARD;
            }

            @Override
            public int getWidth() {
                return 256;
            }

            @Override
            public int getHeight() {
                return 256;
            }

            @Override
            public int getU() {
                return icon.getU();
            }

            @Override
            public int getV() {
                return icon.getV();
            }
        };
    }

    @Override
    public IImage image(int u, int v) {
        return new IImage() {
            @Override
            public ResourceLocation getImage() {
                return STANDARD;
            }

            @Override
            public int getWidth() {
                return 256;
            }

            @Override
            public int getHeight() {
                return 256;
            }

            @Override
            public int getU() {
                return u;
            }

            @Override
            public int getV() {
                return v;
            }
        };
    }

    @Override
    public IImage image(ResourceLocation image, int w, int h) {
        return new IImage() {
            @Override
            public ResourceLocation getImage() {
                return image;
            }

            @Override
            public int getWidth() {
                return w;
            }

            @Override
            public int getHeight() {
                return h;
            }

            @Override
            public int getU() {
                return 0;
            }

            @Override
            public int getV() {
                return 0;
            }
        };
    }

    @Override
    public IColor color(int color) {
        return () -> color;
    }

    @Override
    public IColor color(StyledColor color) {
        return new ColorFromStyle(color);
    }
}
