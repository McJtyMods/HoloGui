package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Locale;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/** Small geometric symbols stay sharp at every hologram scale and viewing angle. */
class HoloWindowButton extends AbstractHoloComponent<HoloWindowButton> {

    // The visible frame is slightly inset from the background quad's outer edge.
    static final double RIGHT = 9.0;
    static final double WIDTH = .56;
    private static final double TOP = -.6;
    private static final double HEIGHT = .49;
    private static final int NORMAL = 0x9edce9;
    private static final int HOVER = 0xe2fbff;
    private static final int PINNED = 0x70ffcf;

    enum Kind { CLOSE, HELP, PIN }

    private final Kind kind;
    private final IEvent help;

    HoloWindowButton(double x, Kind kind, IEvent help) {
        super(x, TOP, WIDTH, HEIGHT);
        this.kind = kind;
        this.help = help;
        name("hologui." + kind.name().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean isInside(double x, double y) {
        // Adjacent rectangles share a border, but never handle the same click.
        return x >= this.x && x < this.x + w && y >= this.y && y < this.y + h;
    }

    @Override
    public void render(PoseStack poses, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        boolean hover = isInside(cursorX, cursorY);
        boolean pinned = kind == Kind.PIN && holo.isPinned();
        HoloGuiRenderTools.renderControl(poses, buffer, x, y, w, h, hover || pinned);
        int color = pinned ? PINNED : hover ? kind == Kind.CLOSE ? 0xffa8a8 : HOVER : NORMAL;
        poses.pushPose();
        poses.translate(0, 0, .002);
        if (kind == Kind.CLOSE) {
            line(poses, buffer, .31, .25, .69, .75, color);
            line(poses, buffer, .31, .75, .69, .25, color);
        } else if (kind == Kind.HELP) {
            line(poses, buffer, .34, .30, .42, .21, color);
            line(poses, buffer, .42, .21, .60, .21, color);
            line(poses, buffer, .60, .21, .68, .30, color);
            line(poses, buffer, .68, .30, .68, .42, color);
            line(poses, buffer, .68, .42, .50, .55, color);
            line(poses, buffer, .50, .55, .50, .61, color);
            line(poses, buffer, .50, .75, .50, .78, color);
        } else {
            // A tilted pushpin straightens and gains a status light while pinned.
            poses.pushPose();
            if (!pinned) {
                double cx = x + w / 2;
                double cy = y + h / 2;
                poses.translate(cx * .1 - .42, .42 - cy * .1, 0);
                poses.mulPose(Axis.ZP.rotationDegrees(-30));
                poses.translate(-(cx * .1 - .42), -(.42 - cy * .1), 0);
            }
            line(poses, buffer, .32, .22, .68, .22, color);
            line(poses, buffer, .38, .22, .38, .45, color);
            line(poses, buffer, .62, .22, .62, .45, color);
            line(poses, buffer, .38, .45, .27, .56, color);
            line(poses, buffer, .62, .45, .73, .56, color);
            line(poses, buffer, .27, .56, .73, .56, color);
            line(poses, buffer, .50, .56, .50, .83, color);
            poses.popPose();
            if (pinned) {
                line(poses, buffer, .18, .91, .82, .91, color);
            }
        }
        poses.popPose();
    }

    private void line(PoseStack poses, MultiBufferSource buffer, double x1, double y1, double x2, double y2, int color) {
        HoloGuiRenderTools.renderLine(poses, buffer, x + x1 * w, y + y1 * h, x + x2 * w, y + y2 * h, .0315, color);
    }

    @Override
    public void renderTooltip(PoseStack poses, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        String key = kind == Kind.PIN ? holo.isPinned() ? "unpin" : "pin" : kind == Kind.HELP ? "help" : "close";
        double left = Math.min(x, RIGHT - 2.7);
        poses.pushPose();
        poses.translate(0, 0, .012);
        HoloGuiRenderTools.renderControl(poses, buffer, left, y + h + .1, 2.7, .55, false);
        HoloGuiRenderTools.renderControlText(poses, buffer, left + .1, y + h + .15, 2.5, .45,
                Component.translatable("gui.hologui." + key).getString(), HOVER, .42f);
        poses.popPose();
    }

    @Override
    public void hit(Player player, IHoloGuiEntity holo, double x, double y) {
        switch (kind) {
            case CLOSE -> holo.close();
            case HELP -> help.hit(this, player, holo, x, y);
            case PIN -> holo.setPinned(!holo.isPinned());
        }
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity holo, double x, double y) {
        Entity entity = holo.getEntity();
        player.level().playSound(player, entity.getX(), entity.getY(), entity.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1, 1);
    }
}
