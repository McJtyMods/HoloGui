package mcjty.hologui.gui;

import mcjty.hologui.api.IGuiComponent;
import mcjty.hologui.api.IGuiRegistry;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GuiRegistry implements IGuiRegistry {

    private final Map<String, Function<Player, IGuiComponent>> registry = new HashMap<>();

    @Override
    public void registerGui(String id, Function<Player, IGuiComponent> factory) {
        registry.put(id, factory);
    }

    @Override
    @Nullable
    public IGuiComponent createGui(String id, Player player) {
        return  registry.getOrDefault(id, (p) -> null).apply(player);
    }
}
