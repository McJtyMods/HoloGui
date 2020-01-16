package mcjty.hologui.api.components;

import mcjty.hologui.api.IEvent;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IImage;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Function;

public interface IIconChoice extends IGuiComponent<IIconChoice> {

    IIconChoice addImage(IImage image);

    /**
     * Get an image index (add to the image table using addImage)
     */
    IIconChoice getter(Function<PlayerEntity, Integer> getter);

    IIconChoice hitEvent(IEvent event);
}
