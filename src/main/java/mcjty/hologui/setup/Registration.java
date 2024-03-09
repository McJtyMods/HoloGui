package mcjty.hologui.setup;


import mcjty.hologui.HoloGui;
import mcjty.hologui.gui.HoloGuiEntity;
import mcjty.hologui.gui.HoloGuiSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fml.common.Mod;
import net.neoforged.neoforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

@Mod.EventBusSubscriber
public class Registration {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HoloGui.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HoloGui.MODID);
    public static final Supplier<EntityType<HoloGuiEntity>> HOLOGUI_ENTITY_TYPE = ENTITIES.register("hologui", Registration::createHoloGuiType);

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
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
