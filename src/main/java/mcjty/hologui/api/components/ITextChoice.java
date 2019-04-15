package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Function;

public interface ITextChoice extends IGuiComponent<ITextChoice> {

    ITextChoice addText(String text);

    ITextChoice getter(Function<EntityPlayer, Integer> getter);

    ITextChoice hitEvent(IEvent event);

    ITextChoice color(int color);

    ITextChoice scale(float scale);
}
