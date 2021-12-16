package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IPanel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HoloPanel extends AbstractHoloComponent<IPanel> implements IPanel {

    private final List<IGuiComponent<?>> children = new ArrayList<>();

    HoloPanel(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IPanel add(IGuiComponent<?>... components) {
        Collections.addAll(children, components);
        return this;
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        for (IGuiComponent<?> child : children) {
            child.render(matrixStack, buffer, player, holo, cursorX, cursorY);
        }
    }

    @Override
    public IGuiComponent<?> findHoveringWidget(double cursorX, double cursorY) {
        for (IGuiComponent<?> child : children) {
            if (child.isInside(cursorX, cursorY)) {
                return child.findHoveringWidget(cursorX, cursorY);
            }
        }
        return null;
    }

    @Override
    public Optional<IGuiComponent<?>> findChild(String name) {
        for (IGuiComponent<?> child : children) {
            Optional<IGuiComponent<?>> result = child.findChild(name);
            if (result.isPresent()) {
                return result;
            }
        }
        return super.findChild(name);
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        for (IGuiComponent<?> child : children) {
            if (child.isInside(cursorX, cursorY)) {
                child.hit(player, entity, cursorX, cursorY);
            }
        }
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        for (IGuiComponent<?> child : children) {
            if (child.isInside(cursorX, cursorY)) {
                child.hitClient(player, entity, cursorX, cursorY);
            }
        }
    }
}
