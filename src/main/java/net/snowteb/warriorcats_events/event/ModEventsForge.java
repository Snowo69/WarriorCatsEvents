package net.snowteb.warriorcats_events.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.MakeshiftBedBlock;
import net.snowteb.warriorcats_events.block.custom.MossBedBlock;
import net.snowteb.warriorcats_events.block.custom.PreyBonesBlock;
import net.snowteb.warriorcats_events.block.custom.StoneCraftingTable;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.client.LeapClientState;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatAvoidGoal;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModFoodHerbs;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.others.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.particles.WCEParticles;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import net.snowteb.warriorcats_events.util.ModTags;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.PlayerShape;

import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsForge {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;
        Level level = event.getLevel();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        if (hand != InteractionHand.MAIN_HAND) return;

        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);

        if ((blockState.getBlock() instanceof StoneCraftingTable table)) {
            if ((PlayerShape.getCurrentShape(player) instanceof Animal)) {
                if (player.isShiftKeyDown()) {
                    if (!level.isClientSide()) {
                        if (table.handleHerbsRecipeCraftingBlockState(blockState, level, pos, player, hand)) {
                            if (level instanceof ServerLevel sLevel) {
                                Vec3 position = pos.getCenter();

                                Direction facing = blockState.getValue(StoneCraftingTable.FACING);

                                position = switch (facing) {
                                    case NORTH -> position.add(0,0,0.1);
                                    case SOUTH -> position.add(0,0,-0.1);
                                    case WEST -> position.add(0.1,0,0);
                                    case EAST -> position.add(-0.1,0,0);
                                    default -> position;
                                };

                                sLevel.sendParticles(
                                        WCEParticles.HERBS_FALL.get(),
                                        position.x, position.y - 0.1, position.z,
                                        10, 0.1, 0.0, 0.1, 0.005);
                            }

                            event.setCanceled(true);
                            event.setCancellationResult(InteractionResult.SUCCESS);
                        }
                    } else {
                        LeapClientState.setCanceled();
                    }
                }
            }
        }

        if (!level.isClientSide()) {
            if (player.getItemInHand(hand).is(Items.BONE)) {
                if (blockState.isSolid()) {
                    BlockPos above = pos.above();
                    BlockState onTop = level.getBlockState(above);
                    if (onTop.isAir() || onTop.is(BlockTags.SMALL_FLOWERS) || onTop.is(Blocks.GRASS)) {
                        BlockState newBlockstate = ModBlocks.PREY_BONES.get().defaultBlockState()
                                .setValue(PreyBonesBlock.FACING, player.getDirection().getOpposite());

                        level.setBlockAndUpdate(above, newBlockstate);

                        if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);

                        level.playSound(null, pos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS,
                                0.8F, 1.4F);

                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }
                } else if (blockState.is(ModBlocks.PREY_BONES.get())) {
                    PreyBonesBlock.Bones bones = blockState.getValue(PreyBonesBlock.BONES);

                    if (PreyBonesBlock.canPlaceBones(bones)){
                        BlockState newBlockstate = ModBlocks.PREY_BONES.get().defaultBlockState()
                                .setValue(PreyBonesBlock.FACING, blockState.getValue(PreyBonesBlock.FACING));

                        if (bones == PreyBonesBlock.Bones.STAGE_1) {
                            newBlockstate = newBlockstate.setValue(PreyBonesBlock.BONES, PreyBonesBlock.Bones.STAGE_2);
                        } else if (bones == PreyBonesBlock.Bones.STAGE_2) {
                            newBlockstate = newBlockstate.setValue(PreyBonesBlock.BONES, PreyBonesBlock.Bones.STAGE_3);
                        }

                        level.setBlockAndUpdate(pos, newBlockstate);
                        if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);


                        level.playSound(null, pos, SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS,
                                0.8F, 1.4F);

                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }

                }
            }
        }


    }

    @SubscribeEvent
    public static void canPlayerSleep(SleepingTimeCheckEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer)) return;

        if (event.getEntity().level().isDay()) {
            event.setResult(Event.Result.ALLOW);
        }
    }


    @SubscribeEvent
    public static void onSleepAttempt(PlayerSleepInBedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!player.level().isDay()) return;

        int sleepingCooldown = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getSleepingCooldown)
                .orElse(0);

        if (sleepingCooldown > 0) {
            player.displayClientMessage(
                    Component.literal("You are not tired enough!")
                            .withStyle(ChatFormatting.GRAY),
                    true
            );
            event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);

        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        long quarterDay = 24000L / 4;
        LevelAccessor level = event.getLevel();
        if (level instanceof ServerLevel sLevel) {
            if (sLevel.isDay()) {
                long dayTime = sLevel.getDayTime();
                event.setTimeAddition(dayTime + quarterDay);
                WarriorCatsEvents.LOGGER.info("Time advanced for " + quarterDay + " ticks");
            }

            for (ServerPlayer player : sLevel.players()) {
                if (player.isSleeping()) {
                    player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .ifPresent(cap -> cap.setSleepingCooldown(12000));
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();

        BlockPos sleepingPos = player.getSleepingPos().orElse(null);
        if (sleepingPos == null) return;

        Level level = player.level();
        BlockState state = level.getBlockState(sleepingPos);

        if ((state.getBlock() instanceof MossBedBlock)) {
            level.setBlock(
                    sleepingPos,
                    state.setValue(MossBedBlock.OCCUPIED, false),
                    3
            );

            if (level instanceof ServerLevel) {
                ModEvents2.schedule(1, () -> {
                    Vec3 exactPos = sleepingPos.getCenter().add(0, 0.1, 0);
                    player.teleportTo(exactPos.x, exactPos.y, exactPos.z);
                });

            }
        } else if (state.getBlock() instanceof MakeshiftBedBlock) {
            if (level instanceof ServerLevel sLevel) {

                Vec3 exactPos = sleepingPos.getCenter().add(0, -0.3, 0);

                sLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, state),
                        exactPos.x, exactPos.y, exactPos.z,
                        15, 0.5, 0.3, 0.5, 0.1
                );

                sLevel.playSound(null, player.blockPosition(), SoundEvents.CHERRY_LEAVES_BREAK,
                        SoundSource.BLOCKS, 1.0F, 1.0F);

            }

            level.setBlock(
                    sleepingPos,
                    Blocks.AIR.defaultBlockState(),
                    3
            );

        }
    }


    /**
     * This modifies some foods so that some will fill thirsts, and some other will fill more hunger.
     */
    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack stack = event.getItem();
        if (!stack.isEdible()) return;
        if (player.level().isClientSide()) return;

        player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.SORREL) {
                int randomThirst = 2 + player.getRandom().nextInt(3);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.TRAVELING_HERBS) {
                thirst.addThirst(4);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.MOUSE_FOOD) {
                int randomThirst = 1 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.SQUIRREL_FOOD) {
                int randomThirst = 2 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (stack.getItem().getFoodProperties() == ModFoodHerbs.PIGEON_FOOD) {
                int randomThirst = 2 + player.getRandom().nextInt(2);
                thirst.addThirst(randomThirst);
            }

            if (WCEServerConfig.SERVER.VANILLA_MEAT_BONUS.get()) {
                if (stack.is(Items.CHICKEN) || stack.is(Items.PORKCHOP)
                        || stack.is(Items.BEEF) || stack.is(Items.MUTTON)
                        || stack.is(Items.RABBIT) || stack.is(Items.SALMON)
                        || stack.is(Items.COD) || stack.is(Items.TROPICAL_FISH)
                        || stack.is(Items.SWEET_BERRIES)) {
                    int randomThirst = 1 + player.getRandom().nextInt(1);
                    thirst.addThirst(randomThirst);
                    if (!stack.is(Items.SWEET_BERRIES)) {
                        player.getFoodData().eat(3, 0.84f);
                    }
                }
            }

            if (stack.is(ModTags.Items.ADDITIONAL_PREY)) {
                int randomThirst = 1 + player.getRandom().nextInt(1);
                thirst.addThirst(randomThirst);
                player.getFoodData().eat(4, 0.84f);
            }




            if (player instanceof ServerPlayer serverPlayer) {
                ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), serverPlayer);
            }
        });

        if (stack.getItem() == ModItems.YARROW.get()) {

            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
            }
            if (player.hasEffect(ModEffects.DEATHBERRIES.get())) {
                player.removeEffect(ModEffects.DEATHBERRIES.get());
            }

        }

        if (stack.is(Items.CHICKEN) || stack.is(Items.PORKCHOP)
                || stack.is(Items.BEEF) || stack.is(Items.MUTTON)
                || stack.is(Items.RABBIT) || stack.is(Items.SALMON)
                || stack.is(Items.COD) || stack.is(Items.TROPICAL_FISH)
                || stack.is(ModItems.SQUIRREL_FOOD.get()) || stack.is(ModItems.PIGEON_FOOD.get())
                || stack.is(ModItems.MOUSE_FOOD.get())
        ) {
            if (event.getEntity().level().random.nextFloat() < 0.22) {
                Level level = event.getEntity().level();
                LivingEntity entity = event.getEntity();
                if (entity instanceof Player && level instanceof ServerLevel) {
                    ItemStack bonestack = new ItemStack(Items.BONE, 1 + level.getRandom().nextInt(3));
                    ItemEntity itemEnt = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), bonestack);
                    itemEnt.setDeltaMovement(entity.getLookAngle().scale(0.3));
                    itemEnt.setPickUpDelay(60);
                    level.addFreshEntity(itemEnt);
                    level.playSound(null, itemEnt.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.PLAYERS, 0.8f, 1.0f);
                }
            }
        }
    }


    /**
     * Every time an entity falls:
     * Check if it is a player, if it is not:
     * Then make a list of all the players in the server, and for every player verify if their UUID is the same as the shape that fell.
     * If it is, then choose it as the owner of the shape.
     * Then check if the owner has Jump level greater than 2. If it does, reduce it's fall distance.
     * <p>
     * If the entity is a Wild cat, reduce its fall distance too.
     */
    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        ServerPlayer owner = null;
        if (entity instanceof ServerPlayer sp) {
            owner = sp;
        } else {
            if (entity.level() == null || !entity.level().isClientSide()) {
                var server = entity.getServer();
                if (server != null) {
                    for (ServerPlayer candidate : server.getPlayerList().getPlayers()) {
                        LivingEntity currentShape = PlayerShape.getCurrentShape(candidate);
                        if (currentShape != null && currentShape.getUUID().equals(entity.getUUID())) {
                            owner = candidate;
                            break;
                        }
                    }
                }
            }
        }
        if (owner != null) {
            owner.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(cap -> {
                if (cap.getJumpLevel() > 2) {
                    event.setDistance(Math.max(0f, event.getDistance() - 3f));
                }
            });
        }

        if (entity instanceof WCatEntity wCat) {
            if (wCat.returnHomeFlag) {
                event.setDistance(Math.max(0f, event.getDistance() - 10f));
            } else {
                event.setDistance(Math.max(0f, event.getDistance() - 6f));
            }
        }

    }

    /**
     * Every time a creeper spawns, add this goal to it.
     * This so that creepers run away from Wild Cats.
     */
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Creeper creeper) {
//
//            creeper.goalSelector.getAvailableGoals().removeIf(g
//                    -> g.getGoal() instanceof AvoidEntityGoal<?>);
//
            boolean alreadyHasAvoid = creeper.goalSelector.getAvailableGoals().stream()
                    .anyMatch(g -> g.getGoal() instanceof WCatAvoidGoal);

            if (!alreadyHasAvoid) {
                creeper.goalSelector.addGoal(3, new WCatAvoidGoal(creeper, 12.0F, 1.3D, 1.2D));
            }

//            creeper.goalSelector.addGoal(3,
//                    new AvoidEntityGoal<>(
//                            creeper,
//                            WCatEntity.class,
//                            12.0F,
//                            1.3D,
//                            1.2D
//                    )
//            );

        }

        if (WCEServerConfig.SERVER.ENHANCED_ANIMALS.get()) {
            if (event.getEntity() instanceof Fox fox) {
                CompoundTag tag = fox.getPersistentData();

                fox.goalSelector.getAvailableGoals().removeIf(g
                        -> g.getGoal() instanceof NearestAttackableTargetGoal<?>);
                fox.goalSelector.getAvailableGoals().removeIf(g
                        -> g.getGoal() instanceof MeleeAttackGoal);
                fox.goalSelector.getAvailableGoals().removeIf(g
                        -> g.getGoal() instanceof AvoidEntityGoal<?>);


                fox.goalSelector.addGoal(2,
                        new NearestAttackableTargetGoal<>(fox, LivingEntity.class, 10, false, false,
                                (animal) -> animal instanceof Player || animal instanceof WCatEntity
                                        || animal instanceof Chicken || animal instanceof Rabbit || animal instanceof AbstractSchoolingFish));
                fox.goalSelector.addGoal(2, new MeleeAttackGoal(fox, 1.2D, true));

                if (!tag.getBoolean("fox_enhanced")) {
                    tag.putBoolean("fox_enhanced", true);
                    fox.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
                    fox.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
                    fox.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.2D);
                    fox.getAttribute(Attributes.ARMOR).setBaseValue(12D);
                    fox.setHealth(fox.getMaxHealth());
                }

            }

            if (event.getEntity() instanceof Wolf wolf) {
                CompoundTag tag = wolf.getPersistentData();

                wolf.goalSelector.getAvailableGoals().removeIf(g
                        -> g.getGoal() instanceof NearestAttackableTargetGoal<?>);

                wolf.goalSelector.addGoal(4,
                        new NearestAttackableTargetGoal<>(wolf, LivingEntity.class, 10, false, false,
                                (animal) -> animal instanceof Player || animal instanceof WCatEntity || animal instanceof AbstractSkeleton));

                if (!tag.getBoolean("wolf_enhanced")) {
                    tag.putBoolean("wolf_enhanced", true);
                    wolf.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
                    wolf.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35D);
                    wolf.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.2D);
                    wolf.getAttribute(Attributes.ARMOR).setBaseValue(12D);
                    wolf.setHealth(wolf.getMaxHealth());
                }

            }
        }

        if (event.getEntity() instanceof WCatEntity wCat) {
            CompoundTag tag = wCat.getPersistentData();

            if (wCat.isTame()) {
                LivingEntity owner = wCat.getOwner();
                if (owner instanceof ServerPlayer serverPlayer) {
                    UUID clanUUID = serverPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

                    if (!Objects.equals(clanUUID, wCat.getClanUUID())) {
                        wCat.setClanUUID(clanUUID);
                        wCat.updateClanCatData();
                    }
                }
            }

            if (!tag.getBoolean("spawn_att_applied")) {
                tag.putBoolean("spawn_att_applied", true);

                if (wCat.isBaby()) {
                    wCat.applyBabyAttributes();
                } else {
                    wCat.applyAdultAttributes();
                }

                wCat.setHealth(wCat.getMaxHealth());
            }
        }

        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            UUID mateUUID = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getMateUUID).orElse(ClanData.EMPTY_UUID);
            Entity ent = level.getEntity(mateUUID);
            if (ent != null) {
                if (ent instanceof ServerPlayer playerMate) {
                    String myMorphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getMorphName).orElse("Unnamed");

                    String mateMorphName = playerMate.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getMorphName).orElse("Unnamed");

                    playerMate.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                        if (!cap.getMateName().getString().equals(myMorphName)) {
                            cap.setMateName(Component.literal(myMorphName));
                        }
                    });

                    player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                        if (!cap.getMateName().getString().equals(mateMorphName)) {
                            cap.setMateName(Component.literal(mateMorphName));
                        }
                    });
                }
            }
        }

    }

    /**
     * Every time a player wants to attack a wild cat, check certain conditions, and under that criteria decide whether to let the player attack it or not.
     */
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (target instanceof EagleEntity eagle) {
            if (player.getVehicle() == eagle) {
                event.setCanceled(true);
            }
        }

        if (target instanceof ServerPlayer serverPlayer) {

            UUID targetUUID = serverPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);
            UUID thisUUID = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

            if (!targetUUID.equals(thisUUID) || (targetUUID.equals(ClanData.EMPTY_UUID) || thisUUID.equals(ClanData.EMPTY_UUID))) {
                return;
            }
            if (!player.isShiftKeyDown()) {
                event.setCanceled(true);
            }
        }

        if (!(target instanceof WCatEntity || target instanceof EagleEntity)) {
            return;
        }

        WCatEntity wcat = null;
        if (target instanceof WCatEntity) wcat = (WCatEntity) target;

        EagleEntity eagle = null;
        if (target instanceof EagleEntity) eagle = (EagleEntity) target;

        if (player.level().isClientSide()) {
            return;
        }

        if (wcat != null) {

            if (!wcat.isTame()) {
                return;
            }

            LivingEntity owner = wcat.getOwner();
            if (owner == null) {
                return;
            }

            if (!owner.getUUID().equals(player.getUUID())) {
                return;
            }

            if (player instanceof ServerPlayer serverPlayer) {
                if (!wcat.getClanUUID().equals(ClanData.EMPTY_UUID)) {
                    UUID clanUUID = serverPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

                    ClanData data = ClanData.get(serverPlayer.serverLevel().getServer().overworld());
                    ClanData.Clan clan = data.getClan(clanUUID);
                    if (clan != null) {
                        if (!wcat.getClanUUID().equals(clanUUID)) {
                            return;
                        }
                    }
                }
            }

            if (!player.isShiftKeyDown()) {
                event.setCanceled(true);
            }
        } else if (eagle != null) {
            if (!eagle.isTame()) {
                return;
            }

            LivingEntity owner = eagle.getOwner();
            if (owner == null) {
                return;
            }

            if (!owner.getUUID().equals(player.getUUID())) {
                return;
            }

            if (!player.isShiftKeyDown()) {
                event.setCanceled(true);
            }
        }
    }

//    @SubscribeEvent
//    public static void onPlayerHurt(LivingHurtEvent event) {
//        if (!(event.getEntity() instanceof Player player)) return;
//
//        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
//
//        if (head.getItem() instanceof FlowerCrownItem) {
//            head.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
//        }
//        if (head.getItem() instanceof FlowerArmorItem) {
//            head.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
//        }
//    }


}
