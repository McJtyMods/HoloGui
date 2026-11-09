package mcjty.hologui.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import mcjty.hologui.api.IHoloGuiEntity;
import mcjty.hologui.api.components.ITextEntry;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiRenderTools;
import mcjty.hologui.gui.TextEntryScreen;
import net.minecraft.client.Minecraft;
import mcjty.lib.client.RenderBufferSource;
import net.minecraft.world.entity.player.Player;

public class HoloTextEntry extends AbstractHoloComponent<ITextEntry> implements ITextEntry {

    private Supplier<String> text = () -> "";
    private int maxLength = 1024;
    private ChangeEvent changeEvent;

    HoloTextEntry(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    @Override
    public ITextEntry text(Supplier<String> text) {
        this.text = text;
        return this;
    }

    @Override
    public ITextEntry maxLength(int length) {
        maxLength = Math.max(1, Math.min(2048, length));
        return this;
    }

    @Override
    public ITextEntry changed(ChangeEvent event) {
        changeEvent = event;
        return this;
    }

    @Override
    public String getText() {
        return text.get();
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void commit(Player player, IHoloGuiEntity entity, String text) {
        if (changeEvent != null) {
            // Preserve line breaks, reject control/format codes and bound untrusted input.
            StringBuilder clean = new StringBuilder();
            text.codePoints().filter(c -> c == '\n' || c >= 32 && c != 127 && c != 167)
                    .limit(maxLength).forEach(clean::appendCodePoint);
            changeEvent.changed(player, entity, clean.toString());
        }
    }

    @Override
    public void hit(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (getName() != null && entity instanceof HoloGuiEntity holo) {
            holo.beginTextEntry(player, getName());
        }
    }

    @Override

    public void hitClient(Player player, IHoloGuiEntity entity, double cursorX, double cursorY) {
        if (getName() != null && entity instanceof HoloGuiEntity holo) {
            Minecraft.getInstance().gui.setScreen(new TextEntryScreen(holo, this));
        }
    }

    @Override
    public void render(PoseStack poses, RenderBufferSource buffer, Player player, IHoloGuiEntity holo, double cursorX, double cursorY) {
        HoloGuiRenderTools.renderBorder(poses, buffer, x, y, w, h, 75, 225, 255, 220);
        String[] lines = getText().split("\\n", -1);
        int maxChars = Math.max(1, (int) (w * 10 / 3));
        for (int line = 0; line < Math.min(lines.length, (int) (h / .55)); line++) {
            String value = lines[line];
            HoloGuiRenderTools.renderText(poses, buffer, x + .15, y + .15 + line * .55,
                    value.substring(0, Math.min(value.length(), maxChars)), 0xd9f9ff, .5f);
        }
        if (getText().isEmpty()) {
            HoloGuiRenderTools.renderText(poses, buffer, x + .15, y + .15, "Click to type...", 0x83afba, .5f);
        }
    }
}
