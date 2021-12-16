package mcjty.hologui.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface IGuiComponent<P extends IGuiComponent<P>> {

    void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY);

    void renderTooltip(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY);

    IGuiComponent<?> findHoveringWidget(double cursorX, double cursorY);

    Optional<IGuiComponent<?>> findChild(String name);

    void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY);

    void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY);

    boolean isInside(double x, double y);

    double getX();

    double getY();

    double getW();

    double getH();

    /// Set the name of this component
    P name(String name);

    String getName();
}
