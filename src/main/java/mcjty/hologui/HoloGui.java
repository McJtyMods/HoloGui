package mcjty.hologui;


import mcjty.hologui.api.IHoloGuiHandler;
import mcjty.hologui.commands.CommandHoloCfg;
import mcjty.hologui.gui.HoloGuiHandler;
import mcjty.hologui.setup.ModSetup;
import mcjty.lib.base.ModBase;
import mcjty.lib.proxy.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.util.Optional;
import java.util.function.Function;


@Mod(modid = HoloGui.MODID, name = HoloGui.MODNAME,
        dependencies =
                "required-after:mcjtylib_ng@[" + HoloGui.MIN_MCJTYLIB_VER + ",);" +
                "after:forge@[" + HoloGui.MIN_FORGE11_VER + ",)",
        acceptedMinecraftVersions = "[1.12,1.13)",
        version = HoloGui.VERSION,
        guiFactory = "mcjty.hologui.config.HoloGuiModGuiFactory")
public class HoloGui implements ModBase {
    public static final String MODID = "hologui";
    public static final String MODNAME = "HoloGui";
    public static final String VERSION = "0.0.7-beta";
    public static final String MIN_FORGE11_VER = "14.23.3.2694";
    public static final String MIN_MCJTYLIB_VER = "3.5.0";

    @SidedProxy(clientSide = "mcjty.hologui.setup.ClientProxy", serverSide = "mcjty.hologui.setup.ServerProxy")
    public static IProxy proxy;
    public static ModSetup setup = new ModSetup();

    @Mod.Instance
    public static HoloGui instance;

    public static IHoloGuiHandler guiHandler = new HoloGuiHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        setup.preInit(event);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        setup.init(e);
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        setup.postInit(e);
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHoloCfg());
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
                    setup.getLogger().warn("Some mod didn't return a valid result with getHoloHandler!");
                }
            }
        }
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {
        // @todo
    }
}
