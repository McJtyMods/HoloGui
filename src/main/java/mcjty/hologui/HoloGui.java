package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.config.Config;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.setup.ClientRegistration;
import mcjty.hologui.setup.ModSetup;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(HoloGui.MODID)
public class HoloGui {
    public static final String MODID = "hologui";

    public static ModSetup setup = new ModSetup();

    public static HoloGui instance;

    public static IHoloGuiHandler guiHandler = new HoloGuiHandler();

    public HoloGui() {
        instance = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        ModEntities.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> setup.init(event));
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> ClientRegistration.init());
        // The following is needed to make sure our SpriteUploader is setup at exactly the right moment
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, ColorHandlerEvent.Block.class, event -> ClientRegistration.setupSpriteUploader());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("hologui-client.toml"));
    }


    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            if ("getHoloHandler".equalsIgnoreCase(message.getMethod())) {
                Supplier<Function<IHoloGuiHandler, Void>> supplier = message.getMessageSupplier();
                supplier.get().apply(guiHandler);
            }
        });
    }

}
