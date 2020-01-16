package mcjty.hologui.gui.components;

import com.mojang.blaze3d.platform.GlStateManager;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class HoloStackIcon extends AbstractHoloComponent<IStackIcon> implements IStackIcon {

    private Supplier<ItemStack> stackSupplier;
    private double scale = 1.0;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    HoloStackIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IStackIcon scale(double scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public IStackIcon itemStack(@Nonnull ItemStack stack) {
        this.stackSupplier = () -> stack;
        return this;
    }

    @Override
    public IStackIcon itemStack(Supplier<ItemStack> supplier) {
        this.stackSupplier = supplier;
        return this;
    }

    @Override
    public IStackIcon tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler) {
        this.tooltipHandler = tooltipHandler;
        return this;
    }

    @Override
    public void render(PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderItem(x, y, stackSupplier.get(), null, false, scale);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void renderTooltip(PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        GlStateManager.pushMatrix();
        GlStateManager.scaled(0.01, 0.01, 0.01);
        GlStateManager.rotatef(180, 0, 1, 0);
        GlStateManager.rotatef(180, 0, 0, 1);
        GlStateManager.translatef(0, 0, -10);
        GlStateManager.scaled(0.4, 0.4, 0.0);
        HoloGuiRenderTools.renderToolTip(stackSupplier.get(), (int) (x * 30 - 120), (int) (y * 30 - 120), tooltipHandler);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

}
