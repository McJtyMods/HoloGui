package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IColorPicker;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class HoloColorPicker extends AbstractHoloComponent<IColorPicker> implements IColorPicker {

    private Supplier<Integer> color = () -> 0x77eeff;
    private ColorEvent selected;
    private float hue = .53f;

    HoloColorPicker(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IColorPicker color(Supplier<Integer> color) {
        this.color = color;
        int rgb = color.get();
        float red = (rgb >> 16 & 255) / 255f;
        float green = (rgb >> 8 & 255) / 255f;
        float blue = (rgb & 255) / 255f;
        float max = Math.max(red, Math.max(green, blue));
        float min = Math.min(red, Math.min(green, blue));
        float delta = max - min;
        if (delta > .0001f) {
            hue = (max == red ? (green - blue) / delta + (green < blue ? 6 : 0)
                    : max == green ? (blue - red) / delta + 2 : (red - green) / delta + 4) / 6;
        }
        return this;
    }

    @Override
    public IColorPicker selected(ColorEvent event) {
        selected = event;
        return this;
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (cursorY >= y + h - .7) {
            hue = (float) Mth.clamp((cursorX - x) / w, 0, .999);
        } else if (selected != null) {
            float saturation = (float) Mth.clamp((cursorX - x) / w, 0, 1);
            float value = 1 - (float) Mth.clamp((cursorY - y) / (h - 1), 0, 1);
            selected.selected(player, entity, Mth.hsvToRgb(hue, saturation, value));
        }
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (cursorY >= y + h - .7) {
            hue = (float) Mth.clamp((cursorX - x) / w, 0, .999);
        }
    }

    @Override
    public void render(PoseStack poses, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 24; col++) {
                int rgb = Mth.hsvToRgb(hue, col / 23f, 1 - row / 15f);
                HoloGuiRenderTools.renderFill(poses, buffer, x + col * w / 24, y + row * (h - 1) / 16,
                        w / 24 + .01, (h - 1) / 16 + .01, rgb);
            }
        }
        for (int col = 0; col < 48; col++) {
            HoloGuiRenderTools.renderFill(poses, buffer, x + col * w / 48, y + h - .7,
                    w / 48 + .01, .65, Mth.hsvToRgb(col / 48f, 1, 1));
        }
        HoloGuiRenderTools.renderBorder(poses, buffer, x + hue * w - .03, y + h - .72, .06, .7, 255, 255, 255, 255);
        HoloGuiRenderTools.renderFill(poses, buffer, x, y + h + .2, .6, .35, color.get());
    }
}
