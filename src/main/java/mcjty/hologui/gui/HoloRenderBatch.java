package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Captures widget geometry and item state during extraction, with no live world access at submission. */
public final class HoloRenderBatch implements MultiBufferSource {

    private final List<BiConsumer<PoseStack, SubmitNodeCollector>> draws = new ArrayList<>();

    @Override
    public VertexConsumer getBuffer(RenderType type) {
        List<Vertex> vertices = new ArrayList<>();
        draws.add((poses, collector) -> {
            if (vertices.isEmpty()) return;
            collector.submitCustomGeometry(poses, type, (pose, output) -> {
            for (Vertex v : vertices) {
                output.addVertex(pose, v.x, v.y, v.z).setColor(v.color).setUv(v.u, v.v)
                        .setOverlay(v.overlay).setLight(v.light).setNormal(pose, v.nx, v.ny, v.nz).setLineWidth(v.width);
            }
            });
        });
        return new VertexConsumer() {
            private Vertex current;

            @Override
            public VertexConsumer addVertex(float x, float y, float z) {
                current = new Vertex(x, y, z);
                vertices.add(current);
                return this;
            }

            @Override
            public VertexConsumer setColor(int r, int g, int b, int a) {
                return setColor(a << 24 | r << 16 | g << 8 | b);
            }

            @Override
            public VertexConsumer setColor(int color) {
                current.color = color;
                return this;
            }

            @Override
            public VertexConsumer setUv(float u, float v) {
                current.u = u;
                current.v = v;
                return this;
            }

            @Override
            public VertexConsumer setUv1(int u, int v) {
                current.overlay = u | v << 16;
                return this;
            }

            @Override
            public VertexConsumer setUv2(int u, int v) {
                current.light = u | v << 16;
                return this;
            }

            @Override
            public VertexConsumer setNormal(float x, float y, float z) {
                current.nx = x;
                current.ny = y;
                current.nz = z;
                return this;
            }

            @Override
            public VertexConsumer setLineWidth(float width) {
                current.width = width;
                return this;
            }
        };
    }

    public void item(PoseStack poses, ItemStack stack) {
        item(poses, stack, ItemDisplayContext.GUI, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1, false, 1);
    }

    public void item(PoseStack poses, ItemStack stack, ItemDisplayContext context, int light, int overlay, float brightness, boolean invalid, float alpha) {
        ItemStackRenderState state = new ItemStackRenderState();
        Minecraft mc = Minecraft.getInstance();
        mc.getItemModelResolver().updateForTopItem(state, stack, context, mc.level, mc.player, 1);
        command(poses, (base, collector) -> state.submit(base,
                new ItemTintCollector(collector, brightness, invalid, alpha), light, overlay, 0));
    }

    public void command(PoseStack poses, BiConsumer<PoseStack, SubmitNodeCollector> command) {
        PoseStack.Pose local = poses.last().copy();
        draws.add((base, collector) -> {
            base.pushPose();
            base.mulPose(local.pose());
            command.accept(base, collector);
            base.popPose();
        });
    }

    public void submit(PoseStack poses, SubmitNodeCollector collector) {
        int order = 0;
        for (var draw : draws) {
            draw.accept(poses, new OrderedSubmitCollector(collector.order(order++)));
        }
    }

    private static final class Vertex {
        private final float x, y, z;
        private int color = -1, overlay, light = LightCoordsUtil.FULL_BRIGHT;
        private float u, v, nx, ny, nz = 1, width = 1;

        private Vertex(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
