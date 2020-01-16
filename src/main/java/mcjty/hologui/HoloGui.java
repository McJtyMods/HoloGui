package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.setup.ClientRegistration;
import mcjty.hologui.setup.ModSetup;
import mcjty.lib.base.ModBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HoloGui.MODID)
public class HoloGui implements ModBase {
    public static final String MODID = "hologui";

    public static ModSetup setup = new ModSetup();

    public static HoloGui instance;

    public static IHoloGuiHandler guiHandler = new HoloGuiHandler();

    public HoloGui() {
        instance = this;

//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        ModEntities.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> setup.init(event));
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> ClientRegistration.init());

//        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("xnet-client.toml"));
//        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("xnet-common.toml"));
    }


    @Override
    public String getModId() {
        return HoloGui.MODID;
    }

    // @todo 1.14
//    @Mod.EventHandler
//    public void imcCallback(FMLInterModComms.IMCEvent event) {
//        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
//            if (message.key.equalsIgnoreCase("getHoloHandler")) {
//                Optional<Function<IHoloGuiHandler, Void>> value = message.getFunctionValue(IHoloGuiHandler.class, Void.class);
//                if (value.isPresent()) {
//                    value.get().apply(guiHandler);
//                } else {
//                    setup.getLogger().warn("Some mod didn't return a valid result with getHoloHandler!");
//                }
//            }
//        }
//    }

    @Override
    public void openManual(PlayerEntity player, int bookindex, String page) {
        // @todo
    }
}
