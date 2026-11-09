package mcjty.hologui.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import mcjty.hologui.api.components.ITextEntry;
import mcjty.hologui.network.PacketTextEntry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Keyboard focus for an in-world text field. The world and hologui remain visible. */
public class TextEntryScreen extends Screen {

    private final HoloGuiEntity holo;
    private final ITextEntry field;
    private final String guiId;
    private final String tag;
    private MultiLineEditBox editor;
    private boolean committed;
    private String draft;
    private int focusTicks;

    public TextEntryScreen(HoloGuiEntity holo, ITextEntry field) {
        super(Component.literal("Holographic text entry"));
        this.holo = holo;
        this.field = field;
        guiId = holo.getGuiId();
        tag = holo.getTag();
        draft = field.getText();
    }

    @Override
    protected void init() {
        if (editor != null) {
            draft = editor.getValue();
        }
        int boxWidth = Math.min(380, width - 40);
        editor = MultiLineEditBox.builder().setX((width - boxWidth) / 2).setY(height / 2 - 55)
                .setPlaceholder(Component.literal("Enter display text")).build(font, boxWidth, 110, title);
        editor.setCharacterLimit(field.getMaxLength());
        editor.setValue(draft);
        addRenderableWidget(editor);
        setInitialFocus(editor);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(font, "HOLOGRAPHIC TEXT", width / 2, height / 2 - 75, 0xff76eaff);
        graphics.centeredText(font, "Esc or click outside to finish", width / 2, height / 2 + 65, 0xffd9f9ff);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!editor.isMouseOver(event.x(), event.y())) {
            onClose();
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void tick() {
        if (++focusTicks % 100 == 0) {
            ClientPacketDistributor.sendToServer(new PacketTextEntry(holo.getId(), guiId, tag, field.getName(), "", false));
        }
        if (!holo.isAlive() || !holo.getGuiId().equals(guiId) || !holo.getTag().equals(tag)) {
            onClose();
        }
    }

    @Override
    public void removed() {
        if (!committed && editor != null && minecraft.getConnection() != null) {
            committed = true;
            ClientPacketDistributor.sendToServer(new PacketTextEntry(holo.getId(), guiId, tag, field.getName(), editor.getValue()));
        }
    }
}
