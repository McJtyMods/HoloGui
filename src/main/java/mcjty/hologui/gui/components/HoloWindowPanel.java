package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IPanel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Shared window controls, independent of a panel's content and current page. */
public class HoloWindowPanel extends AbstractHoloComponent<HoloWindowPanel> {

    private final IGuiComponent<?> content;
    private final IHoloGuiEntity holo;
    private final IEvent help;
    private final List<HoloWindowButton> controls = new ArrayList<>();
    private boolean closeVisible;
    private boolean pinVisible;
    private boolean initialized;

    public HoloWindowPanel(IGuiComponent<?> content, IHoloGuiEntity holo) {
        super(content.getX(), content.getY(), content.getW(), content.getH());
        this.content = content;
        this.holo = holo;
        help = content instanceof IPanel panel ? panel.getHelpEvent() : null;
    }

    private List<HoloWindowButton> controls() {
        if (!initialized || closeVisible != holo.canClose() || pinVisible != holo.canPin()) {
            initialized = true;
            closeVisible = holo.canClose();
            pinVisible = holo.canPin();
            controls.clear();
            double right = HoloWindowButton.RIGHT;
            if (closeVisible) {
                controls.add(new HoloWindowButton(right -= HoloWindowButton.WIDTH, HoloWindowButton.Kind.CLOSE, null));
            }
            if (help != null) {
                controls.add(new HoloWindowButton(right -= HoloWindowButton.WIDTH, HoloWindowButton.Kind.HELP, help));
            }
            if (pinVisible) {
                controls.add(new HoloWindowButton(right - HoloWindowButton.WIDTH, HoloWindowButton.Kind.PIN, null));
            }
        }
        return controls;
    }

    @Override
    public void render(PoseStack poses, MultiBufferSource buffer, Player player, IHoloGuiEntity entity, double x, double y) {
        content.render(poses, buffer, player, entity, x, y);
        poses.pushPose();
        poses.translate(0, 0, .004);
        for (HoloWindowButton button : controls()) {
            button.render(poses, buffer, player, entity, x, y);
        }
        poses.popPose();
    }

    @Override
    public IGuiComponent<?> findHoveringWidget(double x, double y) {
        for (HoloWindowButton button : controls()) {
            if (button.isInside(x, y)) {
                return button;
            }
        }
        return content.findHoveringWidget(x, y);
    }

    @Override
    public Optional<IGuiComponent<?>> findChild(String name) {
        for (HoloWindowButton button : controls()) {
            if (name.equals(button.getName())) {
                return Optional.of(button);
            }
        }
        return content.findChild(name);
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double x, double y) {
        for (HoloWindowButton button : controls()) {
            if (button.isInside(x, y)) {
                button.hit(player, entity, x, y);
                return;
            }
        }
        content.hit(player, entity, x, y);
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double x, double y) {
        for (HoloWindowButton button : controls()) {
            if (button.isInside(x, y)) {
                button.hitClient(player, entity, x, y);
                return;
            }
        }
        content.hitClient(player, entity, x, y);
    }
}
