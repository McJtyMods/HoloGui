package mcjty.hologui.api;

import net.minecraft.resources.ResourceLocation;

public interface IImage {

    ResourceLocation getImage();

    int getWidth();

    int getHeight();

    int getU();

    int getV();
}
