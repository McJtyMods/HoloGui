package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.components.IPanel;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HoloPanel extends AbstractHoloComponent implements IPanel {

    private final List<IGuiComponent> children = new ArrayList<>();

    HoloPanel(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public HoloPanel add(IGuiComponent... components) {
        Collections.addAll(children, components);
        return this;
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        for (IGuiComponent child : children) {
            child.render(player, holo, cursorX, cursorY);
        }
    }

    @Override
    public IGuiComponent findHoveringWidget(double cursorX, double cursorY) {
        for (IGuiComponent child : children) {
            if (child.isInside(cursorX, cursorY)) {
                return child.findHoveringWidget(cursorX, cursorY);
            }
        }
        return null;
    }

    @Override
    public void hit(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        for (IGuiComponent child : children) {
            if (child.isInside(cursorX, cursorY)) {
                child.hit(player, entity, cursorX, cursorY);
            }
        }
    }

    @Override
    public void hitClient(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        for (IGuiComponent child : children) {
            if (child.isInside(cursorX, cursorY)) {
                child.hitClient(player, entity, cursorX, cursorY);
            }
        }
    }
}
