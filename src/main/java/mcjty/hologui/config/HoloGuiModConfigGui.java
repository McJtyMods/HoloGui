package mcjty.hologui.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class HoloGuiModConfigGui extends Screen {

    private final Screen parent;

    public HoloGuiModConfigGui(Screen parent) {
        super(Component.literal("HoloGui settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(CycleButton.<GuiStyle>builder(style -> Component.literal(style.getName()))
                .withValues(Arrays.stream(GuiStyle.values()).filter(style -> style != GuiStyle.NONE).toList())
                .withInitialValue(Config.GUI_STYLE.get())
                .create(width / 2 - 150, height / 2 - 35, 300, 20, Component.literal("Panel style"),
                        (button, value) -> Config.setGuiStyle(value)));
        addRenderableWidget(CycleButton.<GuiTextStyle>builder(style -> Component.literal(style.name()))
                .withValues(GuiTextStyle.values()).withInitialValue(Config.GUI_TEXT_STYLE.get())
                .create(width / 2 - 150, height / 2 - 10, 300, 20, Component.literal("Text style"),
                        (button, value) -> Config.setGuiTextStyle(value)));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(width / 2 - 100, height / 2 + 30, 200, 20).build());
    }

    @Override
    public void onClose() {
        Config.CLIENT_CONFIG.save();
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        graphics.drawCenteredString(font, title, width / 2, 30, 0xffffff);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
