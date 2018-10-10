package mcjty.hologui.api;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Optional;

public interface IGuiComponent<P extends IGuiComponent<P>> {

    void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY);

    void renderTooltip(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY);

    IGuiComponent<?> findHoveringWidget(double cursorX, double cursorY);

    Optional<IGuiComponent<?>> findChild(String name);

    void hit(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY);

    void hitClient(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY);

    boolean isInside(double x, double y);

    double getX();

    double getY();

    double getW();

    double getH();

    /// Set the name of this component
    P name(String name);

    String getName();
}
