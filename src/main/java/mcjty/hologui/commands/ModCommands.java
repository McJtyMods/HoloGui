package mcjty.hologui.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mcjty.hologui.HoloGui;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> commands = dispatcher.register(
                Commands.literal(HoloGui.MODID)
                        .then(CommandHoloCfg.register(dispatcher))
        );

        dispatcher.register(Commands.literal("holo").redirect(commands));
    }

}
