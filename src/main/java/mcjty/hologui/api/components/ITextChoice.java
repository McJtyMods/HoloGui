package mcjty.hologui.api.components;

import mcjty.hologui.api.IColor;
import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public interface ITextChoice extends IGuiComponent<ITextChoice> {

    ITextChoice addText(String text);

    ITextChoice getter(Function<Player, Integer> getter);

    ITextChoice hitEvent(IEvent event);
    ITextChoice hitEventClient(IEvent event);

    @Deprecated
    ITextChoice color(int color);
    ITextChoice color(IColor color);

    @Deprecated
    ITextChoice hoverColor(int color);
    ITextChoice hoverColor(IColor color);

    // -1 to disable border
    @Deprecated
    ITextChoice borderColor(int color);
    ITextChoice borderColor(IColor color);

    ITextChoice scale(float scale);
}
