package mcjty.hologui.gui.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class HoloStackIcon extends AbstractHoloComponent<IStackIcon> implements IStackIcon {

    private Supplier<ItemStack> stackSupplier;
    private float scale = 1.0f;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    HoloStackIcon(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public IStackIcon scale(float scale) {
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
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderItem(matrixStack, buffer, x, y, stackSupplier.get(), null, false, scale);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void renderTooltip(MatrixStack matrixStack, IRenderTypeBuffer buffer, PlayerEntity player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        matrixStack.push();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
        matrixStack.translate(0, 0, -10);
        matrixStack.scale(0.4f, 0.4f, 0.0f);
        HoloGuiRenderTools.renderToolTip(matrixStack, buffer, stackSupplier.get(), (int) (x * 30 - 120), (int) (y * 30 - 120), tooltipHandler);
        matrixStack.pop();
    }

}
