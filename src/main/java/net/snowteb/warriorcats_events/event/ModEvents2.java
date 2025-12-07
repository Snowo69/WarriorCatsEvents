package net.snowteb.warriorcats_events.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.SyncSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealth;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.thirst.PlayerThirst;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import net.snowteb.warriorcats_events.util.ModAttributes;

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
    public static void onPlayerDead(PlayerEvent.Clone event) {

        event.getEntity().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
            if (event.isWasDeath()) {
                newStore.reset();
            } else {
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                    newStore.copyFrom(oldStore);
                });
            }

            if (event.getEntity() instanceof ServerPlayer player) {
                ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(newStore.getThirst()), player);
            }
        });



        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getEntity().getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(newStore -> {
                event.getOriginal().getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(oldStore -> {
                    newStore.copyFrom(oldStore);

                    if (event.getEntity() instanceof ServerPlayer player) {
                        newStore.sync(player);
                    }
                });
            });
        }

        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getEntity().getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(newStore -> {
                event.getOriginal().getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(oldStore -> {
                    newStore.copyFrom(oldStore);

                    if (event.getEntity() instanceof ServerPlayer player) {
                        var speedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
                        if (speedAttr != null) {
                            speedAttr.removeModifier(PlayerSkill.SPEED_SKILL_UUID);
                            double bonus = 0.025 * newStore.getSpeedLevel();
                            if (bonus > 0) {
                                speedAttr.addPermanentModifier(
                                        new AttributeModifier(
                                                PlayerSkill.SPEED_SKILL_UUID,
                                                "skill_speed_bonus",
                                                bonus,
                                                AttributeModifier.Operation.MULTIPLY_TOTAL
                                        )
                                );
                            }
                        }
                        var hpAttr = player.getAttribute(Attributes.MAX_HEALTH);
                        if (hpAttr != null) {
                            hpAttr.removeModifier(PlayerSkill.HP_SKILL_UUID);
                            double bonus = 0.1 * newStore.getHPLevel();
                            if (bonus > 0) {
                                hpAttr.addPermanentModifier(
                                        new AttributeModifier(
                                                PlayerSkill.HP_SKILL_UUID,
                                                "skill_hp_bonus",
                                                bonus,
                                                AttributeModifier.Operation.MULTIPLY_TOTAL
                                        )
                                );
                            }
                        }
                        var dmgAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
                        if (dmgAttr != null) {
                            dmgAttr.removeModifier(PlayerSkill.DMG_SKILL_UUID);
                            double bonus = 0.12 * newStore.getDMGLevel();
                            if (bonus > 0) {
                                dmgAttr.addPermanentModifier(
                                        new AttributeModifier(
                                                PlayerSkill.DMG_SKILL_UUID,
                                                "skill_dmg_bonus",
                                                bonus,
                                                AttributeModifier.Operation.ADDITION
                                        )
                                );
                            }
                        }
                        var jumpAttr = player.getAttribute(ModAttributes.PLAYER_JUMP.get());
                        if (jumpAttr != null) {
                            jumpAttr.removeModifier(PlayerSkill.JUMP_SKILL_UUID);
                            double bonus = 0.093 * newStore.getJumpLevel();
                            if (bonus > 0) {
                                jumpAttr.addPermanentModifier(
                                        new AttributeModifier(
                                                PlayerSkill.JUMP_SKILL_UUID,
                                                "skill_jump_bonus",
                                                bonus,
                                                AttributeModifier.Operation.ADDITION
                                        )
                                );
                            }
                        }
                        var armorAttr = player.getAttribute(Attributes.ARMOR);
                        if (armorAttr != null) {
                            armorAttr.removeModifier(PlayerSkill.ARMOR_SKILL_UUID);
                            double bonus = 3.5 * newStore.getArmorLevel();
                            if (bonus > 0) {
                                armorAttr.addPermanentModifier(
                                        new AttributeModifier(
                                                PlayerSkill.ARMOR_SKILL_UUID,
                                                "skill_armor_bonus",
                                                bonus,
                                                AttributeModifier.Operation.ADDITION
                                        )
                                );
                            }
                        }

                        ModPackets.sendToPlayer(
                                new SyncSkillDataPacket(newStore.getSpeedLevel(), newStore.getHPLevel(),
                                        newStore.getDMGLevel(), newStore.getJumpLevel(), newStore.getArmorLevel()),
                                player
                        );
                    }


                });


            });
        }


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

                if (thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.00028 && !(event.player.isCreative() || event.player.isSpectator())) {
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


                    if (cap.isStealthOn() && cap.isUnlocked() && cap.isOn()) {
                        ServerLevel level = (ServerLevel) event.player.level();

                        level.sendParticles(
                                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                event.player.getX(),
                                event.player.getY() + 0.1,
                                event.player.getZ(),
                                2, 0.1, 0, 0.1, 0.002
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
            }
        }
    }


    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        double extra = player.getAttribute(ModAttributes.PLAYER_JUMP.get()).getValue();

        if (extra > 0) {
            player.setDeltaMovement(
                    player.getDeltaMovement().x,
                    player.getDeltaMovement().y + extra,
                    player.getDeltaMovement().z
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;

        if (data.contains(Player.PERSISTED_NBT_TAG)) {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        } else {
            persistent = new CompoundTag();
            data.put(Player.PERSISTED_NBT_TAG, persistent);
        }

        if (persistent.getBoolean("warriorcats_events.starting_items")) {
            return;
        }

        persistent.putBoolean("warriorcats_events.starting_items", true);
        player.getInventory().add(new ItemStack(ModItems.WARRIORS_GUIDE.get()));
        player.getInventory().add(new ItemStack(ModItems.CLAWS.get()));
        player.sendSystemMessage(Component.literal("You have received your own [Claws] and [A Warrior's Guide]!").withStyle(ChatFormatting.YELLOW));
        player.sendSystemMessage(Component.literal("Get support and stay tuned for mod updates: ").append(
                Component.literal("[Discord]")
                        .withStyle(style -> style
                                .withColor(0x579dff)
                                .withUnderlined(true)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/SkYvZr9DBb"))
                        )
        ));
    }


}
