package net.snowteb.warriorcats_events.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.resource.PathPackResources;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.ModEntities;
//import net.snowteb.warriorcats_events.entity.client.VanillaWCatModel;
import net.snowteb.warriorcats_events.entity.custom.*;
import net.snowteb.warriorcats_events.util.ModAttributes;

import java.nio.file.Path;

import static net.minecraft.world.flag.FeatureFlags.VANILLA;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(
                    ModEntities.WCAT.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
            );
            SpawnPlacements.register(
                    ModEntities.SQUIRREL.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
            );
            SpawnPlacements.register(
                    ModEntities.MOUSE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
            );
            SpawnPlacements.register(
                    ModEntities.PIGEON.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
            );
            SpawnPlacements.register(
                    ModEntities.BADGER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (entityType, level, spawnReason, pos, random) ->
                            level.getBlockState(pos.below()).isSolid()
            );

        });


    }


    @SubscribeEvent
    public static void addPackFinder(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

        var modFileInfo = ModList.get().getModFileById("warriorcats_events");
        if (modFileInfo == null) return;
        var modFile = modFileInfo.getFile();
        Path root = modFile.getFilePath();

        Path packPath = root.resolve("resourcepacks").resolve("Requiem Text Roman font");

        event.addRepositorySource(sink -> {
            sink.accept(Pack.create(
                    "requiem_text_roman_font",
                    Component.literal("Requiem Text Roman font"),
                    false,
                    (profile) -> new PathPackResources("Requiem Text Roman font", true, packPath),       // <--- Carga del pack
                    new Pack.Info(
                            Component.literal("Optional Requiem Roman Font"),
                            15,
                            15,
                            FeatureFlagSet.of(),
                            false
                    ),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    false,
                    PackSource.BUILT_IN
            ));
        });
    }


    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOUSE.get(), MouseEntity.setAttributes().build());
        event.put(ModEntities.SQUIRREL.get(), SquirrelEntity.setAttributes().build());
        event.put(ModEntities.WCAT.get(), WCatEntity.setAttributes().build());
        event.put(ModEntities.PIGEON.get(), PigeonEntity.setAttributes().build());
        event.put(ModEntities.BADGER.get(), BadgerEntity.setAttributes().build());
       // event.put(ModEntities.VANILLAWCAT.get(), VanillaWCatEntity.setAttributes().build());

    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.PLAYER_JUMP.get());
    }


}