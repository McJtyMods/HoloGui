package mcjty.hologui.gui;

import mcjty.hologui.HoloGui;
import mcjty.hologui.api.components.IIntegerSlider;
import mcjty.hologui.network.PacketSliderInput;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** A held attack button drags an in-world slider while the player's gaze moves along its track. */
@EventBusSubscriber(modid = HoloGui.MODID, value = Dist.CLIENT)
public class SliderInput {

    private static HoloGuiEntity holo;
    private static IIntegerSlider slider;
    private static String guiId;
    private static String tag;
    private static int ticks;
    private static double lastX = Double.NaN;

    public static void begin(HoloGuiEntity entity, IIntegerSlider component) {
        holo = entity;
        slider = component;
        guiId = entity.getGuiId();
        tag = entity.getTag();
        lastX = Double.NaN;
        ticks = 0;
    }

    @SubscribeEvent
    public static void blockAttack(PlayerInteractEvent.LeftClickBlock event) {
        if (holo != null && event.getLevel().isClientSide) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post event) {
        if (holo == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        boolean finish = mc.player == null || mc.screen != null || !mc.options.keyAttack.isDown()
                || !holo.isAlive() || mc.player.distanceToSqr(holo) >= 64 || !guiId.equals(holo.getGuiId()) || !tag.equals(holo.getTag());
        if (finish || ++ticks % 2 == 0) {
            if (mc.getConnection() != null && mc.player != null && (finish || !Double.isFinite(lastX) || Math.abs(holo.getCursorX() - lastX) > .01 || ticks % 20 == 0)) {
                PacketDistributor.sendToServer(new PacketSliderInput(holo.getId(), guiId, tag, slider.getName(), finish, mc.player.getYRot(), mc.player.getXRot()));
                lastX = holo.getCursorX();
            }
        }
        if (finish) {
            holo = null;
            slider = null;
        }
    }
}
