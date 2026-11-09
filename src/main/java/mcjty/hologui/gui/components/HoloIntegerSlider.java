package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.BiFunction;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IIntegerSlider;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.SliderInput;
import mcjty.lib.client.RenderBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class HoloIntegerSlider extends AbstractHoloComponent<IIntegerSlider> implements IIntegerSlider {

    private String label = "";
    private String suffix = "";
    private int min;
    private int max = 100;
    private int step = 1;
    private boolean logarithmic;
    private BiFunction<Player, IHoloGuiEntity, Integer> getter = (p, e) -> 0;
    private ChangeEvent changed;

    HoloIntegerSlider(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IIntegerSlider label(String label) {
        this.label = label;
        return this;
    }

    @Override
    public IIntegerSlider suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public IIntegerSlider range(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Invalid slider range");
        }
        this.min = min;
        this.max = max;
        return this;
    }

    @Override
    public IIntegerSlider step(int step) {
        this.step = Math.max(1, step);
        return this;
    }

    @Override
    public IIntegerSlider logarithmic(boolean logarithmic) {
        this.logarithmic = logarithmic;
        return this;
    }

    @Override
    public IIntegerSlider getter(BiFunction<Player, IHoloGuiEntity, Integer> getter) {
        this.getter = getter;
        return this;
    }

    @Override
    public IIntegerSlider changed(ChangeEvent event) {
        changed = event;
        return this;
    }

    @Override
    public boolean isTrack(double cx, double cy) {
        return cx >= x + .85 && cx <= x + w - .85 && cy >= y && cy <= y + h;
    }

    private void set(Player player, IHoloGuiEntity entity, long value) {
        int bounded = (int) Math.max((long) min, Math.min((long) max, value));
        if (changed != null && bounded != getter.apply(player, entity)) {
            changed.changed(player, entity, bounded);
        }
    }

    @Override
    public void drag(Player player, IHoloGuiEntity entity, double cx, double cy) {
        double fraction = Mth.clamp((cx - x - 1) / Math.max(.1, w - 2), 0, 1);
        double span = (double) max - min;
        double offset = logarithmic ? Math.expm1(fraction * Math.log1p(span)) : fraction * span;
        long steps = Math.round(offset / step);
        set(player, entity, min + steps * step);
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cx, double cy) {
        if (cx < x + .85) {
            set(player, entity, (long) getter.apply(player, entity) - step);
        } else if (cx > x + w - .85) {
            set(player, entity, (long) getter.apply(player, entity) + step);
        } else {
            drag(player, entity, cx, cy);
            if (getName() != null && entity instanceof HoloGuiEntity holo) {
                holo.beginSlider(player, getName());
            }
        }
    }

    @Override

    public void hitClient(Player player, IHoloGuiEntity entity, double cx, double cy) {
        if (getName() != null && isTrack(cx, cy) && entity instanceof HoloGuiEntity holo) {
            SliderInput.begin(holo, this);
        }
    }

    @Override
    public void render(PoseStack poses, RenderBufferSource buffer, Player player, IHoloGuiEntity holo, double cx, double cy) {
        int value = Mth.clamp(getter.apply(player, holo), min, max);
        boolean hovered = isInside(cx, cy);
        double arrowSize = Math.min(.5, h - .1);
        double centerY = y + h - .26;
        HoloGuiRenderTools.renderControl(poses, buffer, x + .1, centerY - arrowSize / 2, arrowSize, arrowSize, hovered && cx < x + .85);
        HoloGuiRenderTools.renderControl(poses, buffer, x + w - .1 - arrowSize, centerY - arrowSize / 2,
                arrowSize, arrowSize, hovered && cx > x + w - .85);
        HoloGuiRenderTools.renderChevron(poses, buffer, x + .1 + arrowSize / 2, centerY, false, 0xc1f5ff);
        HoloGuiRenderTools.renderChevron(poses, buffer, x + w - .1 - arrowSize / 2, centerY, true, 0xc1f5ff);
        HoloGuiRenderTools.renderControlText(poses, buffer, x + 1, y, w - 2, .5,
                label + ": " + value + suffix, 0xd9f9ff, .45f);
        double fraction = max == min ? 0 : logarithmic ? Math.log1p(value - (double) min) / Math.log1p((double) max - min)
                : (value - (double) min) / ((double) max - min);
        poses.pushPose();
        poses.translate(0, 0, -.002);
        HoloGuiRenderTools.renderFill(poses, buffer, x + 1, y + h - .3, w - 2, .08, 0x42677d);
        poses.popPose();
        HoloGuiRenderTools.renderFill(poses, buffer, x + 1, y + h - .3, (w - 2) * fraction, .08, 0x63dae9);
        poses.pushPose();
        poses.translate(0, 0, .002);
        HoloGuiRenderTools.renderFill(poses, buffer, x + .9 + (w - 2) * fraction, y + h - .46, .2, .4,
                hovered ? 0xffffff : 0xa6f6ff);
        poses.popPose();
    }
}
