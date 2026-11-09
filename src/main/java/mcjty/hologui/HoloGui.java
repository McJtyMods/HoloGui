package mcjty.hologui;

import java.util.function.Function;
import java.util.function.Supplier;
import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.config.Config;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.gui.HoloGuiRenderType;
import mcjty.hologui.network.HoloMessages;
import mcjty.hologui.setup.ClientSetup;
import mcjty.hologui.setup.ModSetup;
import mcjty.hologui.setup.Registration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;

@Mod(HoloGui.MODID)
public class HoloGui {
    public static final String MODID = "hologui";

    public static ModSetup setup = new ModSetup();

    public static HoloGui instance;

    public static IHoloGuiHandler guiHandler = new HoloGuiHandler();

    public HoloGui(ModContainer mod, IEventBus bus, Dist dist) {
        instance = this;

        mod.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        Registration.init(bus);

        bus.addListener(setup::init);
        bus.addListener(HoloMessages::register);
        bus.addListener(this::processIMC);

        if (dist.isClient()) {
            ClientSetup.registerConfigScreen(mod);
            bus.addListener(ClientSetup::entityRenderers);
            // The following is needed to make sure our SpriteUploader is setup at exactly the right moment
            bus.addListener(ClientSetup::setupSpriteUploader);
            bus.addListener(HoloGuiRenderType::registerPipelines);
        }

    }

    private void processIMC(final InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            if ("getHoloHandler".equalsIgnoreCase(message.method())) {
                Supplier<?> supplier = message.messageSupplier();
                ((Function<IHoloGuiHandler, Void>) supplier.get()).apply(guiHandler);
            }
        });
    }

}
