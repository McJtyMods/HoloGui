package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mcjty.hologui.api.*;
import mcjty.hologui.gui.ColorFromStyle;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.MultiBufferSource;
// @todo 1.18 import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class AbstractSlots<P extends IGuiComponent<P>> extends AbstractHoloComponent<P> {

    protected BiPredicate<ItemStack, Integer> filter = (stack, index) -> true;
    protected BiFunction<ItemStack, Integer, IImage> overlay = (itemStack, integer) -> null;
    protected IStackEvent hitEvent;
    protected IStackEvent doubleClickEvent;
    protected int selected = -1;
    private boolean withAmount = false;
    private boolean exactView = false;
    private boolean fullBright = false;

    private int previousSelected = -1;
    private long previousTime = -1;
    private BiConsumer<ItemStack, List<String>> tooltipHandler = (itemStack, strings) -> {};

    public AbstractSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Pair<ItemStack, Integer> pair = getSelectedPair(player, cursorX, cursorY);
        if (hitEvent != null) {
            hitEvent.hit(this, player, entity, cursorX, cursorY, pair.getLeft(), pair.getRight());
        }

        selected = pair.getRight();

        long time = System.currentTimeMillis();
        if (doubleClickEvent != null && previousTime != -1 && previousSelected != -1) {
            if (time - previousTime < 500 && selected == previousSelected) {
                doubleClickEvent.hit(this, player, entity, cursorX, cursorY, pair.getLeft(), pair.getRight());
            }
        }
        previousTime = time;
        previousSelected = selected;
    }

    @Override
    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Entity ent = entity.getEntity();
        player.level.playSound(player, ent.getX(), ent.getY(), ent.getZ(), HoloGuiSounds.GUICLICK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        Pair<ItemStack, Integer> pair = getSelectedPair(player, cursorX, cursorY);
        selected = pair.getRight();
    }

    public P tooltipHandler(BiConsumer<ItemStack, List<String>> tooltipHandler) {
        this.tooltipHandler = tooltipHandler;
        return getThis();
    }

    private P getThis() {
        //noinspection unchecked
        return (P) this;
    }

    public P withAmount() {
        this.withAmount = true;
        return getThis();
    }

    public P exactView() {
        this.exactView = true;
        return getThis();
    }

    public P fullBright() {
        this.fullBright = true;
        return getThis();
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        IColor color = new ColorFromStyle(StyledColor.BORDER);
        int bc = color.getColor();
        if (bc != -1) {
            HoloGuiRenderTools.renderBorder(matrixStack, buffer, x, y, w, h, bc & 255, (bc >> 8) & 255, (bc >> 16) & 255, (bc >> 24) & 255);
        }
        double yy = y;
        double xx = x;

        Pair<ItemStack, Integer> cursorPair = getSelectedPair(player, cursorX, cursorY);

        for (Pair<ItemStack, Integer> pair : getStacks(player)) {
            ItemStack stack = pair.getLeft();
            if (exactView || !stack.isEmpty()) {
                ResourceLocation lightmap = null;
                if ((!fullBright) && selected != pair.getRight()) {
                    lightmap = HoloStackToggle.DARKEN;
                }
                HoloGuiRenderTools.renderItem(matrixStack, buffer, xx, yy, stack, lightmap, cursorPair.getRight() == pair.getRight(), 0.9f);
                if (withAmount && stack.getCount() > 1) {
                    matrixStack.pushPose();
                    matrixStack.scale(.5f, .5f, .5f);
                    String s = Integer.toString(stack.getCount());
                    HoloGuiRenderTools.renderTextShadow(matrixStack, buffer, xx * 2 - 4 + .4, yy * 2 - 4 + .9, s, 0xffffffff, 1.0f);
                    matrixStack.popPose();
                }

                IImage image = overlay.apply(stack, pair.getRight());
                if (image != null) {
                    HoloGuiRenderTools.renderImage(matrixStack, buffer, xx+.13, yy+.13, image.getU(), image.getV(), 16, 16, image.getWidth(), image.getHeight(), image.getImage());
                }

                xx++;
                if (xx >= x + w) {
                    yy++;
                    xx = x;
                    if (yy >= y + h) {
                        break;
                    }
                }
            }
        }
        // @todo 1.18 RenderHelper.turnBackOn();
    }

    protected abstract List<Pair<ItemStack, Integer>> getStacks(Player player);

    @Nonnull
    protected Pair<ItemStack, Integer> getSelectedPair(Player player, double cursorX, double cursorY) {
        if (!isInside(cursorX, cursorY)) {
            return Pair.of(ItemStack.EMPTY, -1);
        }
        List<Pair<ItemStack, Integer>> stacks = getStacks(player);

        double yy = y;
        double xx = x;

        for (Pair<ItemStack, Integer> pair : stacks) {
            ItemStack stack = pair.getLeft();
            if (exactView || !stack.isEmpty()) {
                if (cursorX >= xx && cursorX <= xx+1 && cursorY >= yy && cursorY <= yy+1) {
                    return pair;
                }
                xx++;
                if (xx >= x + w) {
                    yy++;
                    xx = x;
                    if (yy >= y + h) {
                        break;
                    }
                }
            }
        }
        return Pair.of(ItemStack.EMPTY, -1);

    }

    @Nonnull
    protected ItemStack getSelectedStack(Player player, double cursorX, double cursorY) {
        return getSelectedPair(player, cursorX, cursorY).getLeft();
    }

    @Override
    public void renderTooltip(PoseStack matrixStack, MultiBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        ItemStack stack = getSelectedStack(player, cursorX, cursorY);
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            matrixStack.scale(0.01f, 0.01f, 0.01f);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            matrixStack.translate(0, 0, -10);
            matrixStack.scale(0.4f, 0.4f, 0.0f);
            int xx = (int) (cursorX - x);
            int yy = (int) (cursorY - y);
            HoloGuiRenderTools.renderToolTip(matrixStack, buffer, stack, (int) ((xx+x) * 30 - 120), (int) ((yy+y) * 30 - 120 + 25), tooltipHandler);
            // @todo 1.18 RenderHelper.turnBackOn();
            matrixStack.popPose();
        }
    }

}
