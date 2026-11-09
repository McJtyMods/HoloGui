package mcjty.hologui.gui;

import net.minecraft.client.renderer.texture.TextureAtlas;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.quad.BakedColors;

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
                           int[] tints, List<BakedQuad> quads, ItemStackRenderState.FoilType foil) {
        if (brightness != 1 || invalid || alpha != 1) {
            quads = quads.stream().map(quad -> {
                var colors = quad.bakedColors();
                var info = quad.materialInfo();
                if (alpha < 1) info = new BakedQuad.MaterialInfo(info.sprite(), info.layer(), (info.sprite().atlasLocation().equals(TextureAtlas.LOCATION_ITEMS)
                        ? Sheets.translucentItemSheet() : Sheets.translucentBlockItemSheet()),
                        info.tintIndex(), info.shade(), info.lightEmission(), info.ambientOcclusion());
                return new BakedQuad(quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                        quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(), quad.direction(), info,
                        quad.bakedNormals(), BakedColors.of(tint(colors.color(0)), tint(colors.color(1)), tint(colors.color(2)), tint(colors.color(3))));
            }).toList();
        }
        delegate.submitItem(poses, context, light, overlay, outline, tints, quads, foil);
    }
}
