package net.snowteb.warriorcats_events;

import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.Objective;

@Mod.EventBusSubscriber
public class ScoreboardInitializer {

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
            Scoreboard scoreboard = serverWorld.getServer().getScoreboard();

            Objective objective = scoreboard.getObjective("Lives");
            if (objective == null) {
                scoreboard.addObjective("Lives", ObjectiveCriteria.DUMMY,
                        net.minecraft.network.chat.Component.literal("Leader's Lives"),
                        ObjectiveCriteria.RenderType.INTEGER
                );
                System.out.println("[WarriorCatsEvents] Scoreboard 'Lives' created.");
            }
        }
    }
}
