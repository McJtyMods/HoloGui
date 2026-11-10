package mcjty.hologui.gui;

import mcjty.hologui.api.components.ITextEntry;
import mcjty.hologui.network.PacketTextEntry;
import mcjty.lib.network.ClientNetworking;
import net.minecraft.client.gui.GuiGraphics;
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
        editor = new MultiLineEditBox(font, (width - boxWidth) / 2, height / 2 - 55, boxWidth, 110,
                Component.literal("Enter display text"), title);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, "HOLOGRAPHIC TEXT", width / 2, height / 2 - 75, 0x76eaff);
        graphics.drawCenteredString(font, "Esc or click outside to finish", width / 2, height / 2 + 65, 0xd9f9ff);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!editor.isMouseOver(mouseX, mouseY)) {
            onClose();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        if (++focusTicks % 100 == 0) {
            ClientNetworking.sendToServer(new PacketTextEntry(holo.getId(), guiId, tag, field.getName(), "", false));
        }
        if (!holo.isAlive() || !holo.getGuiId().equals(guiId) || !holo.getTag().equals(tag)) {
            onClose();
        }
    }

    @Override
    public void removed() {
        if (!committed && editor != null && minecraft.getConnection() != null) {
            committed = true;
            ClientNetworking.sendToServer(new PacketTextEntry(holo.getId(), guiId, tag, field.getName(), editor.getValue()));
        }
    }
}
