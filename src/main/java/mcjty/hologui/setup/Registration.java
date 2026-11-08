package mcjty.hologui.setup;

import java.util.function.Supplier;
import mcjty.hologui.HoloGui;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, HoloGui.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HoloGui.MODID);
    public static final Supplier<EntityType<HoloGuiEntity>> HOLOGUI_ENTITY_TYPE = ENTITIES.register("hologui", Registration::createHoloGuiType);

    public static void init(IEventBus bus) {
        SOUNDS.register(bus);
        ENTITIES.register(bus);
        HoloGuiSounds.init();   // Class loading
    }

    private static EntityType<HoloGuiEntity> createHoloGuiType() {
        return EntityType.Builder.of(HoloGuiEntity::new, MobCategory.MISC)
                .sized(1, 1)
                .setShouldReceiveVelocityUpdates(false)
                .noSummon()
                .fireImmune()
                .build("hologui");
    }
}
