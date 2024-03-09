package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.config.Config;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.setup.ClientSetup;
import mcjty.hologui.setup.ModSetup;
import mcjty.hologui.setup.Registration;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.DistExecutor;
import net.neoforged.neoforge.fml.ModLoadingContext;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.config.ModConfig;
import net.neoforged.neoforge.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fml.loading.FMLPaths;

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

        Registration.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);
        bus.addListener(this::processIMC);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientSetup::entityRenderers);
            // The following is needed to make sure our SpriteUploader is setup at exactly the right moment
            bus.addListener(EventPriority.NORMAL, false, ClientSetup::setupSpriteUploader);
        });

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
