package mcjty.hologui.config;

import mcjty.hologui.HoloGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class HoloGuiModConfigGui extends GuiConfig {

    public HoloGuiModConfigGui(GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(HoloGui.setup.getConfig().getCategory(GuiConfiguration.CATEGORY_GUI)).getChildElements(),
                HoloGui.MODID, false, false, "HoloGui Config");
    }
}
