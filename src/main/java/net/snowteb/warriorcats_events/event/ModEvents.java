package net.snowteb.warriorcats_events.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.ModEntities;
//import net.snowteb.warriorcats_events.entity.client.VanillaWCatModel;
import net.snowteb.warriorcats_events.entity.custom.*;
import net.snowteb.warriorcats_events.util.ModAttributes;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    /**
     * Valid spawns registry
     */
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(
                    ModEntities.WCAT.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
                                    && level.getFluidState(pos).isEmpty()
                                    && level.getFluidState(pos.below()).isEmpty()

            );
            SpawnPlacements.register(
                    ModEntities.SQUIRREL.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
                                    && level.getFluidState(pos).isEmpty()
                                    && level.getFluidState(pos.below()).isEmpty()

            );
            SpawnPlacements.register(
                    ModEntities.MOUSE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
                                    && level.getFluidState(pos).isEmpty()
                                    && level.getFluidState(pos.below()).isEmpty()

            );
            SpawnPlacements.register(
                    ModEntities.PIGEON.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
                                    && level.getFluidState(pos).isEmpty()
                                    && level.getFluidState(pos.below()).isEmpty()

            );
            SpawnPlacements.register(
                    ModEntities.BADGER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
                                    && level.getFluidState(pos).isEmpty()
                                    && level.getFluidState(pos.below()).isEmpty()

            );

        });


    }

//    /**
//     * Adds the custom font.
//     */
//    @SubscribeEvent
//    public static void addPackFinder(AddPackFindersEvent event) {
//        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
//
//        var modFileInfo = ModList.get().getModFileById("warriorcats_events");
//        if (modFileInfo == null) return;
//
//        var modFile = modFileInfo.getFile();
//
//        Path packPath = modFile.getFilePath()
//                .resolve("resourcepacks/requiem_text_roman_font");
//
//        event.addRepositorySource(consumer -> {
//            consumer.accept(Pack.create(
//                    "requiem_text_roman_font",
//                    Component.literal("Requiem Roman Font"),
//                    false,
//                    id -> new PathPackResources(
//                            id,
//                            true,
//                            packPath
//                    ),
//                    new Pack.Info(
//                            Component.literal("Optional Requiem Roman Font"),
//                            15,
//                            15,
//                            FeatureFlagSet.of(),
//                            false
//                    ),
//                    PackType.CLIENT_RESOURCES,
//                    Pack.Position.TOP,
//                    false,
//                    PackSource.BUILT_IN
//            ));
//        });
//    }





    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOUSE.get(), MouseEntity.setAttributes().build());
        event.put(ModEntities.SQUIRREL.get(), SquirrelEntity.setAttributes().build());
        event.put(ModEntities.WCAT.get(), WCatEntity.setAttributes().build());
        event.put(ModEntities.PIGEON.get(), PigeonEntity.setAttributes().build());
        event.put(ModEntities.BADGER.get(), BadgerEntity.setAttributes().build());
       // event.put(ModEntities.VANILLAWCAT.get(), VanillaWCatEntity.setAttributes().build());

    }

    /**
     * This adds the custom attribute to the player.
     */
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.PLAYER_JUMP.get());
    }


}