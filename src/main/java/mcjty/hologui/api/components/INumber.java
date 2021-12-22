package mcjty.hologui.api.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface INumber extends IGuiComponent<INumber> {

    INumber getter(BiFunction<Player, IHoloGuiEntity, Integer> getter);

    @Deprecated
    INumber color(int color);
    INumber color(IColor color);

    INumber colorGetter(Function<Player, Integer> getter);
}
