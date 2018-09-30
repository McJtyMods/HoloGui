package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.proxy.CommonProxy;
import mcjty.lib.base.ModBase;
import mcjty.lib.compat.MainCompatHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;


@Mod(modid = HoloGui.MODID, name = HoloGui.MODNAME,
        dependencies =
                "required-after:mcjtylib_ng@[" + HoloGui.MIN_MCJTYLIB_VER + ",);" +
                "after:forge@[" + HoloGui.MIN_FORGE11_VER + ",)",
        acceptedMinecraftVersions = "[1.12,1.13)",
        version = HoloGui.VERSION)
public class HoloGui implements ModBase {
    public static final String MODID = "hologui";
    public static final String MODNAME = "HoloGui";
    public static final String VERSION = "0.0.2-alpha";
    public static final String MIN_FORGE11_VER = "14.23.3.2694";
    public static final String MIN_MCJTYLIB_VER = "3.0.4";

    @SidedProxy(clientSide = "mcjty.hologui.proxy.ClientProxy", serverSide = "mcjty.hologui.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static HoloGui instance;

    public static IHoloGuiHandler guiHandler = new HoloGuiHandler();

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Override
    public String getModId() {
        return HoloGui.MODID;
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equalsIgnoreCase("getHoloHandler")) {
                Optional<Function<IHoloGuiHandler, Void>> value = message.getFunctionValue(IHoloGuiHandler.class, Void.class);
                if (value.isPresent()) {
                    value.get().apply(guiHandler);
                } else {
                    logger.warn("Some mod didn't return a valid result with getHoloHandler!");
                }
            }
        }
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {
        // @todo
    }
}
