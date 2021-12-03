package mcjty.hologui;

import mcjty.hologui.gui.HoloGuiEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HoloGui.MODID);

    public static void register() {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final RegistryObject<EntityType<HoloGuiEntity>> HOLOGUI_ENTITY_TYPE = ENTITIES.register("hologui", ModEntities::createHoloGuiType);

    private static EntityType<HoloGuiEntity> createHoloGuiType() {
        return EntityType.Builder.of(HoloGuiEntity::new, EntityClassification.MISC)
                .sized(1, 1)
                .setShouldReceiveVelocityUpdates(false)
                .build("hologui");
    }
}
