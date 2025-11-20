package net.snowteb.warriorcats_events.util;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.ModEntities;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntitySpawns {
    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntities.MOUSE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                (entityType, level, reason, pos, random) ->
                        level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)
                                && level.getRawBrightness(pos, 0) > 8,
                SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
