package mcjty.hologui.api;

import net.minecraft.resources.Identifier;

public interface IImage {

    Identifier getImage();

    int getWidth();

    int getHeight();

    int getU();

    int getV();
}
