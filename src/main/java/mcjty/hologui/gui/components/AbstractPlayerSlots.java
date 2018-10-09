package mcjty.hologui.gui.components;

import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.IStackEvent;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractPlayerSlots extends AbstractHoloComponent {

    protected Predicate<ItemStack> filter = itemStack -> false;
    protected IStackEvent hitEvent;

    public AbstractPlayerSlots(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public void hit(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (hitEvent != null) {
            Pair<ItemStack, Integer> pair = getSelectedPair(player, cursorX, cursorY);
            hitEvent.hit(this, player, entity, cursorX, cursorY, pair.getLeft(), pair.getRight());
        }
    }

    @Override
    public void hitClient(EntityPlayer player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        Entity ent = entity.getEntity();
        player.world.playSound(ent.posX, ent.posY, ent.posZ, HoloGuiSounds.guiclick, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
    }

    @Override
    public void render(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderBorder(x, y, w, h, 255, 255, 255, 255);
        double yy = y;
        double xx = x;

        for (Pair<ItemStack, Integer> pair : getStacks(player)) {
            ItemStack stack = pair.getLeft();
            HoloGuiRenderTools.renderItem(xx, yy, stack, null, false, 0.9);
            xx++;
            if (xx >= x+w) {
                yy++;
                xx = x;
                if (yy >= y+h) {
                    break;
                }
            }
        }
        RenderHelper.enableStandardItemLighting();
    }

    protected abstract List<Pair<ItemStack, Integer>> getStacks(EntityPlayer player);

    @Nonnull
    protected Pair<ItemStack, Integer> getSelectedPair(EntityPlayer player, double cursorX, double cursorY) {
        List<Pair<ItemStack, Integer>> stacks = getStacks(player);

        int xx = (int) (cursorX - x);
        int yy = (int) (cursorY - y);
        int ww = (int) w;

        int index = yy * ww + xx % ww;
        if (index >= 0 && index < stacks.size()) {
            return stacks.get(index);
        } else {
            return Pair.of(ItemStack.EMPTY, -1);
        }
    }

    @Nonnull
    protected ItemStack getSelectedStack(EntityPlayer player, double cursorX, double cursorY) {
        return getSelectedPair(player, cursorX, cursorY).getLeft();
    }

    @Override
    public void renderTooltip(EntityPlayer player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        ItemStack stack = getSelectedStack(player, cursorX, cursorY);
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.01, 0.01, 0.01);
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.translate(0, 0, -10);
            GlStateManager.scale(0.4, 0.4, 0.0);
            int xx = (int) (cursorX - x);
            int yy = (int) (cursorY - y);
            HoloGuiRenderTools.renderToolTip(stack, (int) ((xx+x) * 30 - 120), (int) ((yy+y) * 30 - 120 + 25));
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

}
