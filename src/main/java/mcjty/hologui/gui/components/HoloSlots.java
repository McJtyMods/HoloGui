package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.ISlots;
import net.minecraft.entity.player.EntityPlayer;

public class HoloSlots extends AbstractHoloComponent implements ISlots {

    public HoloSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {

    }
}
