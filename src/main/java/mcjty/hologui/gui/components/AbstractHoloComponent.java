package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IHoloGuiEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public abstract class AbstractHoloComponent<P extends IGuiComponent<P>> implements IGuiComponent<P> {

    private String name;

    protected final double x;
    protected final double y;
    protected final double w;
    protected final double h;

    public AbstractHoloComponent(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean isInside(double x, double y) {
        return x >= this.x && x <= (this.x + this.w) && y >= this.y && y <= (this.y + this.h);
    }

    @Override
    public void renderTooltip(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {

    }

    @Override
    public IGuiComponent<?> findHoveringWidget(double cursorX, double cursorY) {
        return this;
    }

    @Override
    public Optional<IGuiComponent<?>> findChild(String name) {
        if (name.equals(this.name)) {
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getW() {
        return w;
    }

    @Override
    public double getH() {
        return h;
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {

    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {

    }

    @Override
    public P name(String name) {
        this.name = name;
        //noinspection unchecked
        return (P) this;
    }

    @Override
    public String getName() {
        return name;
    }
}
