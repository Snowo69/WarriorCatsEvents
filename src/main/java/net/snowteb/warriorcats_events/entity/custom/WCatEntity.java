package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.snowteb.warriorcats_events.WCEConfig;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.screen.WCatMenu;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.util.ModTags;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import tocraft.walkers.api.PlayerShape;

import java.util.*;
import java.util.function.Predicate;

import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.Rank.*;

/**
 * Welcome to by far the most complicated shi to understand.
 */

public class WCatEntity extends TamableAnimal implements GeoEntity {

    public enum CatMode {
        SIT,
        FOLLOW,
        WANDER
    }


    public enum Rank {
        NONE,
        KIT,
        APPRENTICE,
        WARRIOR,
        MEDICINE
    }

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);


    private static final EntityDataAccessor<Float> SCALE =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> KITTING_TICKS =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);


    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private Goal preyTarget;
    private boolean kitBorn = false;
    boolean animPlayed;
    public CatMode mode = CatMode.WANDER;
    private BlockPos wanderCenter = null;
    int maxVariants = 20;
    private boolean wasBaby = this.isBaby();
    int catSniffTickCooldown = 0;
    public boolean isAShape = false;

    private int scentTick = 0;
    private Vec3 scentDirection = null;
    private Vec3 scentStartPos = null;
    private double scentDistance = 0;
    private double scentMaxDistance = 5;
    private double scentStep = 0.2;



    private final SimpleContainer inventory = new SimpleContainer(3);

    @Nullable
    private Vec3 leaderCallTarget;


    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.BOOLEAN);
    public int attackAnimationTimeout = 0;


    private static final EntityDataAccessor<Integer> GENDER =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> EXPECTING_KITS =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<Component>> MATE =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Optional<Component>> PREFIX =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Integer> RANK =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> AGE_SYNC =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> APP_SCALE =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.BOOLEAN);


    private static final String[] PREFIX_1 = {
            // 1, 10, 11
            //MultiColor
            "Leaf", "Marble", "Fleck", "Dapple", "Spotted",
            "Tawny", "Mottle", "Speckle", "Brindle", "Splotch",
            "Bumble", "Bright", "Sun", "Rainbow"

    };
    private static final String[] PREFIX_2 = {
            // 2, 3, 9
            // Gray and cream
            "Pearl", "Mist", "Ivory", "Silk", "Feather",
            "Leaf", "Ash", "Fawn", "Soft", "Frost", "Snow",
            "Cloud", "Storm", "Sparrow", "Rat", "Bengal",
            "Willow"
    };
    private static final String[] PREFIX_3 = {
            // 4, 8
            // Tabbies
            "Tiger", "Flame", "Ember", "Bracken", "Fire",
            "Oak", "Rust", "Maple", "Amber", "Hare",
            "Lion", "Dawn", "Dark", "Bumble", "Mole",
            "Sun", "Blaze", "Chestnut", "Fox"
    };
    private static final String[] PREFIX_4 = {
            // 5, 6, 7, 12
            // Stone colors
            "Stone", "Jay", "Pebble", "Dark", "Ash",
            "Night", "Dust", "Mist", "Swift", "Shade",
            "Holly", "Mole", "Storm", "Sparrow", "Twitch"


    };


    private static final String[] SUFIX = {
            "claw", "fur", "feather", "pelt", "eye",
            "heart", "tail", "wing", "whisker", "blaze",
            "fang", "shade", "step", "fall", "song",
            "stripe", "light", "leap", "foot", "spring",
            "pit", "stream", "patch"
    };

    /**
     * Depending on the variant, pick a set of prefixes
     */
    private String[] getPrefixForVariant(int variant) {
        return switch (variant) {
            case 0 -> PREFIX_1;
            case 1 -> PREFIX_2;
            case 2 -> PREFIX_2;
            case 3 -> PREFIX_3;
            case 4 -> PREFIX_4;
            case 5 -> PREFIX_4;
            case 6 -> PREFIX_4;
            case 7 -> PREFIX_3;
            case 8 -> PREFIX_2;
            case 9 -> PREFIX_1;
            case 10 -> PREFIX_1;
            case 11 -> PREFIX_4;
            case 12 -> PREFIX_3; // chestnutpatch
            case 13 -> PREFIX_2; // ratstar
            case 14 -> PREFIX_4; // twitchstream
            case 15 -> PREFIX_3; // blazepit
            case 16 -> PREFIX_2; // bengalpelt
            case 17 -> PREFIX_2; // sparrowstar
            case 18 -> PREFIX_3; // foxeater
            case 19 -> PREFIX_2; // willowsong

            default -> PREFIX_1; // fallback
        };
    }


    public WCatEntity(EntityType<? extends TamableAnimal> type, Level world) {
        super(type, world);
        if (!this.level().isClientSide()) {
            this.setGender(this.random.nextInt(2));
        }
    }

    private int getWanderRadius() {
        return WCEConfig.COMMON.WILDCAT_WANDER_RADIUS.get();
    }

    private int getKittingTime() {
        return 20 * 60 * WCEConfig.COMMON.KITTING_MINUTES.get();
    }

    private int getKitGrowthTimeMinutes() {
        return WCEConfig.COMMON.KIT_GROWTH_MINUTES.get();
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Under certain conditions, proceed to follow the owner.
     */
    private static class CatFollowOwnerGoal extends Goal {

        private final TamableAnimal cat;
        private LivingEntity owner;
        private final double speed;
        private final float stopDistance;
        private final float startDistance;

        public CatFollowOwnerGoal(TamableAnimal cat, double speed, float stopDistance, float startDistance) {
            this.cat = cat;
            this.speed = speed;
            this.stopDistance = stopDistance;
            this.startDistance = startDistance;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (!cat.isTame()) return false;
            if (wcat.mode != CatMode.FOLLOW) return false;
            if (cat.isOrderedToSit()) return false;

            if (cat.getTarget() != null) return false;

            LivingEntity ownerEntity = cat.getOwner();
            if (ownerEntity == null) return false;
            if (cat.distanceTo(ownerEntity) < startDistance) return false;

            this.owner = ownerEntity;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (wcat.mode != CatMode.FOLLOW) return false;
            if (cat.isOrderedToSit()) return false;

            if (cat.getTarget() != null) return false;

            if (owner == null || !owner.isAlive()) return false;

            return cat.distanceTo(owner) > stopDistance;
        }

        /**
         * Calculates the distance to the owner, then looks at the owner.
         * If the distance is greater than 25, teleport to the owner
         * If the distance is less or equals than the max distance it can approach, then stop.
         * If none of the other conditions are true, then move to where the owner is.
         */
        @Override
        public void tick() {
            if (owner == null) return;

            double dist = cat.distanceTo(owner);

            cat.getLookControl().setLookAt(owner, 10.0F, cat.getMaxHeadXRot());

            if (dist > 25 && (cat.getOwner() != null && cat.getOwner().onGround())) {
                cat.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                cat.getNavigation().stop();
                return;
            }

            if (dist <= stopDistance) {
                cat.getNavigation().stop();
                return;
            }

            double dx = cat.getX() - owner.getX();
            double dz = cat.getZ() - owner.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);

            if (len < 0.001) len = 0.001;

            double ratio = stopDistance / len;

            double targetX = owner.getX() + dx * ratio;
            double targetZ = owner.getZ() + dz * ratio;
            double targetY = owner.getY();

            cat.getNavigation().moveTo(targetX, targetY, targetZ, speed);
        }

    }

    /**
     * Under certain chance and conditions, find a target block in certain range. This depends on the cat's rank.
     * When it starts, move to the block.
     * When it stops, set a cooldown so it doesn't constantly move from block to block.
     */
    public class CasualBlockSeekGoal extends Goal {

        private final WCatEntity cat;
        private final double speed;
        private final int baseRadius;
        private final double chance;
        private int cooldown = 0;


        private BlockPos targetPos = null;
        private Predicate<BlockState> targetPredicate;

        public CasualBlockSeekGoal(WCatEntity cat, double speed, int baseRadius, double chance) {
            this.cat = cat;
            this.speed = speed;
            this.baseRadius = baseRadius;
            this.chance = chance;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            if (cat.isOrderedToSit()) return false;

            if (cat.getRandom().nextDouble() >= this.chance) return false;

            this.targetPredicate = defineTargetPredicate();

            this.targetPos = findTargetBlock();
            return this.targetPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return targetPos != null &&
                    !cat.getNavigation().isDone() &&
                    !cat.isOrderedToSit();
        }

        @Override
        public void start() {

            if (cat.wanderCenter != null &&
                    cat.blockPosition().distSqr(cat.wanderCenter) > getWanderRadius() * getWanderRadius()) {
                cat.getNavigation().moveTo(wanderCenter.getX(), wanderCenter.getY(), wanderCenter.getZ(), speed);
                return;
            }

            if (targetPos != null) {
                cat.getNavigation().moveTo(
                        targetPos.getX() + 0.5,
                        targetPos.getY(),
                        targetPos.getZ() + 0.5,
                        speed
                );
            }
        }

        @Override
        public void stop() {
            targetPos = null;
            cat.getNavigation().stop();
            this.cooldown = 200 + cat.getRandom().nextInt(4) * 40;
        }

        private Predicate<BlockState> defineTargetPredicate() {

            Block targetBlock = switch (cat.getRank()) {
                case KIT -> ModBlocks.MOSSBED.get();
                case APPRENTICE -> ModBlocks.MOSSBED.get();
                case WARRIOR -> ModBlocks.MOSSBED.get();
                case MEDICINE -> ModBlocks.STONECLEFT.get();
                default -> ModBlocks.MOSSBED.get();
            };

            return state -> state.is(targetBlock);
        }

        /**
         * In certain radious, make a list of available blocks.
         * For every block found, verify if it could be a target.
         * If it is, then set it as the target block.
         */
        private BlockPos findTargetBlock() {
            Level level = cat.level();
            BlockPos origin = cat.blockPosition();

            List<BlockPos> found = new ArrayList<>();

            int radius = this.baseRadius;

            for (int x = -radius; x <= radius; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = origin.offset(x, y, z);
                        if (targetPredicate.test(level.getBlockState(pos))) {
                            found.add(pos);
                        }
                    }
                }
            }

            if (found.isEmpty()) return null;

            return found.get(cat.getRandom().nextInt(found.size()));
        }
    }

    /**
     * Under Certain conditions, the cat will pick a position withing the radius and will move to it.
     * When it stops, set a cooldown so it doesn't constantly wander around.
     */
    private class BoundedWanderGoal extends WaterAvoidingRandomStrollGoal {

        private final TamableAnimal cat;
        private int cooldown = 0;

        public BoundedWanderGoal(TamableAnimal cat, double speed) {
            super(cat, speed);
            this.cat = cat;
            this.setInterval(40);
        }


        @Override
        public boolean canUse() {
            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (!cat.isTame()) return false;
            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

            if (wcat.wanderCenter != null &&
                    cat.blockPosition().distSqr(wcat.wanderCenter) > getWanderRadius() * getWanderRadius()) {
                return false;
            }

            if (wcat.wanderCenter == null) return false;

            Vec3 pos = this.getRandomPointInRadius(wcat);

            if (pos == null) return false;

            this.wantedX = pos.x;
            this.wantedY = pos.y;
            this.wantedZ = pos.z;

            return true;
        }

        @Override
        public void stop() {
            super.stop();

            cooldown = cat.getRandom().nextInt(5) * 20 + 140;
        }

        private Vec3 getRandomPointInRadius(WCatEntity wcat) {
            int attempts = 10;

            for (int i = 0; i < attempts; i++) {

                double angle = cat.getRandom().nextDouble() * (Math.PI * 2);
                double r = cat.getRandom().nextDouble() * getWanderRadius();

                double x = wcat.wanderCenter.getX() + 0.5 + Math.cos(angle) * r;
                double z = wcat.wanderCenter.getZ() + 0.5 + Math.sin(angle) * r;
                double y = wcat.getY();

                BlockPos groundPos = BlockPos.containing(x, y - 1, z);

                if (cat.level().getBlockState(groundPos).isSolid()) {
                    return new Vec3(x, y, z);
                }
            }

            return null;
        }

        @Override
        protected Vec3 getPosition() {
            return new Vec3(this.wantedX, this.wantedY, this.wantedZ);
        }


        @Override
        public boolean canContinueToUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

            if (wcat.wanderCenter != null &&
                    cat.blockPosition().distSqr(wcat.wanderCenter) > getWanderRadius() * getWanderRadius()) {
                return false;
            }

            return !cat.getNavigation().isDone();
        }
    }


//    public class LeaderCallsGoal extends Goal {
//
//        private final WCatEntity cat;
//        private Vec3 target;
//        private int time;
//
//        public LeaderCallsGoal(WCatEntity cat) {
//            this.cat = cat;
//            this.setFlags(EnumSet.of(Flag.MOVE));
//        }
//
//        private void clearTarget() {
//            time = 0;
//            target = null;
//            cat.setLeaderCallTarget(null);
//        }
//
//        @Override
//        public boolean canUse() {
//            return cat.getLeaderCallTarget() != null && cat.isTame() && cat.getOwner() != null;
//        }
//
//        @Override
//        public void start() {
//            target = cat.getLeaderCallTarget();
//            this.time = 200;
//            if (target != null) {
//                cat.getNavigation().moveTo(
//                        target.x,
//                        target.y,
//                        target.z,
//                        1.3D
//                );
//            }
//        }
//
//        @Override
//        public void tick() {
//            time--;
//            if (time <= 0) {
//                clearTarget();
//                return;
//            }
//
//            if (target == null) return;
//
//            if (cat.distanceToSqr(target) < 1.3D) {
//                clearTarget();
//            }
//        }
//
//        @Override
//        public boolean canContinueToUse() {
//            return target != null;
//        }
//
//        @Override
//        public void stop() {
//            time = 0;
//            target = null;
//        }
//    }


    /**
     * Under certain conditions, find the nearest valid item it can pick up.
     * When it starts, move to the target.
     * When it stops, set the target as null, and the security counter is set to zero, and stop moving.
     */
    public class WCatPickupItemGoal extends Goal {

        private final WCatEntity cat;
        private ItemEntity target;
        private int cooldown = 0;
        private int keepTicks = 0;
        private final int BASE_COOLDOWN = 30;

        public WCatPickupItemGoal(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (target != null && (!target.isAlive() || cat.distanceTo(target) > 25D)) {
                target = null;
            }

            if (cat.isOrderedToSit()) return false;

            if (target != null) return false;

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            target = findNearestItem();

            if (target != null) {
                cooldown = BASE_COOLDOWN + cat.getRandom().nextInt(10);
                return true;
            }

            cooldown = 10;
            return false;
        }


        @Override
        public boolean canContinueToUse() {
            if (target != null && cat.distanceTo(target) > 20D) return false;
            return target != null
                    && target.isAlive()
                    && !cat.isOrderedToSit();
        }

        @Override
        public void start() {
            if (target != null) {
                cat.getNavigation().moveTo(target, 1.1D);
            }
        }

        @Override
        public void stop() {
            target = null;
            keepTicks = 0;
            cat.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (target == null || !target.isAlive()) {
                stop();
                return;
            }

            /**
             * If it is not moving, then move to the target.
             */
            if (!cat.getNavigation().isInProgress()) {
                cat.getNavigation().moveTo(target, 1.1D);
            }

            /**
             * If it still is not moving, start counting.
             */
            if (cat.getNavigation().isInProgress()) {
                keepTicks = 0;
            } else {
                keepTicks++;
            }

            /**
             * In case it gets stuck without being able to pick up the item or move, then stop.
             */
            if (keepTicks > 60) {
                stop();
                return;
            }

            /**
             * Withing certain distance of the target, try to insert it into the inventory.
             * Then remove 1 from the stack on the ground.
             */
            ItemStack groundItems = target.getItem();
            if (cat.distanceTo(target) < 1.2D) {
                if (cat.tryInsert(groundItems)) {
                    groundItems.shrink(1);

                    if (groundItems.isEmpty()) {
                        target.discard();
                    }


                    cat.level().playSound(
                            null, cat.getX(), cat.getY(), cat.getZ(),
                            SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL,
                            0.5F, 0.9F + cat.getRandom().nextFloat() * 0.2F
                    );
                    cat.level().playSound(
                            null, cat.getX(), cat.getY(), cat.getZ(),
                            SoundEvents.CAT_EAT, SoundSource.NEUTRAL,
                            0.6F, 0.9F + cat.getRandom().nextFloat() * 0.2F
                    );

                }
                stop();
            } else {

                if (cat.distanceTo(target) > 20D) {
                    this.stop();
                }
                /**
                 * If the distance to the item is not enough and this is not moving, then move around the item.
                 */
                if (target != null && !cat.getNavigation().isInProgress()) {
                    List<BlockPos> positions = List.of(
                            target.blockPosition().offset(2, 1, 2),
                            target.blockPosition().offset(2, 1, -2),
                            target.blockPosition().offset(-2, 1, 2),
                            target.blockPosition().offset(-2, 1, -2)
                    );

                    for (BlockPos pos : positions) {
                        if (cat.getNavigation().moveTo(pos.getX(), pos.getY(), pos.getZ(), 1.1D)) {
                            break;
                        }
                    }
                }
            }
        }


        /**
         * In certain are, make a list of droped items.
         * Then for every item in the list, verify if the cat can accept it.
         * If the distance to the item is less than the one from the last item, then set that item as the closest.
         */
        private ItemEntity findNearestItem() {
            AABB box = cat.getBoundingBox().inflate(16);

            List<ItemEntity> items = cat.level().getEntitiesOfClass(
                    ItemEntity.class,
                    box
            );

            double closestDist = Double.MAX_VALUE;
            ItemEntity closest = null;

            for (ItemEntity item : items) {
                ItemStack stack = item.getItem();

                if (!cat.canAccept(stack)) continue;

                double dist = cat.distanceToSqr(item);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = item;
                }
            }

            return closest;
        }

    }

    /**
     * Depending on the rank, different conditions.
     * Then for every slot in the cat's inventory, verify that:
     * 1) If the slot is not empty, the item in the slot is the same as the target item, and the item count is less than 32, return true.
     * 2) If the slot is empty, return true as well.
     * If the for naturally ends, return false.
     */
    public boolean canAccept(ItemStack stack) {

        if (this.getRank() == Rank.MEDICINE) {
            if (!stack.is(ModTags.Items.HERBS)) return false;
        } else if (this.isBaby() && this.getRank() == Rank.KIT) {
            if (!(stack.is(Items.STICK) || stack.is(Items.MOSS_BLOCK) || stack.is(Items.SLIME_BALL))) return false;
        } else {
            if (!stack.is(ModTags.Items.PREY)) return false;
        }

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);

            if (!slot.isEmpty()
                    && ItemStack.isSameItemSameTags(slot, stack)
                    && slot.getCount() < 32) {
                return true;
            }

            if (slot.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * For every slot in the inventory:
     * If the slot is empty, then copy the item from the dropped item and return true.
     * If the item in the slot is the same as the item on the ground, then increment its ammount by 1 and return true.
     * Otherwise return false.
     */
    private boolean tryInsert(ItemStack stack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.isEmpty()) {
                inventory.setItem(i, stack.copyWithCount(1));
                return true;
            }
            if (ItemStack.isSameItemSameTags(slot, stack) && slot.getCount() < 32) {
                slot.grow(1);
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the entity dies.
     */
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        /**
         * If these conditions are true, then return.
         */
        if (this.level().isClientSide) return;
        if (this.isRemoved() && !this.dead) return;
        if (this.isBaby()) return;

        /**
         * If it has an owner, then send the owner a message with the coordinates where the cat died.
         */
        if (this.getOwner() != null) {
            ServerPlayer owner = (ServerPlayer) this.getOwner();
            owner.sendSystemMessage(Component.literal(
                    String.format("At: X=%.1f, Y=%.1f, Z=%.1f",
                            this.getX(), this.getY(), this.getZ())
            ).withStyle(ChatFormatting.GRAY));
        }

        /**
         * For every slot in the cats inventory, get the item that's in the slot.
         * If the slot is not empty, then spawn a dropped item at the position and set the slot in the inventory empty.
         */
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack.copy());
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }

    }


    @Override
    protected void registerGoals() {
        this.preyTarget = new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (target) -> {
            return mode == CatMode.WANDER && (target instanceof MouseEntity || target instanceof PigeonEntity || target instanceof SquirrelEntity);
        });


        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        //this.goalSelector.addGoal(4, new LeaderCallsGoal(this));
        this.goalSelector.addGoal(3, new WCatPickupItemGoal(this));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new CatFollowOwnerGoal(this, 1.2D, 1.0F, 7.0F)); // prioridad alta para seguir si toca
        this.targetSelector.addGoal(7, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(8, this.preyTarget);
        this.goalSelector.addGoal(9, new WCAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(10, new BoundedWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(12, new CasualBlockSeekGoal(this, 1.0D, 15, 0.07D));

    }


    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ARMOR, 1.0D);
    }


    public static void initSpawn(EntityType<WCatEntity> type) {
        SpawnPlacements.register(
                type,
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, level, reason, pos, random)
                        -> level.getBlockState(pos.below()).isSolid()
        );
    }

    /**
     * This is just too much, if you have any questions about the mobinteract just ask me, i aint writing all this :sob:
     */
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (this.getRank() == Rank.MEDICINE && this.isTame() && this.getOwner() == pPlayer) {

            if (!pPlayer.getItemInHand(pHand).isEmpty()) {
                if (pPlayer.getItemInHand(pHand).is(ModItems.DOCK_LEAVES.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.DOCK.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.SORREL.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.SORRELPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.BURNET.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.BURNETPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.CHAMOMILE.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.CHAMOMILEPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.DAISY.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.DAISYPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.CATMINT.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.CATMINTPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.YARROW.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.YARROWPLANT.get(), 40);
                    return InteractionResult.SUCCESS;
                }
                if (pPlayer.getItemInHand(pHand).is(ModItems.GLOW_SHROOM.get())) {
                    medicineCatScentsBlock(pPlayer, ModBlocks.GLOWSHROOM.get(), 40);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if ((itemstack.is(ModItems.CLAWS.get()) && pPlayer.isShiftKeyDown()) && this.isTame() && (this.getOwner() == pPlayer) && !this.isBaby()) {

            if (!level().isClientSide && pPlayer instanceof ServerPlayer sPlayer) {
                Component catInvName = this.getCustomName();
                NetworkHooks.openScreen(
                        sPlayer,
                        new SimpleMenuProvider(
                                (id, inv, p) -> new WCatMenu(id, inv, this.inventory),
                                Component.literal(catInvName.getString())));
            }

            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (!this.isTame() && itemstack.is(ModItems.FRESHKILL_AND_HERBS_BUNDLE.get())) {

            if (!this.level().isClientSide()) {

                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                int tameRoll;
                tameRoll = this.random.nextInt(2);
                if (tameRoll == 0) {
                    this.tame(pPlayer);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                    this.setRank(WARRIOR);


                    if (!this.hasCustomName()) {
                        int variant = this.getVariant();
                        String[] prefixSet = getPrefixForVariant(variant);

                        String genderS;
                        if (this.getGender() == 0) {
                            genderS = " ♂";
                        } else {
                            genderS = " ♀";
                        }

                        int i = this.random.nextInt(prefixSet.length);
                        int j = this.random.nextInt(SUFIX.length);

                        String finalName = prefixSet[i] + SUFIX[j] + genderS;
                        this.setCustomName(Component.literal(finalName));
                        this.setCustomNameVisible(true);
                        this.setPrefix(Component.literal(prefixSet[i]));

                    }

                    mode = CatMode.FOLLOW;
                    sendModeMessage(pPlayer);

                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                    this.setCustomName(null);
                }
            }


            this.gameEvent(GameEvent.EAT);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        if (this.isTame() && pPlayer.isShiftKeyDown() && pPlayer.getUUID().equals(this.getOwnerUUID())
                && pPlayer.getMainHandItem().isEmpty()) {
            if (!this.level().isClientSide()) {
                switch (mode) {
                    case SIT:
                        mode = CatMode.FOLLOW;
                        this.setInSittingPose(false);
                        break;
                    case FOLLOW:
                        mode = CatMode.WANDER;
                        wanderCenter = this.blockPosition();
                        break;
                    case WANDER:
                        mode = CatMode.SIT;
                        this.setInSittingPose(true);
                        break;
                }
                sendModeMessage(pPlayer);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && pPlayer.isShiftKeyDown() && pPlayer.getUUID().equals(this.getOwnerUUID()) && itemstack.is(ModItems.WHISKERS.get()) && (this.getRank() != KIT)) {
            Rank current = this.getRank();

            switch (current) {
                case NONE:
                    this.setRank(APPRENTICE);
                    break;
                case APPRENTICE:
                    this.setRank(Rank.WARRIOR);
                    break;
                case WARRIOR:
                    this.setRank(Rank.MEDICINE);
                    break;
                case MEDICINE:
                    this.setRank(Rank.NONE);
                    break;
            }

            sendRankMessage(pPlayer);
            return InteractionResult.SUCCESS;
        }


        if (this.isTame() && itemstack.is(ModItems.CATMINT.get()) && this.getRank() != MEDICINE && !this.isExpectingKits()) {
            if (!pPlayer.getAbilities().instabuild) itemstack.shrink(1);

            if (hasValidMateNearby()) {
                this.setInLove(pPlayer);
            }

            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && itemstack.is(ModItems.WARRIORNAMERANDOMIZER.get()) && pPlayer.isShiftKeyDown()) {

            if (!this.level().isClientSide()) {

                if (this.getVariant() == 12 || this.getVariant() == 13 || this.getVariant() == 14
                        || this.getVariant() == 15 || this.getVariant() == 16 || this.getVariant() == 17
                        || this.getVariant() == 18 || this.getVariant() == 19) {

                    itemstack.hurtAndBreak(1, pPlayer, (p) ->
                            p.broadcastBreakEvent(pHand));
                    String genderV;
                    String finalName;
                    String prefixForVariant = "";
                    String suffixForVariant = "";

                    if (this.getVariant() == 12) {
                        prefixForVariant = "Chestnut";
                        suffixForVariant = "patch";
                    } else if (this.getVariant() == 13) {
                        prefixForVariant = "Rat";
                        suffixForVariant = "star";
                    } else if (this.getVariant() == 14) {
                        prefixForVariant = "Twitch";
                        suffixForVariant = "stream";
                    } else if (this.getVariant() == 15) {
                        prefixForVariant = "Blaze";
                        suffixForVariant = "pit";
                    } else if (this.getVariant() == 16) {
                        prefixForVariant = "Bengal";
                        suffixForVariant = "pelt";
                    } else if (this.getVariant() == 17) {
                        prefixForVariant = "Sparrow";
                        suffixForVariant = "star";
                    } else if (this.getVariant() == 18) {
                        prefixForVariant = "Fox";
                        suffixForVariant = "eater";
                    } else if (this.getVariant() == 19) {
                        prefixForVariant = "Willow";
                        suffixForVariant = "song";
                    }


                    if (this.getGender() == 0) {
                        genderV = " ♂";
                    } else {
                        genderV = " ♀";
                    }
                    if (this.isBaby()) {
                        finalName = prefixForVariant + "kit" + genderV;
                    } else if (this.getRank() == APPRENTICE) {
                        finalName = prefixForVariant + "paw" + genderV;
                    } else {
                        finalName = prefixForVariant + suffixForVariant + genderV;
                    }

                    this.setPrefix(Component.literal(prefixForVariant));


                    this.setCustomName(Component.literal(finalName));
                    this.setCustomNameVisible(true);
                } else {
                    return InteractionResult.PASS;
                }


            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isTame() && itemstack.is(ModItems.WARRIORNAMERANDOMIZER.get())) {

            if (!this.level().isClientSide()) {
                itemstack.hurtAndBreak(1, pPlayer, (p) ->
                        p.broadcastBreakEvent(pHand));


                int variantS = this.getVariant();
                String[] prefixSet = getPrefixForVariant(variantS);

                String genderV;
                if (this.getGender() == 0) {
                    genderV = " ♂";
                } else {
                    genderV = " ♀";
                }

                int i = this.random.nextInt(prefixSet.length);
                int j = this.random.nextInt(SUFIX.length);


                String finalName;

                if (this.isBaby()) {
                    finalName = prefixSet[i] + "kit" + genderV;
                } else if (this.getRank() == APPRENTICE) {
                    finalName = prefixSet[i] + "paw" + genderV;
                } else {
                    finalName = prefixSet[i] + SUFIX[j] + genderV;
                }

                this.setPrefix(Component.literal(prefixSet[i]));


                this.setCustomName(Component.literal(finalName));
                this.setCustomNameVisible(true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isTame() && itemstack.is(ModItems.WHISKERS.get())) {

            if (!level().isClientSide()) {
                itemstack.hurtAndBreak(1, pPlayer, (p) ->
                        p.broadcastBreakEvent(pHand));
            }

            boolean expectingKits = this.isExpectingKits();
            String name = this.hasCustomName() ? this.getCustomName().getString() : "Unknown";
            float kittingTime = ((getKittingTime()) - this.getKittingTicks()) / (20f * 60f);
            String KitTime;
            String genderText = this.isMale() ? "Tom-cat" : "She-cat";
            String expectingText = expectingKits ? "Yes" : "No";
            Component catMate = this.getMate();
            String rankText = switch (this.getRank()) {
                case NONE -> "Loner";
                case KIT -> "Kit";
                case APPRENTICE -> "Apprentice";
                case WARRIOR -> "Warrior";
                case MEDICINE -> "Medicine Cat";
            };
            String ageText;
            float moons;
            if (!level().isClientSide()) {
                float moonsCalc = (float) ((this.getAge() + (20 * 60 * getKitGrowthTimeMinutes())) / (100.0 * getKitGrowthTimeMinutes()));
                this.entityData.set(AGE_SYNC, moonsCalc);
            }


            if (this.getAge() < 0) {
                moons = this.entityData.get(AGE_SYNC);

                ageText = String.format("%.2f moons", moons);
                ;
            } else {
                ageText = "Fully grown";
            }

            if (this.getKittingTicks() > 20) {
                KitTime = String.format("%.2f min", kittingTime);
            } else {
                KitTime = "Not expecting";
            }


            Component msg = Component.literal(
                            "=========================").withStyle(ChatFormatting.GRAY)
                    .append("\n- Displaying cat information -").withStyle(ChatFormatting.WHITE)
                    .append("\n=========================").withStyle(ChatFormatting.GRAY)
                    .append("\nName: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(name).withStyle(ChatFormatting.WHITE))
                    .append("\nGender: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(genderText).withStyle(ChatFormatting.WHITE))
                    .append("\nRole: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(rankText).withStyle(ChatFormatting.WHITE))
                    .append("\nAge: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(ageText).withStyle(ChatFormatting.WHITE))
                    .append("\nMate: ").withStyle(ChatFormatting.GOLD)
                    .append(catMate.copy().withStyle(ChatFormatting.WHITE))
                    .append("\nExpecting kits: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(expectingText).withStyle(ChatFormatting.WHITE))
                    .append("\nTime until kits: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(KitTime).withStyle(ChatFormatting.WHITE))
                    .append("\n=========================").withStyle(ChatFormatting.GRAY);
            if (this.level().isClientSide()) {
                pPlayer.displayClientMessage(msg, false);

            }

            return InteractionResult.SUCCESS;
        }

        if (this.isBaby() && this.getRank() == KIT) {
            if (itemstack.is(ModItems.DEATHBERRIES.get())) {
                if (!this.level().isClientSide()) {
                    ServerLevel level = ((ServerLevel) this.level());
                    if (pPlayer instanceof ServerPlayer serverPlayer) {

                        MinecraftServer server = serverPlayer.getServer();
                        if (server != null) {

                            Advancement adv = server.getAdvancements()
                                    .getAdvancement(new ResourceLocation("warriorcats_events:fed_kit_deathberries"));

                            if (adv != null) {
                                serverPlayer.getAdvancements().award(adv, "fed_kit_deathberries");
                            }
                        }
                    }

                    this.addEffect(new MobEffectInstance(ModEffects.DEATHBERRIES.get(), 3600, 0));
                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_EAT, SoundSource.AMBIENT, 0.8f, 1f);
                    BlockParticleOption particle = new BlockParticleOption(
                            ParticleTypes.BLOCK,
                            Blocks.REDSTONE_BLOCK.defaultBlockState()
                    );
                    level.sendParticles(
                            particle,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            10,
                            0.2f, 0.2f, 0.2f, 0.1
                    );
                }

                this.gameEvent(GameEvent.EAT);
                return InteractionResult.sidedSuccess(this.level().isClientSide());

            }
            if (itemstack.is(ModItems.YARROW.get()) && (this.hasEffect(ModEffects.DEATHBERRIES.get()) || this.hasEffect(MobEffects.POISON))) {
                if (!this.level().isClientSide()) {
                    ServerLevel level = ((ServerLevel) this.level());

                    if (this.hasEffect(ModEffects.DEATHBERRIES.get())) {
                        this.removeEffect(ModEffects.DEATHBERRIES.get());
                    }
                    if (this.hasEffect(MobEffects.POISON)) {
                        this.removeEffect(MobEffects.POISON);
                    }

                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_EAT, SoundSource.AMBIENT, 0.8f, 1f);
                    level.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            10,
                            0.2, 0.2, 0.2, 0.1
                    );
                    this.gameEvent(GameEvent.EAT);
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
        }


        return super.mobInteract(pPlayer, pHand);
    }


    /**
     * If the cooldown is not ready, send message and return.
     * <p>
     * Scan all the blocks in certain radius. If it finds the desired block, check if the last block or this one was nearest.
     * After that, if the block was null (couldn't find a block), send a message, set cooldown, and return.
     * Otherwise, send a message and set cooldown.
     */
    public void medicineCatScentsBlock(Player player, Block targetBlock, int radius) {
        if (this.level().isClientSide) return;

        Component name = this.getName();

        if (catSniffTickCooldown > 0) {
            player.sendSystemMessage(
                    Component.literal("")
                            .append(name).withStyle(ChatFormatting.GRAY)
                            .append(" can't scent that fast!").withStyle(ChatFormatting.GRAY)
            );
            return;
        }

        BlockPos catPos = this.blockPosition();
        final BlockPos[] closest = {null};
        final double[] closestDistSqr = {Double.MAX_VALUE};

        BlockPos.betweenClosedStream(
                catPos.offset(-radius, -radius, -radius),
                catPos.offset(radius, radius, radius)
        ).forEach(pos -> {
            if (this.level().getBlockState(pos).is(targetBlock)) {
                double dist = pos.distSqr(catPos);
                if (dist < closestDistSqr[0]) {
                    closestDistSqr[0] = dist;
                    closest[0] = pos.immutable();
                }
            }
        });

        if (closest[0] == null) {
            player.sendSystemMessage(
                    Component.literal("")
                            .append(name).withStyle(ChatFormatting.GRAY)
                            .append(" can't scent anything nearby.").withStyle(ChatFormatting.GRAY)
            );
            catSniffTickCooldown = 400;
            return;
        }

        int distance = (int) Math.sqrt(closestDistSqr[0]);

        String distanceText = String.valueOf(distance);

        player.sendSystemMessage(
                Component.literal("")
                        .append(name)
                        .append(Component.literal(" is scenting ")).withStyle(ChatFormatting.WHITE)
                        .append(targetBlock.getName().withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" at about ")).withStyle(ChatFormatting.WHITE)
                        .append(Component.literal(distanceText).withStyle(ChatFormatting.GREEN))
                        .append(Component.literal(" blocks."))
        );

        catSniffTickCooldown = 400;

        Vec3 catVec = this.position().add(0, 0.6, 0);
        Vec3 targetVec = Vec3.atCenterOf(closest[0]);

        Vec3 direction = targetVec.subtract(catVec).normalize();

        double maxDistance = 10.0;

        double step = 0.2;

        for (double d = 0; d < maxDistance; d += step) {
            Vec3 particlePos = catVec.add(direction.scale(d));

            double pOffset = 0.1 + (1/3)*d + (0.05 * (Math.exp(0.5 * d) - 1));
            ((ServerLevel) this.level()).sendParticles(
                    ParticleTypes.END_ROD,
                    particlePos.x,
                    particlePos.y,
                    particlePos.z,
                    1,
                    pOffset, pOffset, pOffset,
                    0.0
            );
        }

    }


    /**
     * Called when two entities are set in love.
     * <p>
     * If the other parent is a Wild cat, and this cat is tamed, and the other one is tamed:
     * Then if this cats gender is 1, and if the other cats gender is 0, then set this cat to expect kits and send the advancement.
     * Then reset the love counter, this so the she cat doesn't spawn infinite kits for no reason.
     * <p>
     * This method is called in both cats, so its not necessary to make two logics.
     */
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {

        if (otherParent instanceof WCatEntity partner && this.isTame() && ((WCatEntity) otherParent).isTame()) {


            if (this.getGender() == 1) {
                int otherParentGender = partner.getGender();
                if (otherParentGender == 0) {
                    this.setExpectingKits(true);

                    Entity owner = this.getOwner();
                    if (owner instanceof ServerPlayer serverPlayer) {
                        MinecraftServer server = serverPlayer.getServer();
                        if (server != null) {

                            Advancement adv = server.getAdvancements()
                                    .getAdvancement(new ResourceLocation("warriorcats_events:bred_wildcat"));

                            if (adv != null) {
                                serverPlayer.getAdvancements().award(adv, "bred_wildcat");
                            }
                        }
                    }
                }

                this.resetLove();
            }

            /**
             * If the cats are the same gender, unlock the advancement.
             */
            if (this.getGender() == partner.getGender()) {
                Entity owner = this.getOwner();
                if (owner instanceof ServerPlayer serverPlayer) {
                    MinecraftServer server = serverPlayer.getServer();
                    if (server != null) {

                        Advancement adv = server.getAdvancements()
                                .getAdvancement(new ResourceLocation("warriorcats_events:homo_bred"));

                        if (adv != null) {
                            serverPlayer.getAdvancements().award(adv, "homo_bred");
                        }
                    }
                }
            }

            /**
             * After all is done, set this cats mate to the other cats name.
             * And set the other cats mate to this cats name
             */
            Component MateName = otherParent.getCustomName();
            Component thisName = this.getCustomName();
            this.setMate(MateName);
            partner.setMate(thisName);

            /**
             * If for any reason, bug, glitch, the other partner's mate info doesn't change, then try to change it again.
             */
            if (Objects.equals(partner.getMate(), Component.literal("None"))) {
                partner.setMate(this.getCustomName());
            }
        }

        return null;
    }


    @Override
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
    }

    /**
     * Called every tick.
     */
    @Override
    public void aiStep() {
        super.aiStep();

        /**
         * If this cat is expecting kits, then set a counter until the kits are born.
         */
        if (!this.level().isClientSide && this.isExpectingKits()) {
            this.setKittingTicks(this.getKittingTicks() + 1);

            if (this.getKittingTicks() >= getKittingTime()) {
                this.setExpectingKits(false);
                this.setKittingTicks(0);

                Kitting();

            }
        }

        /**
         * If the kit grew from baby to adult, then perform onGrewUp, change its rank and attributes.
         */
        if (!this.level().isClientSide()) {
            if (this.wasBaby && !this.isBaby()) {
                this.onGrewUp();
                this.setRank(Rank.WARRIOR);
                this.applyAdultAttributes();
            }
            this.wasBaby = this.isBaby();
        }

        /**
         * If the cats mode is sit:
         * if it isn't ordered to sit, order it to sit, then stop all navigations.
         *
         * Otherwise (If the cats mode is not sit):
         * If it is ordered to sit, then set it to false.
         */
        if (mode == CatMode.SIT) {
            if (!this.isOrderedToSit()) this.setOrderedToSit(true);
            this.getNavigation().stop();
        } else {
            if (this.isOrderedToSit()) this.setOrderedToSit(false);
        }

        /**
         * If a cat is following and its distance to the owner is bigger than 25, then find a valid position under certain conditions, and teleport to it.
         */
        if (this.tickCount % 10 != 0) return;
        if (mode == CatMode.FOLLOW && this.isTame()) {
            LivingEntity owner = this.getOwner();

            if (owner != null) {
                double dist = this.distanceTo(owner);

                if (dist > 25 && this.getOwner().onGround()) {
                    BlockPos ownerPos = owner.blockPosition();

                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            BlockPos tpPos = ownerPos.offset(dx, 0, dz);
                            BlockPos below = tpPos.below();
                            BlockPos above = tpPos.above();

                            BlockState floor = level().getBlockState(below);
                            BlockState blockAt = level().getBlockState(tpPos);
                            BlockState blockAbove = level().getBlockState(above);

                            AABB targetBox = this.getBoundingBox().move(tpPos.getX() + 0.5 - this.getX(), tpPos.getY() - this.getY(), tpPos.getZ() + 0.5 - this.getZ());

                            boolean solidFloor = floor.isSolid();
                            boolean spaceAir = blockAt.isAir() && blockAbove.isAir();
                            boolean noFluid = blockAt.getFluidState().isEmpty() && blockAbove.getFluidState().isEmpty();
                            boolean notLeaves = !blockAt.is(BlockTags.LEAVES);
                            boolean noCollision = level().noCollision(this, targetBox);

                            if (solidFloor && spaceAir && noFluid && notLeaves && noCollision) {
                                this.teleportTo(tpPos.getX() + 0.5, tpPos.getY(), tpPos.getZ() + 0.5);
                                this.getNavigation().stop();
                                return;
                            }
                        }
                    }
                }
            }
        }

    }

    public void applyBabyAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);

        this.setHealth(this.getMaxHealth());
    }

    public void applyAppAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);

        this.setHealth(this.getMaxHealth());
    }

    public void applyAdultAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.33);

        this.setHealth(this.getMaxHealth());
    }

    /**
     * It can't attack other animals tamed by their own owner
     */
    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof TamableAnimal tam && tam.isTame()) {

            LivingEntity myOwner = this.getOwner();
            UUID thisOwnerUUID = myOwner != null ? myOwner.getUUID() : null;
            UUID targetOwner = tam.getOwnerUUID();

            if (targetOwner != null && thisOwnerUUID != null && targetOwner.equals(thisOwnerUUID)) {
                return false;
            }
        }
        return super.canAttack(target);
    }

    /**
     * It is allied to any other animals tamed by their own owner
     */
    @Override
    public boolean isAlliedTo(Entity other) {
        if (other instanceof TamableAnimal tam && tam.isTame()) {

            LivingEntity myOwner = this.getOwner();
            UUID thisOwnerUUID = myOwner != null ? myOwner.getUUID() : null;
            UUID targetOwner = tam.getOwnerUUID();

            if (targetOwner != null && thisOwnerUUID != null && targetOwner.equals(thisOwnerUUID)) {
                return true;
            }
        }
        return super.isAlliedTo(other);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("WCatMode", mode.ordinal());
        tag.putInt("Variant", this.getVariant());
        tag.putInt("KittingTicks", this.getKittingTicks());
        tag.putInt("Rank", this.getRank().ordinal());
        tag.putBoolean("kitBorn", kitBorn);
        tag.putBoolean("AppScale", this.isAppScale());
        tag.put("Inventory", inventory.createTag());


        if (this.getMate() != null) {
            tag.putString("Mate", Component.Serializer.toJson(this.getMate()));
        }
        if (this.getPrefix() != null) {
            tag.putString("Prefix", Component.Serializer.toJson(this.getPrefix()));
        }

        tag.putBoolean("ExpectingKits", this.isExpectingKits());
        tag.putInt("Gender", this.getGender());


        if (wanderCenter != null) {
            tag.putInt("WanderX", wanderCenter.getX());
            tag.putInt("WanderY", wanderCenter.getY());
            tag.putInt("WanderZ", wanderCenter.getZ());
            tag.putBoolean("HasWanderCenter", true);
        } else {
            tag.putBoolean("HasWanderCenter", false);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Rank")) {
            int value = tag.getInt("Rank");
            this.setRank(Rank.values()[value]);
        }

        inventory.fromTag(tag.getList("Inventory", Tag.TAG_COMPOUND));


        if (tag.contains("kitBorn")) {
            kitBorn = tag.getBoolean("kitBorn");
        }
        if (tag.contains("AppScale")) {
            this.setAppScale(tag.getBoolean("AppScale"));
        }
        if (tag.contains("KittingTicks")) {
            this.setKittingTicks(tag.getInt("KittingTicks"));
        }

        if (tag.contains("Gender")) {
            this.setGender(tag.getInt("Gender"));
        }

        if (tag.contains("Mate")) {
            Component mate = Component.Serializer.fromJson(tag.getString("Mate"));
            this.setMate(mate);
        }

        if (tag.contains("Prefix")) {
            Component prefix = Component.Serializer.fromJson(tag.getString("Prefix"));
            this.setPrefix(prefix);
        }

        if (tag.contains("ExpectingKits")) {
            this.setExpectingKits(tag.getBoolean("ExpectingKits"));
        }

        if (tag.contains("WCatMode")) {
            int modeIndex = tag.getInt("WCatMode");
            this.mode = CatMode.values()[modeIndex];
        }


        if (tag.contains("Variant")) {
            this.setVariant(tag.getInt("Variant"));
        }


        if (tag.getBoolean("HasWanderCenter")) {
            int x = tag.getInt("WanderX");
            int y = tag.getInt("WanderY");
            int z = tag.getInt("WanderZ");
            this.wanderCenter = new BlockPos(x, y, z);
        } else {
            this.wanderCenter = null;
        }
    }

    /**
     * This is in charge of animations.
     * Again, any questions about this just ask me personally
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>
                (this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>
                (this, "attackController", 0, this::attackPredicate));
        controllers.add(new AnimationController<>
                (this, "playerController", 0, this::playerPredicate));

    }

    @OnlyIn(Dist.CLIENT)
    private <T extends GeoAnimatable> PlayState playerPredicate(AnimationState<T> state) {

        Player player = Minecraft.getInstance().player;

        if (!AnimationClientData.isPlayerShape) {
            AnimationClientData.reset();
            return PlayState.CONTINUE;
        }

        int anim1 = AnimationClientData.getAnim1();
        int anim2 = AnimationClientData.getAnim2();
        int anim3 = AnimationClientData.getAnim3();
        int anim4 = AnimationClientData.getAnim4();
        int anim5 = AnimationClientData.getAnim5();
        int anim6 = AnimationClientData.getAnim6();

        if (PlayerShape.getCurrentShape(player) instanceof WCatEntity) {

            if (!animPlayed) {
                if (anim1 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.groom", Animation.LoopType.PLAY_ONCE));
                    animPlayed = true;
                } else if (anim2 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.stretch", Animation.LoopType.PLAY_ONCE));
                    animPlayed = true;
                } else if (anim3 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.scratch", Animation.LoopType.PLAY_ONCE));
                    animPlayed = true;
                } else if (anim4 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.attack", Animation.LoopType.PLAY_ONCE));
                    animPlayed = true;
                } else if (anim5 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.standstand", Animation.LoopType.PLAY_ONCE)
                            .then("animation.wcat.standidle", Animation.LoopType.LOOP));
                    animPlayed = true;
                } else if (anim6 == 1) {
                    state.getController().setAnimation(RawAnimation.begin()
                            .then("animation.wcat.sitlay", Animation.LoopType.PLAY_ONCE)
                            .then("animation.wcat.layidle", Animation.LoopType.LOOP));
                    animPlayed = true;
                }

            }

            if ((animPlayed && state.getController().hasAnimationFinished()) || state.isMoving()) {
                state.getController().setAnimation(RawAnimation.begin()
                        .then("animation.wcat.idle", Animation.LoopType.LOOP));
                animPlayed = false;
                AnimationClientData.reset();

                return PlayState.CONTINUE;
            }
        }

//        if (this.isAShape) {
//            LivingEntity cat = (LivingEntity) state.getAnimatable();
//            if (player == null) return  PlayState.CONTINUE;
//
//            if (cat == PlayerShape.getCurrentShape(player)) {
//
//            }
//        }


        return PlayState.CONTINUE;
    }


    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<T> state) {
        var controller = state.getController();
        if (this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 100;
            controller.setAnimation(RawAnimation.begin()
                    .then("animation.wcat.attack", Animation.LoopType.PLAY_ONCE));
            controller.forceAnimationReset();
            return PlayState.CONTINUE;
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isAttacking()) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

        LivingEntity cat = (LivingEntity) tAnimationState.getAnimatable();
        double speed = cat.getDeltaMovement().length();
        float animSpeed = (float) (speed * 6.0f);
        animSpeed = Mth.clamp(animSpeed * animSpeed, 0.2f, 1.5f);

        //float walkSpeed = this.walkAnimation.speed();
        //boolean moving = walkSpeed > 0.01F;


        if (this.isInWater()) {
            if (this.isSwimming()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.swim", Animation.LoopType.LOOP));
                tAnimationState.getController().setAnimationSpeed(1f);
            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.inwater", Animation.LoopType.LOOP));
                tAnimationState.getController().setAnimationSpeed(1.4f);
            }
            return PlayState.CONTINUE;
        }

        if (!this.onGround() && !this.isInWater()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().
                    then("animation.wcat.falling", Animation.LoopType.LOOP));
            animPlayed = false;
            return PlayState.CONTINUE;
        }

        // ADD || moving
        //The problem is basically that... It works okay but the this.isAShape has
        // to be a synced data. that and finding a way to determine speed animation.

        // The idea:
        // Make a synced data accesor for this.isAShape, and one for sprinting.
        // In Renderer, sync the player sprinting with the sprinting synced variable
        // And use it to set the animaions if synced isAShape
        if (tAnimationState.isMoving() && !this.isCrouching()) {

//            if (this.isAShape) {
//                if ((speed > 0.2039) && !this.isInWater()){
//                    tAnimationState.getController().setAnimation(RawAnimation.begin().
//                            then("animation.wcat.sprint", Animation.LoopType.LOOP));
//                    tAnimationState.getController().setAnimationSpeed(1f);
//                } else {
//                    tAnimationState.getController().setAnimation(RawAnimation.begin().
//                            then("animation.wcat.walk", Animation.LoopType.LOOP));
//
//                        tAnimationState.getController().setAnimationSpeed(1f);
//
//                }
//            } else {
//                if ((speed > 0.2039) && !this.isInWater() && !this.isAShape) {
//                    tAnimationState.getController().setAnimation(RawAnimation.begin().
//                            then("animation.wcat.sprint", Animation.LoopType.LOOP));
//
//                    tAnimationState.getController().setAnimationSpeed(0.185 * Math.exp(9.91 * speed));
//
//                } else {
//                    tAnimationState.getController().setAnimation(RawAnimation.begin().
//                            then("animation.wcat.walk", Animation.LoopType.LOOP));
//                     tAnimationState.getController().setAnimationSpeed(animSpeed);
//
//                }
//            }

            if ((speed > 0.2039) && !this.isInWater()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.sprint", Animation.LoopType.LOOP));

                tAnimationState.getController().setAnimationSpeed(0.185 * Math.exp(9.91 * speed));

            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.walk", Animation.LoopType.LOOP));
                tAnimationState.getController().setAnimationSpeed(animSpeed);

            }

            animPlayed = false;
            return PlayState.CONTINUE;
        }

        if (this.random.nextInt(1200) == 0 && !AnimationClientData.isPlayerShape) {

            int rand = this.random.nextInt(4);

            if (rand == 0 && !animPlayed) {

                tAnimationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.wcat.groom", Animation.LoopType.PLAY_ONCE));
                animPlayed = true;

            } else if (rand == 1 && !animPlayed) {

                tAnimationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.wcat.scratch", Animation.LoopType.PLAY_ONCE));
                animPlayed = true;

            } else if (rand == 2 && !animPlayed) {

                tAnimationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.wcat.stretch", Animation.LoopType.PLAY_ONCE));
                animPlayed = true;
            } else if (rand == 3 && !animPlayed) {

                tAnimationState.getController().setAnimation(RawAnimation.begin()
                        .then("animation.wcat.roll", Animation.LoopType.PLAY_ONCE));
                animPlayed = true;
            }
            tAnimationState.getController().setAnimationSpeed(1f);

            return PlayState.CONTINUE;

        }
        if (animPlayed && tAnimationState.getController().hasAnimationFinished()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.wcat.idle", Animation.LoopType.LOOP));
            tAnimationState.getController().setAnimationSpeed(1f);
            animPlayed = false;

            return PlayState.CONTINUE;
        }

        if (this.isCrouching()) {
            // ADD || moving
            if (tAnimationState.isMoving() && this.isCrouching()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.crouchingwalk", Animation.LoopType.LOOP));
                tAnimationState.getController().setAnimationSpeed(1f);
            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().
                        then("animation.wcat.crouchingidle", Animation.LoopType.LOOP));
                tAnimationState.getController().setAnimationSpeed(1f);
            }

            animPlayed = false;
            // ADD || !moving
        } else if (!animPlayed) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().
                    then("animation.wcat.idle", Animation.LoopType.LOOP));
            tAnimationState.getController().setAnimationSpeed(1f);
        }

        return PlayState.CONTINUE;

    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    /**
     * Sends a different message depending on the cat's mode.
     */
    private void sendModeMessage(Player player) {
        String name = this.getName().getString();
        switch (mode) {
            case SIT:
                player.displayClientMessage(Component.literal(name + " is staying."), true);
                break;
            case FOLLOW:
                player.displayClientMessage(Component.literal(name + " is following you."), true);
                break;
            case WANDER:
                player.displayClientMessage(Component.literal(name + " is wandering."), true);
                break;
        }
    }

    /**
     * Sends a different message depending on the cat's rank
     */
    private void sendRankMessage(Player player) {
        Rank r = this.getRank();
        String name = this.getName().getString();

        switch (r) {
            case NONE -> player.displayClientMessage(Component.literal(name + " is now a loner."), true);
            case KIT -> player.displayClientMessage(Component.literal(name + " is now a kit."), true);
            case APPRENTICE -> player.displayClientMessage(Component.literal(name + " is now an apprentice."), true);
            case WARRIOR -> player.displayClientMessage(Component.literal(name + " is now a warrior."), true);
            case MEDICINE -> player.displayClientMessage(Component.literal(name + " is now a medicine cat."), true);
        }
    }

    /**
     * This is called when the she-cat is kitting.
     * First, set a random number which will be the ammount of kits, then for every kit that will be born:
     * Reset love so it doesn't spawn kits infinitely.
     * Create a Wild Cat instance.
     * Set its position, age, set it tamed, set its rank, etc etc.
     * Then set its name.
     * If the owner is a player, send a message announcing that the kit was born.
     * Then set its variant, wander center, attributes
     * And finally: Spawn the kit
     * Tadaaaaa
     */
    private void Kitting() {
        String lastPrefix = "";
        if (!(this.level() instanceof ServerLevel server)) return;
        this.setExpectingKits(false);
        LivingEntity owner = this.getOwner();

        int litterSize = 1 + this.random.nextInt(3);

        for (int i = 0; i < litterSize; i++) {
            this.resetLove();
            WCatEntity kit = ModEntities.WCAT.get().create(server);

            if (kit != null) {
                kit.setPos(this.getX(), this.getY(), this.getZ());
                int minutes = WCEConfig.COMMON.KIT_GROWTH_MINUTES.get();
                int growingTicks = minutes * 20 * 60;
                kit.setAge(-growingTicks);
                kit.setTame(true);
                kit.setRank(Rank.KIT);
                kit.kitBorn = true;
                String finalName = "";

                if (!kit.hasCustomName()) {
                    int variant = kit.getVariant();
                    String[] prefixSet = getPrefixForVariant(variant);


                    String genderS;
                    if (kit.getGender() == 0) {
                        genderS = " ♂";
                    } else {
                        genderS = " ♀";
                    }

                    int k = kit.random.nextInt(prefixSet.length);

                    /**
                     * This is so that kits born in the same litter dont have the same prefix when they are born.
                     * Since all this can happen in the same tick, then they all might share the same name, that's why this exists.
                     */
                    if (lastPrefix.equals(prefixSet[k])) {
                        k = kit.random.nextInt(prefixSet.length);
                    }

                    lastPrefix = prefixSet[k];

                    finalName = prefixSet[k] + "kit" + genderS;
                    kit.setCustomName(Component.literal(finalName));
                    kit.setCustomNameVisible(true);

                    kit.setPrefix(Component.literal(prefixSet[k]));

                }


                if (owner instanceof Player player) {
                    String kitName = finalName;

                    kit.setOwnerUUID(player.getUUID());

                    owner.sendSystemMessage(Component.literal(kitName).withStyle(ChatFormatting.GREEN)
                            .append(Component.literal(" has been born!").withStyle(ChatFormatting.WHITE))
                    );


                }

                int randomVariant = this.random.nextInt(maxVariants);
                kit.setVariant(randomVariant);
                kit.wanderCenter = this.blockPosition();
                kit.applyBabyAttributes();


                server.addFreshEntity(kit);
            }
        }

    }

    /**
     * Called when the age is zero (when the cat grows from baby to adult)
     */
    private void onGrewUp() {
        Component prefix = this.getPrefix();
        if (prefix != null && this.isTame()) {
            String genderV = this.getGender() == 0 ? " ♂" : " ♀";
            int i = this.random.nextInt(SUFIX.length);

            String newName = prefix.getString() + SUFIX[i] + genderV;

            Entity owner = this.getOwner();
            if (owner instanceof Player) {

                owner.sendSystemMessage(
                        Component.empty()
                                .append(prefix)
                                .append(Component.literal("paw has become a warrior. "))
                                .append(prefix)
                                .append(Component.literal("paw will now be known as "))
                                .append(Component.literal(newName).withStyle(ChatFormatting.GOLD))
                                .append(Component.literal("!")
                                )
                );
            }

            this.setCustomName(Component.literal(newName));
            this.setCustomNameVisible(true);
            this.setAppScale(false);
            this.level().broadcastEntityEvent(this, (byte) 6);
            this.level().playSound(null, this.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.AMBIENT, 0.8f, 1.2f);


        }
    }

    /**
     * Different voice pitch depending on a cat's age and rank
     */
    @Override
    public float getVoicePitch() {
        return this.isBaby() ?
                (this.getRank() == APPRENTICE ?
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.3F
                        :
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F)
                :
                (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    /**
     * Makes a list of Wild Cats around certain area.
     * If there is a cat in the area of different gender than this cats gender, then return true.
     * Otherwise return false
     */
    private boolean hasValidMateNearby() {
        if (!this.isTame()) return false;
        if (this.isBaby()) return false;
        if (this.isExpectingKits()) return false;
        if (this.getRank() == MEDICINE) return false;

        List<WCatEntity> list = this.level().getEntitiesOfClass(
                WCatEntity.class,
                this.getBoundingBox().inflate(16)
        );

        for (WCatEntity cat : list) {
            if (cat != this && (cat.getGender() != this.getGender()) && !cat.isBaby() && !cat.isExpectingKits() && cat.getRank() != MEDICINE) {

                return true;
            }
        }

        return false;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(VARIANT, 0);
        this.entityData.define(SCALE, 1.0f);
        this.entityData.define(GENDER, 0);
        this.entityData.define(EXPECTING_KITS, false);
        this.entityData.define(KITTING_TICKS, 0);
        this.entityData.define(MATE, Optional.empty());
        this.entityData.define(PREFIX, Optional.empty());
        this.entityData.define(RANK, 0);
        this.entityData.define(AGE_SYNC, 0.0f);
        this.entityData.define(APP_SCALE, false);
        this.entityData.define(ATTACKING, false);

    }

    /**
     * Indicator to allow the cat to perform the attack animation
     */
    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    /**
     * Indicator to allow the cat to perform attack animation
     */
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }


    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    /**
     * Sets the variant and stores it in NBT.
     * Then change the functional size of the cat depending on the variant.
     * This is the collision box size, not the visual size.
     */
    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
//        float scale = switch (variant) {
//            case 0 -> 1f;
//            case 1 -> 1.2f;
//            case 2 -> 1.2f;
//            case 3 -> 1.2f;
//            case 4 -> 1f;
//            case 5 -> 1.2f;
//            case 6 -> 1.2f;
//            case 7 -> 1.4f;
//            case 8 -> 1f;
//            case 9 -> 1.2f;
//            case 10 -> 1.2f;
//            case 11 -> 1.2f;
//            case 12 -> 1.2f; //chestnutpatch
//            case 13 -> 1.2f; //ratstar
//            case 14 -> 1.2f; //twitchstream
//            case 15 -> 1.2f; //blazepit
//            case 16 -> 1.2f; //bengalpelt
//            case 17 -> 1.2f; //sparrowstar
//            case 18 -> 1f; //foxeater
//            case 19 -> 1.2f; //willowsong
//            default -> 1f;
//        };

        float scale = switch (variant) {
            case 0 -> 1f;
            case 1 -> 1f;
            case 2 -> 1f;
            case 3 -> 1f;
            case 4 -> 1f;
            case 5 -> 1f;
            case 6 -> 1f;
            case 7 -> 1f;
            case 8 -> 1f;
            case 9 -> 1f;
            case 10 -> 1f;
            case 11 -> 1f;
            case 12 -> 1f; //chestnutpatch
            case 13 -> 1f; //ratstar
            case 14 -> 1f; //twitchstream
            case 15 -> 1f; //blazepit
            case 16 -> 1f; //bengalpelt
            case 17 -> 1f; //sparrowstar
            case 18 -> 1f; //foxeater
            case 19 -> 1f; //willowsong
            default -> 1f;
        };


        this.entityData.set(SCALE, scale);
    }


//    public void setLeaderCallTarget(@Nullable Vec3 target) {
//        this.leaderCallTarget = target;
//    }
//    @Nullable
//    public Vec3 getLeaderCallTarget() {
//        return this.leaderCallTarget;
//    }


    @Override
    public int getExperienceReward() {
        return 25 + 5 * this.random.nextInt(3);
    }

    private boolean apprenticeAge = false;


    public Vec3 clientMovement = Vec3.ZERO;
    private Vec3 lastClientPos = Vec3.ZERO;

    @Override
    public void tick() {
        super.tick();


        /**
         * If the cat is not apprentice, and the age is bigger than half of the total time until fully grown, and this cat is tamed and it was born as a kit:
         * Then set apprenticeAge to true. This enables the dynamic visual size.
         * then change its name and apply the new attributes.
         */
        if (!this.level().isClientSide()) {
            if (catSniffTickCooldown > 0) catSniffTickCooldown--;

            if (scentDirection != null && scentDistance < scentMaxDistance) {
                scentTick++;

                if (scentTick % 2 == 0) {
                    Vec3 particlePos = scentStartPos.add(scentDirection.scale(scentDistance));

                    ((ServerLevel)this.level()).sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            particlePos.x, particlePos.y, particlePos.z,
                            1,
                            particlePos.x * scentDistance,
                            particlePos.y * scentDistance,
                            particlePos.z * scentDistance,
                            0.0
                    );


                    scentDistance += scentStep;
                }
            }

            if (scentDistance > scentMaxDistance) {
                scentDirection = null;
            }

            if (!apprenticeAge && this.getAge() >= -((getKitGrowthTimeMinutes() * 60 * 20) / 2) && this.kitBorn && this.isTame()) {
                apprenticeAge = true;

                String genderS;
                if (this.getGender() == 0) {
                    genderS = " ♂";
                } else {
                    genderS = " ♀";
                }

                String prefix = this.getPrefix().getString();
                String newName = prefix + "paw" + genderS;
                this.setCustomName(Component.literal(newName));
                this.setCustomNameVisible(true);
                this.setAppScale(true);

                Entity owner = this.getOwner();
                if (owner instanceof Player) {

                    owner.sendSystemMessage(
                            Component.empty()
                                    .append(prefix)
                                    .append(Component.literal("kit has become an apprentice. "))
                                    .append(prefix)
                                    .append(Component.literal("kit will now be known as "))
                                    .append(Component.literal(newName).withStyle(ChatFormatting.GOLD))
                                    .append(Component.literal("!")
                                    )
                    );

                }


                this.setRank(APPRENTICE);
                this.level().broadcastEntityEvent(this, (byte) 6);
                this.level().playSound(null, this.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.AMBIENT, 0.8f, 1.6f);
                this.kitBorn = false;
                this.applyAppAttributes();
            }
        }
        if (this.level().isClientSide) {
            Vec3 pos = this.position();
            clientMovement = pos.subtract(lastClientPos);
            lastClientPos = pos;

        }

    }

    public boolean isExpectingKits() {
        return this.entityData.get(EXPECTING_KITS);
    }

    public void setExpectingKits(boolean value) {
        this.entityData.set(EXPECTING_KITS, value);
    }

    public int getGender() {
        return this.entityData.get(GENDER);
    }

    public void setGender(int value) {
        this.entityData.set(GENDER, value);
    }

    public int getKittingTicks() {
        return this.entityData.get(KITTING_TICKS);
    }

    public void setKittingTicks(int value) {
        this.entityData.set(KITTING_TICKS, value);
    }

    public Component getMate() {
        return this.entityData.get(MATE).orElse(Component.literal("None"));
    }

    public void setMate(@Nullable Component name) {
        this.entityData.set(MATE, Optional.ofNullable(name));
    }

    public Component getPrefix() {
        return this.entityData.get(PREFIX).orElse(Component.literal("None"));
    }

    public void setPrefix(@Nullable Component prefix) {
        this.entityData.set(PREFIX, Optional.ofNullable(prefix));
    }

    public Rank getRank() {
        int value = this.entityData.get(RANK);
        if (value < 0 || value >= Rank.values().length) {
            return Rank.NONE;
        }
        return Rank.values()[value];
    }

    public void setRank(Rank rank) {
        this.entityData.set(RANK, rank.ordinal());
    }

    public boolean isMale() {
        return this.getGender() == 0;
    }

    public boolean isAppScale() {
        return this.entityData.get(APP_SCALE);
    }

    public void setAppScale(boolean value) {
        this.entityData.set(APP_SCALE, value);
    }

    /**
     * When a Wild Cat spawns, set a random gender and a random variant.
     */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData data = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);


        if (!this.level().isClientSide()) {
            this.setGender(this.random.nextInt(2));
        }
        int randomVariant = this.random.nextInt(maxVariants);
        this.setVariant(randomVariant);

        return data;
    }

    /**
     * If the cat is crouching, change its collision box.
     */
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float scale = this.entityData.get(SCALE);

        return switch (pose) {
            case CROUCHING -> super.getDimensions(Pose.STANDING).scale(scale * 0.9f);
            default -> super.getDimensions(pose).scale(scale);
        };
    }


    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (SCALE.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }


    @Override
    public boolean isFood(ItemStack stack) {
        if (stack.is(ModItems.CATMINT.get())) {
            return !this.isExpectingKits() && this.isTame();
        }
        return false;
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.WILDCAT_SCREAM.get();
    }

    /**
     * When the pose changes, refresh dimensions. This is what allows you to not glitch into walls and stuff if you are a Wild Cat
     */
    @Override
    public void setPose(Pose pose) {
        super.setPose(pose);
        this.refreshDimensions();
    }


}
