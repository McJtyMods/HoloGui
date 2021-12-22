package mcjty.hologui.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mcjty.hologui.HoloGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> commands = dispatcher.register(
                Commands.literal(HoloGui.MODID)
                        .then(CommandHoloCfg.register(dispatcher))
        );

        dispatcher.register(Commands.literal("holo").redirect(commands));
    }

}
