package mcjty.hologui.api.components;

import java.util.function.Supplier;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import net.minecraft.world.entity.player.Player;

/** Hue strip and saturation/value palette. Colors are opaque 0xRRGGBB. */
public interface IColorPicker extends IGuiComponent<IColorPicker> {

    IColorPicker color(Supplier<Integer> color);

    IColorPicker selected(ColorEvent event);

    interface ColorEvent {

        void selected(Player player, IHoloGuiEntity entity, int color);
    }
}
