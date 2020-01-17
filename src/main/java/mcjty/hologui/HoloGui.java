package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.config.Config;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.setup.ClientRegistration;
import mcjty.hologui.setup.ModSetup;
import mcjty.lib.base.ModBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.function.Consumer;

@Mod(HoloGui.MODID)
public class HoloGui implements ModBase {
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcCallback);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("hologui-client.toml"));
    }


    @Override
    public String getModId() {
        return HoloGui.MODID;
    }

    private void imcCallback(InterModProcessEvent event) {
        event.getIMCStream("getHoloHandler"::equalsIgnoreCase).forEach(msg -> {
            try {
                msg.<Consumer<IHoloGuiHandler>>getMessageSupplier().get().accept(guiHandler);
            } catch (Exception e) {
                setup.getLogger().warn("The mod '{}' caused an error while trying to get the hologui handler: {}", msg.getModId(), e.getMessage());
            }
        });
    }

    @Override
    public void openManual(PlayerEntity player, int bookindex, String page) {
        // @todo
    }
}
