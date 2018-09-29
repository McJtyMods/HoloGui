package mcjty.hologui.gui;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IHoloGuiRenderer;

public class HoloGuiRenderer implements IHoloGuiRenderer {

    @Override
    public void render(IHoloGuiEntity entity, double x, double y, double z, float entityYaw) {
        HoloGuiEntityRender.doActualRender((HoloGuiEntity) entity, x, y, z, entityYaw);
    }

}
