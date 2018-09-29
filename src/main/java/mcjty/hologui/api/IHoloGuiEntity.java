package mcjty.hologui.api;

import net.minecraft.entity.Entity;

public interface IHoloGuiEntity {

    Entity getEntity();

    void setMaxTimeout(int maxTimeout);

    void setTimeout(int timeout);

    void switchGui(String guiId);
}

