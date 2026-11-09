package mcjty.hologui.gui;

import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import net.minecraft.client.renderer.texture.TextureAtlas;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;

final class ItemTintCollector extends OrderedSubmitCollector {

    private final float brightness;
    private final boolean invalid;
    private final float alpha;

    ItemTintCollector(SubmitNodeCollector delegate, float brightness, boolean invalid, float alpha) {
        super(delegate);
        this.brightness = brightness;
        this.invalid = invalid;
        this.alpha = alpha;
    }

    private int tint(int color) {
        int r = (int) ((color >>> 16 & 255) * (invalid ? 1 : brightness));
        int g = (int) ((color >>> 8 & 255) * brightness);
        int b = (int) ((color & 255) * brightness);
        return (int) ((color >>> 24) * alpha) << 24 | r << 16 | g << 8 | b;
    }

    @Override
    public void submitItem(PoseStack poses, ItemDisplayContext context, int light, int overlay, int outline,
                           int[] tints, ItemQuads quads, ItemStackRenderState.FoilType foil) {
        if (brightness != 1 || invalid || alpha != 1) {
            for (BakedQuad quad : quads.all()) {
                var material = quad.materialInfo();
                int base = material.isTinted() && material.tintIndex() < tints.length ? tints[material.tintIndex()] : 0xffffffff;
                int color = tint(base);
                delegate.submitCustomGeometry(poses, RenderTypes.entityTranslucent(material.sprite().atlasLocation()), (pose, buffer) -> {
                    var instance = new QuadInstance();
                    instance.setColor(color);
                    instance.setLightCoords(light);
                    instance.setOverlayCoords(overlay);
                    buffer.putBakedQuad(pose, quad, instance);
                });
            }
            return;
        }
        delegate.submitItem(poses, context, light, overlay, outline, tints, quads, foil);
    }
}
