package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.IStackIcon;
import mcjty.hologui.gui.HoloGuiRenderTools;
import net.minecraft.client.renderer.MultiBufferSource;
// @todo 1.18 import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderItem(matrixStack, buffer, x, y, stackSupplier.get(), null, false, scale);
        // @todo 1.18 RenderHelper.turnBackOn();
    }

    @Override
    public void renderTooltip(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        matrixStack.pushPose();
        matrixStack.scale(0.01f, 0.01f, 0.01f);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStack.translate(0, 0, -10);
        matrixStack.scale(0.4f, 0.4f, 0.0f);
        HoloGuiRenderTools.renderToolTip(matrixStack, buffer, stackSupplier.get(), (int) (x * 30 - 120), (int) (y * 30 - 120), tooltipHandler);
        matrixStack.popPose();
    }

}
