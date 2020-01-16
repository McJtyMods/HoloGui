package mcjty.hologui.gui;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IGuiRegistry;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GuiRegistry implements IGuiRegistry {

    private final Map<String, Function<PlayerEntity, IGuiComponent>> registry = new HashMap<>();

    @Override
    public void registerGui(String id, Function<PlayerEntity, IGuiComponent> factory) {
        registry.put(id, factory);
    }

    @Override
    @Nullable
    public IGuiComponent createGui(String id, PlayerEntity player) {
        return  registry.getOrDefault(id, (p) -> null).apply(player);
    }
}
