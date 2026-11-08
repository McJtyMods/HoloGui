package mcjty.hologui.api.components;

import java.util.function.BiFunction;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import net.minecraft.world.entity.player.Player;

/** Bounded integer input: click/drag the track, or use either arrow for one exact step. */
public interface IIntegerSlider extends IGuiComponent<IIntegerSlider> {

    IIntegerSlider label(String label);

    IIntegerSlider suffix(String suffix);

    IIntegerSlider range(int min, int max);

    IIntegerSlider step(int step);

    IIntegerSlider logarithmic(boolean logarithmic);

    IIntegerSlider getter(BiFunction<Player, IHoloGuiEntity, Integer> getter);

    IIntegerSlider changed(ChangeEvent event);

    boolean isTrack(double x, double y);

    void drag(Player player, IHoloGuiEntity entity, double x, double y);

    interface ChangeEvent {

        void changed(Player player, IHoloGuiEntity entity, int value);
    }
}
