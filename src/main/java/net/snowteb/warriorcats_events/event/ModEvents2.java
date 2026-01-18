package net.snowteb.warriorcats_events.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.commands.GetClanDataCommand;
import net.snowteb.warriorcats_events.commands.OpenClanDataScreenCommand;
import net.snowteb.warriorcats_events.commands.ResetClanDataCommand;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.OpenClanSetupScreenPacket;
import net.snowteb.warriorcats_events.network.packet.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.SyncSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealth;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.thirst.PlayerThirst;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import net.snowteb.warriorcats_events.util.ModAttributes;

import java.util.*;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID)
public class ModEvents2 {
    private int conditionDropedItems = 0;

    private static final List<Task> tasks = new ArrayList<>();

    public static void schedule(int ticksDelay, Runnable action) {
        tasks.add(new Task(ticksDelay, action));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            Task task = it.next();
            task.ticks--;

            if (task.ticks <= 0) {
                task.action.run();
                it.remove();
            }
        }
    }

    private static boolean isDiamond(ItemStack stack) {
        return stack.getItem() == Items.DIAMOND;
    }
    private static boolean isEmerald(ItemStack stack) {
        return stack.getItem() == Items.EMERALD;
    }


    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.level.isClientSide) return;

        boolean isNightTime = event.level.isNight();

        if (isNightTime) {

            if (event.level.getGameTime() % 200 == 0) {
                List<ItemEntity> items = event.level.getEntitiesOfClass(
                        ItemEntity.class,
                        new AABB(
                                -30000000, event.level.getMinBuildHeight(), -30000000,
                                30000000, event.level.getMaxBuildHeight(), 30000000
                        ),
                        e -> !e.isRemoved() && !e.getItem().isEmpty()
                );


                for (ItemEntity item1 : items) {
                    ItemStack potentialDiamond = item1.getItem();

                    if (!isDiamond(potentialDiamond)) continue;

                    for (ItemEntity item2 : items) {
                        if (item1 == item2) continue;

                        ItemStack potentialEmerald = item2.getItem();

                        if (!isEmerald(potentialEmerald)) continue;

                        double dist = item1.distanceTo(item2);

                        if (dist >= 1.0) continue;

                        if (potentialDiamond.getCount() <= 0 || potentialEmerald.getCount() <= 0) continue;

                        potentialDiamond.shrink(1);
                        potentialEmerald.shrink(1);

                        ItemStack ritualStack = new ItemStack(ModItems.STRANGE_SHINY_STONE.get());
                        ItemEntity ritualItem = new ItemEntity(event.level, item2.getX(), item2.getY(), item2.getZ(), ritualStack);

                        event.level.addFreshEntity(ritualItem);

                        ((ServerLevel) event.level).sendParticles(ParticleTypes.GLOW_SQUID_INK,
                                ritualItem.getX(), ritualItem.getY(), ritualItem.getZ(),
                                15, 0.2f, 0.2f, 0.2f, 0.2f);

                        event.level.playSound(null, ritualItem.blockPosition(),
                                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.AMBIENT,
                                1.0f, 1.3f);

                        break;
                    }
                }
            }
        }
    }

    private static class Task {
        int ticks;
        final Runnable action;

        Task(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
        }
    }


    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        OpenClanDataScreenCommand.register(event.getDispatcher());
        GetClanDataCommand.register(event.getDispatcher());
        ResetClanDataCommand.register(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

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

            if (!event.getObject().getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).isPresent()) {
                PlayerClanDataProvider provider = new PlayerClanDataProvider();
                provider.getOrCreateClanData();
                event.addCapability(new ResourceLocation(WarriorCatsEvents.MODID, "clan_data"), provider);
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerDead(PlayerEvent.Clone event) {

        /**
         * When the player dies, reset the thirst back to max and sync it.
         */
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


        /**
         * If the player dies, keep the stealth data as it was. Unlocked if it was unlocked, on if it was on, etc.
         */
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
            event.getEntity().getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(newStore -> {
                event.getOriginal().getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(oldStore -> {
                    newStore.copyFrom(oldStore);

                    if (event.getEntity() instanceof ServerPlayer player) {
                        ModPackets.sendToPlayer(new S2CSyncClanDataPacket(newStore), player);
                    }
                });
            });
        }

        /**
         * If the player dies, keep the skill data as it was.
         * Then manually apply every attribute again. 1 by 1.
         * Then sync it.
         */
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

            event.player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(PlayerClanData::tick);

            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {

                /**
                 * Every tick, a 0.000357 chance of decreasing thirst.
                 * Which means, on average every 2-3 minutes
                 * Then sync it.
                 */
                if (thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.000359 && !(event.player.isCreative() || event.player.isSpectator())) {
                    int oldThirst = thirst.getThirst();
                    thirst.subThirst(1);
                    if (oldThirst != thirst.getThirst()) {
                        ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), ((ServerPlayer) event.player));
                    }
                }

                /**
                 * If the thirst reaches zero, then hurt the player every 80 ticks, and set saturation to zero.
                 */
                if (thirst.getThirst() <= 0) {
                    thirst.tick();

                    if (thirst.getThirstDamageTimer() >= 60) {
                        event.player.getFoodData().setSaturation(0);
                        event.player.hurt(event.player.damageSources().starve(), 3.0f);
                        event.player.displayClientMessage(Component.literal("You are dehydrated!" ).withStyle(ChatFormatting.RED), true);

                        thirst.resetThirstDamageTimer();
                    }

                } else {
                    thirst.resetThirstDamageTimer();
                }
            });



            event.player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

                if (cap.isStealthOn() && event.player.tickCount % 2 == 0) {
                    event.player.setInvisible(true);
                }

                /**
                 * If Stealth is active, and its unlocked, and its on:
                 * Then send a particle every 3 ticks.
                 */
                    if (cap.isStealthOn() && cap.isUnlocked() && cap.isOn()) {
                        ServerLevel level = (ServerLevel) event.player.level();

                        if (event.player.tickCount % 3 == 0) {
                            if (event.player.onGround()) {
                                level.sendParticles(
                                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                        event.player.getX(),
                                        event.player.getY() + 0.1,
                                        event.player.getZ(),
                                        2, 0.1, 0, 0.1, 0.002
                                );
                            }

                        }

                    }


            });

            event.player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(cap -> {
                if (!cap.isLeaping()) return;

                if (event.player.tickCount % 2 == 0) {
                    ((ServerLevel) event.player.level()).sendParticles(ParticleTypes.INSTANT_EFFECT,
                            event.player.getX(), event.player.getY(), event.player.getZ(),
                            1,
                            0,0,0, 0);
                }

                int damagePowerMultiplier = cap.getLeapPower()/100;

                AABB hitbox = event.player.getBoundingBox().inflate(0.6D);

                List<LivingEntity> targets = event.player.level().getEntitiesOfClass(
                        LivingEntity.class,
                        hitbox,
                        e -> e != event.player && e.isAlive()
                );

                if (!targets.isEmpty()) {
                    LivingEntity target = targets.stream()
                            .min(Comparator.comparingDouble(e ->
                                    e.distanceToSqr(event.player)))
                            .orElse(null);

                    PlayerClanData.Age morphAge = event.player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMorphAge).orElse(PlayerClanData.Age.ADULT);

                    float finalMultiplier = switch (morphAge) {
                        case KIT -> 0.33f;
                        case APPRENTICE -> 0.67f;
                        case ADULT -> 1.0f;
                    };

                    float damage = ((float) (5.0F + ((float) cap.getDMGLevel() /1.25)*damagePowerMultiplier))*finalMultiplier;
                    if (event.player.hasLineOfSight(target)) {
                        if (!(target instanceof TamableAnimal cat && cat.isTame() && cat.getOwner() == event.player)) {
                            BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK,
                                    Blocks.REDSTONE_BLOCK.defaultBlockState());

                            if (event.player.getRandom().nextFloat() < 0.2 && damagePowerMultiplier > 0.7) {
                                target.hurt(event.player.damageSources().playerAttack(event.player), damage * 1.5f);
                                ((ServerLevel) event.player.level()).sendParticles(ParticleTypes.CRIT,
                                        target.getX(), target.getY(), target.getZ(),
                                        30,
                                        0.2f,0.2f,0.2f, 0.2f);
                                ((ServerLevel) event.player.level()).sendParticles(particle,
                                        target.getX(), target.getY(), target.getZ(),
                                        30,
                                        0.1,0.2,0.1, 0.1);
                                event.player.level().playSound(null, event.player.blockPosition(), SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.7f, 1f);
                                event.player.displayClientMessage(Component.literal("Critical hit!").withStyle(ChatFormatting.GRAY), true);
                            } else {
                                target.hurt(event.player.damageSources().playerAttack(event.player), damage);
                            }

                            if (target.isAlive()) event.player.setLastHurtMob(target);

                            event.player.level().playSound(null, event.player.blockPosition(),SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS);

                            ((ServerLevel) event.player.level()).sendParticles(particle,
                                    target.getX(), target.getY(), target.getZ(),
                                    10,
                                    0.1,0.2,0.1, 0.1);
                            ((ServerLevel) event.player.level()).sendParticles(ParticleTypes.CRIT,
                                    target.getX(), target.getY(), target.getZ(),
                                    10,
                                    0.2f,0.2f,0.2f, 0.2f);
                        }
                    }


                    cap.setLeaping(false);
                    cap.setLeapPower(0);
                    event.player.setDeltaMovement(Vec3.ZERO);
                }

                if (event.player.onGround()) {
                    cap.setLeaping(false);
                    cap.setLeapPower(0);
                }
            });


        }

/*
        if (event.player.tickCount % 20 == 0) {
            if (event.player.level().isClientSide()) {
                double speed = event.player.getDeltaMovement().length();
                event.player.sendSystemMessage(Component.literal("Speed Client: " + speed));
            }
        */


    }


    /**
     * When the player joins, sync its thirst data and its stealth data.
     */
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

    /**
     * This is useless and i should remove it but i might need it one day
     */
//    @SubscribeEvent
//    public static void onWorldLoad(LevelEvent.Load event) {
//        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel serverWorld) {
//            Scoreboard scoreboard = serverWorld.getServer().getScoreboard();
//
//            Objective objective = scoreboard.getObjective("Lives");
//            if (objective == null) {
//                scoreboard.addObjective("Lives", ObjectiveCriteria.DUMMY,
//                        net.minecraft.network.chat.Component.literal("Leader's Lives"),
//                        ObjectiveCriteria.RenderType.INTEGER
//                );
//            }
//        }
//    }

    /**
     * Every time and entity jumps, if it is a player, then modify its jump depending on the custom Attribute.
     */
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


    /**
     * When the player joins for the first time. Give it items and send a message.
     */
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;



        if (player instanceof ServerPlayer sPlayer) {
            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), sPlayer);
            });

        }

        if (data.contains(Player.PERSISTED_NBT_TAG)) {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        } else {
            persistent = new CompoundTag();
            data.put(Player.PERSISTED_NBT_TAG, persistent);
        }

        if (persistent.getBoolean("warriorcats_events.starting_items")) {
            return;
        }


        if (player instanceof ServerPlayer sPlayer) {
            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), sPlayer);
                    ModPackets.sendToPlayer(new OpenClanSetupScreenPacket(), sPlayer);
                persistent.putBoolean("warriorcats_events.starting_items", true);
            });
        }
//        player.getInventory().add(new ItemStack(ModItems.WARRIORS_GUIDE.get()));
//        player.getInventory().add(new ItemStack(ModItems.CLAWS.get()));
//        player.sendSystemMessage(Component.literal("You have received your own [Claws] and [A Warrior's Guide]!").withStyle(ChatFormatting.YELLOW));
//        player.sendSystemMessage(Component.literal("Get support and stay tuned for mod updates: ").append(
//                Component.literal("[Discord]")
//                        .withStyle(style -> style
//                                .withColor(0x579dff)
//                                .withUnderlined(true)
//                                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/SkYvZr9DBb"))
//                        )
//        ));




    }


}
