package mcjty.hologui.setup;

import java.util.function.Supplier;
import mcjty.hologui.HoloGui;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiSounds;
import mcjty.lib.setup.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Registration {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, HoloGui.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, HoloGui.MODID);
    public static final Supplier<EntityType<HoloGuiEntity>> HOLOGUI_ENTITY_TYPE = ENTITIES.register("hologui", Registration::createHoloGuiType);

    public static void init() {
        HoloGuiSounds.init();
        SOUNDS.register();
        ENTITIES.register();
    }

    private static EntityType<HoloGuiEntity> createHoloGuiType() {
        return EntityType.Builder.of(HoloGuiEntity::new, MobCategory.MISC)
                .sized(1, 1)
                .noSummon()
                .fireImmune()
                .build("hologui");
    }
}
