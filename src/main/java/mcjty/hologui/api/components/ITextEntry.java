package mcjty.hologui.api.components;

import java.util.function.Supplier;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import net.minecraft.world.entity.player.Player;

/** A named multiline field. Changes are committed on Escape or clicking away. */
public interface ITextEntry extends IGuiComponent<ITextEntry> {

    ITextEntry text(Supplier<String> text);

    ITextEntry maxLength(int length);

    ITextEntry changed(ChangeEvent event);

    String getText();

    int getMaxLength();

    void commit(Player player, IHoloGuiEntity entity, String text);

    interface ChangeEvent {

        void changed(Player player, IHoloGuiEntity entity, String text);
    }
}
