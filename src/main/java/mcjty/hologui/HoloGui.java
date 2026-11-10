package mcjty.hologui;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.commands.ModCommands;
import mcjty.hologui.config.Config;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.network.HoloMessages;
import mcjty.hologui.setup.ModSetup;
import mcjty.hologui.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.neoforged.fml.config.ModConfig;

public class HoloGui implements ModInitializer {

    public static final String MODID = "hologui";
    public static final ModSetup setup = new ModSetup();
    public static HoloGui instance;
    public static final IHoloGuiHandler guiHandler = new HoloGuiHandler();

    @Override
    public void onInitialize() {
        instance = this;
        NeoForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        Registration.init();
        setup.init();
        HoloMessages.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, context, environment) -> ModCommands.register(dispatcher));
    }
}
