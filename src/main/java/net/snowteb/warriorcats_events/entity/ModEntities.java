package net.snowteb.warriorcats_events.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WarriorCatsEvents.MODID);




    public static final RegistryObject<EntityType<MouseEntity>> MOUSE =
            ENTITY_TYPES.register("mouse",
                    () -> EntityType.Builder.of(MouseEntity::new, MobCategory.CREATURE)
                            .sized(0.7f,0.7f)
                            .build(new ResourceLocation(WarriorCatsEvents.MODID, "mouse").toString()));
    public static final RegistryObject<EntityType<SquirrelEntity>> SQUIRREL =
            ENTITY_TYPES.register("squirrel",
                    () -> EntityType.Builder.of(SquirrelEntity::new, MobCategory.CREATURE)
                            .sized(0.7f,0.7f)
                            .build(new ResourceLocation(WarriorCatsEvents.MODID, "squirrel").toString()));
    public static final RegistryObject<EntityType<WCatEntity>> WCAT =
            ENTITY_TYPES.register("warrior_cat",
                    () -> EntityType.Builder.of(WCatEntity::new, MobCategory.CREATURE)
                            .sized(0.9f,0.9f)
                            .build(new ResourceLocation(WarriorCatsEvents.MODID, "warrior_cat").toString()));

    public static final RegistryObject<EntityType<PigeonEntity>> PIGEON =
            ENTITY_TYPES.register("pigeon",
                    () -> EntityType.Builder.of(PigeonEntity::new, MobCategory.CREATURE)
                            .sized(0.9f,0.9f)
                            .build(new ResourceLocation(WarriorCatsEvents.MODID, "pigeon").toString()));

    public static final RegistryObject<EntityType<BadgerEntity>> BADGER =
            ENTITY_TYPES.register("badger",
                    () -> EntityType.Builder.of(BadgerEntity::new, MobCategory.CREATURE)
                            .sized(1f,1f)
                            .build(new ResourceLocation(WarriorCatsEvents.MODID, "badger").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
