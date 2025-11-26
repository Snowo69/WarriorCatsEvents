package net.snowteb.warriorcats_events.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.SyncSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealth;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.thirst.PlayerThirst;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID)
public class ModEvents2 {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {

            if(!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()) {
                PlayerThirstProvider provider = new PlayerThirstProvider();
                provider.getOrCreateThirst();
                event.addCapability(new ResourceLocation(WarriorCatsEvents.MODID, "properties"), provider);

                event.addCapability(new ResourceLocation(WarriorCatsEvents.MODID, "skill_data"),
                        new PlayerSkillProvider());

                event.addCapability(new ResourceLocation(WarriorCatsEvents.MODID, "stealth_mode"),
                        new PlayerStealthProvider());

            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {

        event.getEntity().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
            if (event.isWasDeath()) {
                newStore.reset();
            } else {
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                    newStore.copyFrom(oldStore);
                });
            }

            if (event.getEntity() instanceof ServerPlayer player) {
                ModPackets.sendToPlayer(
                        new ThirstDataSyncStCPacket(newStore.getThirst()),
                        player
                );
            }
        });


        event.getEntity().getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(newStore -> {
            event.getOriginal().getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(oldStore -> {
                newStore.copyFrom(oldStore);

                if (event.getEntity() instanceof ServerPlayer player) {
                    newStore.sync(player);
                }
            });
        });

        event.getEntity().getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(newStore -> {
            event.getOriginal().getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(oldStore -> {
                newStore.copyFrom(oldStore);

                if (event.getEntity() instanceof ServerPlayer player) {
                    ModPackets.sendToPlayer(
                            new SyncSkillDataPacket(newStore.getSpeedLevel(), newStore.getHPLevel()),
                            player
                    );
                }
            });
        });

    }




    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerThirst.class);
        event.register(PlayerSkill.class);
        event.register(PlayerStealth.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {

            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {

                if (thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.00042) {
                    int oldThirst = thirst.getThirst();
                    thirst.subThirst(1);
                    if (oldThirst != thirst.getThirst()) {
                        ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), ((ServerPlayer) event.player));
                    }
                }

                if (thirst.getThirst() <= 0) {
                    thirst.tick();

                    if (thirst.getThirstDamageTimer() >= 80) {
                        event.player.getFoodData().setSaturation(0);
                        event.player.hurt(event.player.damageSources().starve(), 2.0f);
                        event.player.displayClientMessage(Component.literal("You are dehydrated!" ).withStyle(ChatFormatting.RED), true);

                        thirst.resetThirstDamageTimer();
                    }

                } else {
                    thirst.resetThirstDamageTimer();
                }
            });



            event.player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {


                    if (cap.isStealthOn() && cap.isUnlocked()) {
                        ServerLevel level = (ServerLevel) event.player.level();

                        level.sendParticles(
                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                event.player.getX(),
                                event.player.getY() + 0.1,
                                event.player.getZ(),
                                2, 0.1, 0, 0.1, 0.01
                        );
                    }


            });





        }
    }



    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), player);
                });

                player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {
                    cap.sync(player);
                });
            }
        }
    }

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
