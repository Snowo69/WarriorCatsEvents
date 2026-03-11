package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.MossBedBlock;
import net.snowteb.warriorcats_events.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.Rank.MEDICINE;
import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.Rank.WARRIOR;
import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.emptyUUID;

public class WCGoals {

    public static class WCatFollowOwnerGoal extends Goal {

        private final TamableAnimal cat;
        private LivingEntity owner;
        private final double speed;
        private final float stopDistance;
        private final float startDistance;
        private final double angleOffset;


        public WCatFollowOwnerGoal(TamableAnimal cat, double speed, float stopDistance, float startDistance) {
            this.cat = cat;
            this.speed = speed;
            this.angleOffset = cat.getId() * 0.8;


            if (cat instanceof WCatEntity wCatEntity && (wCatEntity.getPersonality() == WCatEntity.Personality.INDEPENDENT || wCatEntity.getPersonality() == WCatEntity.Personality.SHY)) {
                if (wCatEntity.getPersonality() == WCatEntity.Personality.SHY) {
                    this.stopDistance = stopDistance * 5;
                    this.startDistance = startDistance * 5;
                } else {
                    this.stopDistance = stopDistance * 3;
                    this.startDistance = startDistance * 3;
                }
            } else {
                this.stopDistance = stopDistance;
                this.startDistance = startDistance;
            }

            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {

            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;


            if (!cat.isTame()) return false;
            if (wcat.mode != WCatEntity.CatMode.FOLLOW) return false;
            if (cat.isOrderedToSit()) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

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

            if (wcat.mode != WCatEntity.CatMode.FOLLOW) return false;
            if (cat.isOrderedToSit()) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            if (owner == null || !owner.isAlive()) return false;

            return cat.distanceTo(owner) > stopDistance;
        }

        @Override
        public void tick() {
            if (owner == null) return;

            double dist = cat.distanceTo(owner);

            cat.getLookControl().setLookAt(owner, 10.0F, cat.getMaxHeadXRot());
            applySeparation();

            if (dist > 25 && (cat.getOwner() != null && cat.getOwner().onGround())) {
                cat.teleportTo(owner.getX(), owner.getY(), owner.getZ());
                cat.getNavigation().stop();
//                return;
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

            double offsetX = Math.cos(angleOffset) * 0.6;
            double offsetZ = Math.sin(angleOffset) * 0.6;

            if (!hasGroundAhead(targetX, targetZ) || pathHasVoidAhead(owner) ||
                    (cat.distanceTo(owner) < 6.5D && isACatTooClose())) {
                cat.getNavigation().stop();
                return;
            }

            cat.getNavigation().moveTo(targetX + offsetX, targetY, targetZ + offsetZ, speed);
        }

        private boolean isACatTooClose() {
            AABB box = this.cat.getBoundingBox().inflate(0.6);
            List<WCatEntity> entities = cat.level().getEntitiesOfClass(
                    WCatEntity.class,
                    box,
                    kitty -> kitty.isAlive() && kitty != this.cat && kitty.mode == WCatEntity.CatMode.FOLLOW
            );
            return !entities.isEmpty();
        }


        private boolean pathHasVoidAhead(Entity owner) {
            Vec3 catPos = cat.position();
            Vec3 playerPos = owner.position();

            Vec3 dir = playerPos.subtract(catPos);
            double dist = dir.length();
            dir = dir.normalize();

            Level level = cat.level();

            for (double d = 0; d < Math.min(dist, 6); d += 0.5) {
                Vec3 sample = catPos.add(dir.scale(d));
                BlockPos pos = BlockPos.containing(sample.x, cat.getY(), sample.z);

                boolean hasGround = false;
                for (int i = 1; i <= 4; i++) {
                    if (!level.isEmptyBlock(pos.below(i))) {
                        hasGround = true;
                        break;
                    }
                }

                if (!hasGround) {
                    return true;
                }
            }

            return false;
        }


        private boolean hasGroundAhead(double targetX, double targetZ) {
            Level level = cat.level();

            BlockPos pos = BlockPos.containing(targetX, cat.getY(), targetZ);

            for (int i = 0; i < 4; i++) {
                BlockPos check = pos.below(i + 1);
                if (!level.isEmptyBlock(check)) {
                    return true;
                }
            }
            return false;
        }


        private void applySeparation() {
            AABB box = cat.getBoundingBox().inflate(0.3);
            List<WCatEntity> others = cat.level().getEntitiesOfClass(
                    WCatEntity.class,
                    box,
                    e -> e != cat
            );

            if (others.isEmpty()) return;

            double pushX = 0;
            double pushZ = 0;

            for (WCatEntity other : others) {
                double dx = cat.getX() - other.getX();
                double dz = cat.getZ() - other.getZ();
                double dist = Math.max(Math.sqrt(dx * dx + dz * dz), 0.001);

                pushX += dx / dist;
                pushZ += dz / dist;
            }

            cat.setDeltaMovement(
                    cat.getDeltaMovement().add(pushX * 0.05, 0, pushZ * 0.05)
            );
        }

    }

    public static class WCatCasualBlockSeekGoal extends Goal {

        private final WCatEntity cat;
        private final double speed;
        private final int baseRadius;
        private final double chance;
        private int cooldown = 0;

        private BlockPos targetPos = null;
        private Predicate<BlockState> targetPredicate;

        public WCatCasualBlockSeekGoal(WCatEntity cat, double speed, int baseRadius, double chance) {
            this.cat = cat;
            this.speed = speed;
            this.baseRadius = baseRadius;
            this.chance = chance;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cat.returnHomeFlag) return false;

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            if (cat.isOrderedToSit()) return false;

            if (this.targetPos != null) {
                if (targetPredicate != null &&
                        targetPredicate.test(cat.level().getBlockState(this.targetPos))) {
                    return false;
                }
                this.targetPos = null;
            }


            if (cat.mode != WCatEntity.CatMode.WANDER) return false;

            if (cat.isExpectingKits()) {
                if (cat.getRandom().nextDouble() >= this.chance * 2) return false;
            } else {
                if (cat.getRandom().nextDouble() >= this.chance) return false;
            }

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
                    cat.blockPosition().distSqr(cat.wanderCenter) > cat.getWanderRadius() * cat.getWanderRadius()) {
                cat.getNavigation().moveTo(cat.wanderCenter.getX(), cat.wanderCenter.getY(), cat.wanderCenter.getZ(), speed);
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
            this.cooldown = 400 + cat.getRandom().nextInt(4) * 80;
        }

        private Predicate<BlockState> defineTargetPredicate() {

            return switch (cat.getRank()) {
                case MEDICINE -> state -> state.is(ModBlocks.STONECLEFT.get());
                default -> state -> state.getBlock() instanceof MossBedBlock;
            };
        }

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

    public static class WCatBoundedWanderGoal extends WaterAvoidingRandomStrollGoal {

        private final WCatEntity cat;
        private int cooldown = 0;

        public WCatBoundedWanderGoal(WCatEntity cat, double speed) {
            super(cat, speed);
            this.cat = cat;
            this.setInterval(40);
        }


        @Override
        public boolean canUse() {
            if (this.cat.returnHomeFlag) return false;

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            WCatEntity wcat = cat;

            if (wcat.mode != WCatEntity.CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;


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
            int attempts = 7;

            if (this.cat.getRandom().nextFloat() < 0.1f) {
                BlockPos homePos = wcat.getHomePosition();
                if (homePos != null && !homePos.equals(BlockPos.ZERO)) {
                    Vec3 targetPos = Vec3.atCenterOf(homePos);
                    if (wcat.distanceToSqr(targetPos) < 20 * 20) {
                        return targetPos;

                    }
                }
            }

            for (int i = 0; i < attempts; i++) {

                double angle = cat.getRandom().nextDouble() * (Math.PI * 2);
                double r;
                if (cat.isTame()) {
                    r = cat.getRandom().nextDouble() * cat.getWanderRadius();
                } else {
                    r = cat.getRandom().nextDouble() * 32;
                }

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

            if (wcat.mode != WCatEntity.CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

            return !cat.getNavigation().isDone();
        }
    }

    public static class WCatGiveRandomItemGoal extends Goal {
        private final WCatEntity cat;
        private final double speedModifier;
        private static final TargetingConditions PLAYER_TARGET
                = TargetingConditions.forNonCombat().range(32f).ignoreLineOfSight();

        @Nullable
        private Player player;

        WCatGiveRandomItemGoal(WCatEntity cat) {
            this.cat = cat;
            this.speedModifier = 1f;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (cat.getMood() == WCatEntity.Mood.SAD || cat.getMood() == WCatEntity.Mood.STRESSED) return false;

            if (this.cat.getRank() != WARRIOR) return false;

            if (this.cat.mode != WCatEntity.CatMode.WANDER) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            if (this.cat.getRandom().nextFloat() > ((float) 1 / (20 * 60 * 15))) return false;

            this.player = this.cat.level().getNearestPlayer(PLAYER_TARGET, this.cat);

            if (this.player == null) return false;

            if (this.cat.getFriendshipLevel(this.player.getUUID()) < 65) return false;

            return this.cat.getTarget() != this.player;

        }

        public boolean canContinueToUse() {
            return this.player != null && this.cat.distanceToSqr(this.player) < 32 * 32;
        }

        public void start() {
            this.cat.getNavigation().moveTo(this.player, this.speedModifier);
        }

        public void stop() {
            this.player = null;
            this.cat.getNavigation().stop();
        }

        public void tick() {
            this.cat.getLookControl().setLookAt(this.player, (float) (this.cat.getMaxHeadYRot() + 20), (float) this.cat.getMaxHeadXRot());
            if (this.cat.distanceToSqr(this.player) < 6.25D) {
                this.cat.getNavigation().stop();
                performGiveItemInteraction();
                String message = cat.getRandomGiftDialogue(this.cat.getPersonality());
                this.cat.sendInteractionMessage(this.player.getUUID(), message);
                stop();
            } else {
                this.cat.getNavigation().moveTo(this.player, this.speedModifier);
            }

        }

        private void performGiveItemInteraction() {
            ItemStack item = new ItemStack(getPersonalityItemPool(), 2 + this.cat.getRandom().nextInt(3));
            ItemEntity itemEntity = new ItemEntity(this.player.level(), this.cat.getX(), this.cat.getY() + 0.5, this.cat.getZ(), item);
            itemEntity.getPersistentData().putBoolean("gift_by_cat", true);

            Vec3 look = this.cat.getLookAngle();
            double impulse = 0.35;
            itemEntity.setDeltaMovement(look.x * impulse, 0.2, look.z * impulse);

            itemEntity.setDefaultPickUpDelay();

            this.player.level().addFreshEntity(itemEntity);
        }

        private Item getPersonalityItemPool() {
            Item item = Items.COD;
            int randomPool = this.cat.getRandom().nextInt(4);

            if (randomPool == 0) {
                switch (this.cat.getPersonality()) {
                    case NONE -> item = Items.COD;
                    case AMBITIOUS -> item = Items.DIAMOND;
                    case CALM -> item = Items.COD;
                    case FRIENDLY -> item = ModItems.ANIMAL_TOOTH.get();
                    case CAUTIOUS -> item = ModItems.DOCK.get();
                    case RECKLESS -> item = ModItems.ANIMAL_TEETH.get();
                    case GRUMPY -> item = ModItems.ANIMAL_TOOTH.get();
                    case HUMBLE -> item = Items.GLOW_BERRIES;
                    case SHY -> item = Items.SALMON;
                    case INDEPENDENT -> item = Items.CHICKEN;
                }
            } else if (randomPool == 1) {
                switch (this.cat.getPersonality()) {
                    case NONE -> item = Items.COD;
                    case AMBITIOUS -> item = Items.EMERALD;
                    case CALM -> item = Items.SALMON;
                    case FRIENDLY -> item = Items.RABBIT;
                    case CAUTIOUS -> item = ModItems.CATMINT.get();
                    case RECKLESS -> item = Items.BEEF;
                    case GRUMPY -> item = ModItems.MOUSE_FOOD.get();
                    case HUMBLE -> item = ModItems.SQUIRREL_FOOD.get();
                    case SHY -> item = Items.TROPICAL_FISH;
                    case INDEPENDENT -> item = ModItems.MOUSE_FOOD.get();
                }
            } else if (randomPool == 2) {
                switch (this.cat.getPersonality()) {
                    case NONE -> item = Items.COD;
                    case AMBITIOUS -> item = ModItems.PIGEON_FOOD.get();
                    case CALM -> item = ModItems.ANIMAL_TEETH.get();
                    case FRIENDLY -> item = ModItems.SQUIRREL_FOOD.get();
                    case CAUTIOUS -> item = ModItems.GLOW_SHROOM.get();
                    case RECKLESS -> item = Items.DIAMOND;
                    case GRUMPY -> item = ModItems.DOCK_LEAVES.get();
                    case HUMBLE -> item = ModItems.ANIMAL_TOOTH.get();
                    case SHY -> item = ModItems.LEAF_MANE.get();
                    case INDEPENDENT -> item = Items.DIAMOND;
                }
            } else if (randomPool == 3) {
                switch (this.cat.getPersonality()) {
                    case NONE -> item = Items.COD;
                    case AMBITIOUS -> item = ModItems.ANIMAL_TEETH.get();
                    case CALM -> item = ModItems.DOCK_LEAVES.get();
                    case FRIENDLY -> item = Items.IRON_INGOT;
                    case CAUTIOUS -> item = ModItems.LEAF_MANE.get();
                    case RECKLESS -> item = Items.EMERALD;
                    case GRUMPY -> item = Items.GLOW_BERRIES;
                    case HUMBLE -> item = Items.PORKCHOP;
                    case SHY -> item = ModItems.FLOWER_CROWN.get();
                    case INDEPENDENT -> item = ModItems.ANIMAL_TEETH.get();
                }
            }

            return item;
        }

    }

    public static class WCatRunWithPlayerGoal extends Goal {
        private final WCatEntity cat;
        private final double speedModifier;
        private static final TargetingConditions PLAYER_TARGET
                = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();

        @Nullable
        private Player player;

        WCatRunWithPlayerGoal(WCatEntity cat, double pSpeedModifier) {
            this.cat = cat;
            this.speedModifier = pSpeedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (this.cat.getRank() != WARRIOR) return false;

            if (this.cat.mode != WCatEntity.CatMode.FOLLOW) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            this.player = this.cat.level().getNearestPlayer(PLAYER_TARGET, this.cat);

            if (this.player == null) return false;

            if (this.cat.getFriendshipLevel(this.player.getUUID()) < 80) return false;

            return this.player.isSprinting() && this.cat.getTarget() != this.player;

        }

        public boolean canContinueToUse() {
            return this.player != null && this.player.isSprinting() && this.cat.distanceToSqr(this.player) < 256.0D;
        }

        public void start() {
            this.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, false, false), this.cat);
            this.cat.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, false), this.cat);
        }

        public void stop() {
            this.player = null;
            this.cat.getNavigation().stop();
        }

        public void tick() {
            this.cat.getLookControl().setLookAt(this.player, (float) (this.cat.getMaxHeadYRot() + 20), (float) this.cat.getMaxHeadXRot());
            if (this.cat.distanceToSqr(this.player) < 6.25D) {
                this.cat.getNavigation().stop();
            } else {
                this.cat.getNavigation().moveTo(this.player, this.speedModifier);
            }

            if (this.player.isSprinting() && this.player.level().random.nextInt(6) == 0) {
                this.player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, false, false), this.cat);
                this.cat.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, false), this.cat);
            }

        }
    }

    public static class WCatLeaderCallsGoal extends Goal {
        private final WCatEntity cat;
        private final double speed;
        private BlockPos ownerPosition;
        private static final int BASE_OBEY_TICKS = 140;
        private int obeyingLeaderCallForTicks = BASE_OBEY_TICKS;

        public WCatLeaderCallsGoal(WCatEntity cat) {
            this.cat = cat;
            this.speed = 1.2f;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!(cat.leaderCallingToSitFlag || cat.leaderCallingToFollowFlag)) return false;

            if (cat.getOwner() == null) return false;

            this.ownerPosition = cat.getOwner().blockPosition();

            if (cat.distanceToSqr(ownerPosition.getX(), ownerPosition.getY(), ownerPosition.getZ()) > 48 * 48)
                return false;

            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (ownerPosition == null) return false;

            if (cat.distanceToSqr(ownerPosition.getX(), ownerPosition.getY(), ownerPosition.getZ()) > 48 * 48) {
                return false;
            }

            return cat.distanceToSqr(ownerPosition.getX(), ownerPosition.getY(), ownerPosition.getZ()) > 5 * 5;
        }

        @Override
        public void start() {
            this.cat.returnHomeFlag = false;
            this.cat.mode = WCatEntity.CatMode.WANDER;
//            this.cat.getNavigation().moveTo(ownerPosition.getX(), ownerPosition.getY(), ownerPosition.getZ(), speed);
        }

        @Override
        public void stop() {
            if (cat.leaderCallingToSitFlag) {
                this.obeyingLeaderCallForTicks = BASE_OBEY_TICKS;
                cat.leaderCallingToFollowFlag = false;
                cat.leaderCallingToSitFlag = false;
                cat.mode = WCatEntity.CatMode.SIT;
                cat.setOrderedToSit(true);
                cat.lookAtLeaderFlag = true;
                this.ownerPosition = null;
            }
            if (cat.leaderCallingToFollowFlag) {
                this.obeyingLeaderCallForTicks = BASE_OBEY_TICKS;
                cat.leaderCallingToFollowFlag = false;
                cat.leaderCallingToSitFlag = false;
                cat.mode = WCatEntity.CatMode.FOLLOW;
                cat.setOrderedToSit(false);
                this.ownerPosition = null;
            }

        }

        @Override
        public void tick() {

            if (ownerPosition == null) return;

            double dist = cat.distanceToSqr(
                    ownerPosition.getX(),
                    ownerPosition.getY(),
                    ownerPosition.getZ()
            );

//            if (this.cat.obeyingLeaderCallForTicks > 200 && this.startCountingObeyTicks) {
//            }

            if (dist < 5.0 || this.obeyingLeaderCallForTicks <= 0) {
                stop();
            } else {
                this.obeyingLeaderCallForTicks--;
                cat.getNavigation().moveTo(
                        ownerPosition.getX(),
                        ownerPosition.getY(),
                        ownerPosition.getZ(),
                        speed
                );
            }
        }

    }

    public static class WCatReturnHomeGoal extends Goal {
        private final WCatEntity cat;
        private final double speed;
        private BlockPos homeTarget;
        private int stuckTicks = 0;
        private Vec3 lastPos = Vec3.ZERO;

        private int tickCounterUntilHorizontalImpulse;
        private boolean countUntilHorizontalImpulse;


        public WCatReturnHomeGoal(WCatEntity cat, double speed) {
            this.cat = cat;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!cat.returnHomeFlag) return false;

            this.homeTarget = cat.getHomePosition();

            if (this.homeTarget == null || this.homeTarget.equals(BlockPos.ZERO)) return false;

            if (cat.distanceToSqr(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ()) > 160000) return false;

            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (homeTarget == null) return false;

            if (cat.distanceToSqr(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ()) > 160000) {
                return false;
            }

            return cat.mode == WCatEntity.CatMode.WANDER && cat.distanceToSqr(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ()) > 2 * 2;
        }

        @Override
        public void start() {
            this.cat.getNavigation().moveTo(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ(), speed);
        }

        @Override
        public void stop() {
            cat.wanderCenter = cat.blockPosition();
            cat.mode = WCatEntity.CatMode.WANDER;
            cat.returnHomeFlag = false;
            this.homeTarget = null;
            cat.getNavigation().stop();
        }

        @Override
        public void tick() {

            if (homeTarget == null) return;

            double dist = cat.distanceToSqr(
                    homeTarget.getX(),
                    homeTarget.getY(),
                    homeTarget.getZ()
            );

            if (dist < 2.0) {
                cat.getNavigation().moveTo(
                        homeTarget.getX(),
                        homeTarget.getY(),
                        homeTarget.getZ(),
                        speed
                );
                cat.returnHomeFlag = false;
                return;
            }

            if (cat.getNavigation().isDone()) {
                cat.getNavigation().moveTo(
                        homeTarget.getX(),
                        homeTarget.getY(),
                        homeTarget.getZ(),
                        speed
                );
            }

            Vec3 current = cat.position();

            if (current.distanceToSqr(lastPos) < 0.01) {
                stuckTicks++;
            } else {
                stuckTicks = 0;
                lastPos = current;
            }

            if (this.countUntilHorizontalImpulse) {
                this.tickCounterUntilHorizontalImpulse++;
                if (this.tickCounterUntilHorizontalImpulse >= 7) {
                    Vec3 lookAngleImpulse = cat.getLookAngle().normalize().scale(0.8);
                    Vec3 impulse = new Vec3(lookAngleImpulse.x, 0.2, lookAngleImpulse.z);
                    cat.setDeltaMovement(cat.getDeltaMovement().add(impulse));
                    cat.hasImpulse = true;
                    this.countUntilHorizontalImpulse = false;
                    this.tickCounterUntilHorizontalImpulse = 0;
                    this.cat.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 50, 0, false, false));
                }
            }


            if (stuckTicks >= 40) {
                if (cat.horizontalCollision || cat.verticalCollision) {
                    Vec3 lookAngleImpulse = cat.getLookAngle().normalize().scale(2);

                    Vec3 impulse = new Vec3(lookAngleImpulse.x, 0.8, lookAngleImpulse.z);

                    cat.setDeltaMovement(cat.getDeltaMovement().add(impulse));

                    cat.hasImpulse = true;
                    this.countUntilHorizontalImpulse = true;
                }

                stuckTicks = 0;
            }

        }

    }

    public static class WCatLookAtPlayerGoal extends LookAtPlayerGoal {
        private final WCatEntity cat;

        public WCatLookAtPlayerGoal(WCatEntity cat, Class<? extends LivingEntity> pLookAtType, float pLookDistance) {
            super(cat, pLookAtType, pLookDistance);
            this.cat = cat;
        }

        @Override
        public boolean canUse() {
            if (this.cat.isAnImage()) return false;
            return super.canUse();
        }

        @Override
        public void tick() {
            if (cat.isAnImage()) return;
            super.tick();
        }

    }

    public static class WCatMoveToMateGoal extends Goal {
        private final WCatEntity cat;
        private LivingEntity targetMate;
        private boolean isCloseToMate = false;
        private static final int BASE_DURATION = 100;
        private int interactionTickCount = BASE_DURATION;

        public WCatMoveToMateGoal(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (cat.returnHomeFlag) return false;

            if (cat.mode != WCatEntity.CatMode.WANDER) return false;

            if (cat.getMateUUID() == null) return false;
            if (cat.getMateUUID().equals(emptyUUID)) return false;

            if (cat.getMood() == WCatEntity.Mood.SAD || cat.getMood() == WCatEntity.Mood.STRESSED) return false;

            if (cat.getRandom().nextFloat() > 0.0001667f) return false;

            return findMate();
        }

        @Override
        public boolean canContinueToUse() {
            return targetMate != null && cat.mode == WCatEntity.CatMode.WANDER;
        }

        @Override
        public void tick() {

            if (cat.distanceToSqr(targetMate) > 1f && !this.isCloseToMate) {
                cat.getNavigation().moveTo(targetMate.getX(), targetMate.getY(), targetMate.getZ(), 1f);
            } else if (!this.isCloseToMate) {
                this.isCloseToMate = true;
                this.interactionTickCount = BASE_DURATION;
                targetMate.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0, false, false));
            }

            if (this.isCloseToMate && this.interactionTickCount > 0) {
                this.interactionTickCount--;
                ServerLevel sLevel = ((ServerLevel) cat.level());
                cat.getLookControl().setLookAt(targetMate, (float) (cat.getMaxHeadYRot() + 20), (float) cat.getMaxHeadXRot());

                float chance = cat.getRandom().nextFloat();
                if (chance <= 0.05f) {
                    sLevel.sendParticles(ParticleTypes.HEART, cat.getX(), cat.getY(), cat.getZ(), 2, 0.5f, 0.5f, 0.5f, 0.1f);
                    if (chance < 0.03) {
                        sLevel.playSound(null, cat.blockPosition(), SoundEvents.CAT_PURR, SoundSource.NEUTRAL, 0.4F, 1.0F);
                    }
                }

                if (cat.distanceToSqr(targetMate) > 1f) {
                    cat.getNavigation().moveTo(targetMate.getX(), targetMate.getY(), targetMate.getZ(), 1f);
                }
            }
            if (this.interactionTickCount <= 0) {
                stop();
            }
        }

        @Override
        public void stop() {
            if (targetMate != null && this.interactionTickCount <= 0) {
                if (!cat.level().isClientSide()) {
                    ServerLevel sLevel = ((ServerLevel) cat.level());

                    sLevel.sendParticles(ParticleTypes.HEART, cat.getX(), cat.getY(), cat.getZ(), 2, 0.5f, 0.5f, 0.5f, 0.1f);
                    cat.playSound(SoundEvents.CAT_PURR);
                    this.targetMate = null;
                    this.interactionTickCount = BASE_DURATION;
                    this.isCloseToMate = false;
                }
            }
            super.stop();
        }

        private boolean findMate() {
            AABB box = cat.getBoundingBox().inflate(28);

            List<LivingEntity> cats = cat.level().getEntitiesOfClass(
                    LivingEntity.class,
                    box
            );

            for (LivingEntity potentialMate : cats) {
                if (potentialMate == cat) continue;
                if (!(potentialMate instanceof WCatEntity || potentialMate instanceof Player)) continue;

                UUID potentialMateUUID = potentialMate.getUUID();
                if (!potentialMateUUID.equals(cat.getMateUUID())) continue;

                this.targetMate = potentialMate;
                return true;

            }

            return false;
        }

    }

    public static class WCatRandomLookAroundGoal extends RandomLookAroundGoal {
        private final WCatEntity cat;

        public WCatRandomLookAroundGoal(WCatEntity pMob) {
            super(pMob);
            this.cat = pMob;
        }

        @Override
        public boolean canUse() {
            if (this.cat.lookAtLeaderFlag && this.cat.isLookingAtLeader) return false;
            if (this.cat.isAnImage()) return false;
            return super.canUse();
        }

        @Override
        public void tick() {
            if (cat.isAnImage()) return;
            super.tick();
        }

    }

    public static class WCatSeekShelterGoal extends Goal {
        private final WCatEntity cat;
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        private final double speedModifier;
        private final Level level;

        public WCatSeekShelterGoal(WCatEntity cat, double pSpeedModifier) {
            this.cat = cat;
            this.speedModifier = pSpeedModifier;
            this.level = cat.level();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cat.returnHomeFlag) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            if (cat.mode != WCatEntity.CatMode.WANDER || cat.getTarget() != null) return false;
            BlockPos pos = cat.blockPosition();

            boolean isExposed = this.level.canSeeSky(pos);
            boolean isRaining = this.level.isThundering() || this.level.isRainingAt(pos);

            if (isRaining && isExposed) return setWantedPos();

            return false;
        }

        public boolean canContinueToUse() {
            return !this.cat.getNavigation().isDone();
        }

        public void start() {
            this.cat.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        protected boolean setWantedPos() {
            BlockPos pos = this.findShelter();
            if (pos == null) {
                return false;
            } else {
                this.wantedX = pos.getX();
                this.wantedY = pos.getY();
                this.wantedZ = pos.getZ();
                return true;
            }
        }

        @Nullable
        private BlockPos findShelter() {
            BlockPos origin = cat.blockPosition();

            for (int i = 0; i < 12; i++) {
                BlockPos pos = origin.offset(
                        cat.getRandom().nextInt(14) - 7,
                        cat.getRandom().nextInt(6) - 2,
                        cat.getRandom().nextInt(14) - 7
                );

                if (!cat.level().getBlockState(pos.below()).entityCanStandOn(cat.level(), pos.below(), cat))
                    continue;

                if (!cat.level().isEmptyBlock(pos) || !cat.level().isEmptyBlock(pos.above()))
                    continue;

                if (cat.level().canSeeSky(pos))
                    continue;

                return pos;
            }

            return null;
        }

    }

    public static class WCatMedicineHealsCats extends Goal {

        private final WCatEntity cat;
        private LivingEntity target;
        private int cooldown = 400;
        private int keepTicks = 0;
        private final int BASE_COOLDOWN = 600;
        private int healCooldown = 0;

        public WCatMedicineHealsCats(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cat.returnHomeFlag) return false;

            if (cat.getMood() == WCatEntity.Mood.SAD) return false;

            if (cat.getRank() != MEDICINE) return false;

            if (target != null && (!target.isAlive() || cat.distanceTo(target) > 25D)) {
                target = null;
            }

            if (cat.isOrderedToSit()) return false;

            if (target != null) return false;

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            target = findNearestInjuredClanmate();

            if (target != null) {
                cooldown = ((BASE_COOLDOWN + cat.getRandom().nextInt(5) * 20));
                if (!cat.hasPoultice()) {
                    if (!cat.tryMakePoultice()) {
                        cooldown = ((BASE_COOLDOWN + cat.getRandom().nextInt(5) * 20));
                        ;
                        return false;
                    }
                }
                return true;
            }

            cooldown = 600;
            return false;
        }


        @Override
        public boolean canContinueToUse() {
            if (target != null && cat.distanceTo(target) > 20D) return false;
            return target != null
                    && target.isAlive()
                    && !cat.isOrderedToSit()
                    && cat.getRank() == MEDICINE
                    && cat.hasPoultice();
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
            if (healCooldown > 0) healCooldown--;

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
            WCatEntity medicine = this.cat;
            LivingEntity injured = this.target;
            if (medicine.distanceTo(injured) < 1.52D && healCooldown <= 0) {
                if (medicine.tryHealClanmante(injured)) {


                    healCooldown = 20;

                    if (target.getHealth() >= target.getMaxHealth() - 4) {
                        stop();
                        target = null;
                    }

                    injured.level().playSound(
                            null, injured.getX(), injured.getY(), injured.getZ(),
                            SoundEvents.SLIME_JUMP, SoundSource.NEUTRAL,
                            0.5F, 1.5F + cat.getRandom().nextFloat() * 0.2F
                    );
                    injured.level().playSound(
                            null, injured.getX(), injured.getY(), injured.getZ(),
                            SoundEvents.CAT_EAT, SoundSource.NEUTRAL,
                            0.6F, 0.9F + cat.getRandom().nextFloat() * 0.2F
                    );
                    ((ServerLevel) injured.level()).sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            injured.getX(), injured.getY() + injured.getBbHeight() * 0.6,
                            injured.getZ(), 30, 0.3, 0.3, 0.3, 1
                    );

                }
            } else {

                if (medicine.distanceTo(injured) > 30D) {
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
//        private WCatEntity findNearestInjuredClanmate() {
//            AABB box = cat.getBoundingBox().inflate(16);
//
//            List<WCatEntity> cats = cat.level().getEntitiesOfClass(
//                    WCatEntity.class,
//                    box
//            );
//
//            double closestDist = Double.MAX_VALUE;
//            WCatEntity closest = null;
//
//            for (WCatEntity catToHeal : cats) {
//                if (catToHeal.getHealth() >= catToHeal.getMaxHealth() - 4) continue;
//                if (catToHeal == cat) continue;
//                if (catToHeal.getOwner() != cat.getOwner()) continue;
//
//                double dist = cat.distanceToSqr(catToHeal);
//                if (dist < closestDist) {
//                    closestDist = dist;
//                    closest = catToHeal;
//                }
//            }
//
//            return closest;
//        }
        private LivingEntity findNearestInjuredClanmate() {
            AABB box = cat.getBoundingBox().inflate(28);

            List<LivingEntity> cats = cat.level().getEntitiesOfClass(
                    LivingEntity.class,
                    box
            );

            double closestDist = Double.MAX_VALUE;
            LivingEntity closest = null;

            for (LivingEntity catToHeal : cats) {
                if (catToHeal.getHealth() >= catToHeal.getMaxHealth() - 4) continue;
                if (catToHeal == cat) continue;
                if (!(catToHeal instanceof WCatEntity || catToHeal instanceof Player)) continue;
                if (catToHeal instanceof WCatEntity kitty) {
                    if (kitty.getOwner() != cat.getOwner()) continue;
                }
                if (catToHeal instanceof Player playerKitty) {
                    if (cat.getFriendshipLevel(playerKitty.getUUID()) < 60) continue;
                }

                double dist = cat.distanceToSqr(catToHeal);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = catToHeal;
                }
            }

            return closest;
        }

    }

    public static class WCatPickupItemGoal extends Goal {

        private final WCatEntity cat;
        private ItemEntity target;
        private int cooldown = 0;
        private int keepTicks = 0;
        private static final int BASE_COOLDOWN = 45;
        private float growingMinimumDistance = 0;

        public WCatPickupItemGoal(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (cat.returnHomeFlag) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

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
                cooldown = (int) ((BASE_COOLDOWN + cat.getRandom().nextInt(10)) * cat.itemPickupChanceMultiplier());
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
            this.growingMinimumDistance = 0;
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
            if (cat.distanceTo(target) < 1.42D + this.growingMinimumDistance) {
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

                    this.growingMinimumDistance += 0.05F;

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
            AABB box = cat.getBoundingBox().inflate(12);

            List<ItemEntity> items = cat.level().getEntitiesOfClass(
                    ItemEntity.class,
                    box
            );

            double closestDist = Double.MAX_VALUE;
            ItemEntity closest = null;

            for (ItemEntity item : items) {
                ItemStack stack = item.getItem();

                if (!cat.canAccept(stack)) continue;
                if (item.getPersistentData().getBoolean("gift_by_cat")) continue;


                double dist = cat.distanceToSqr(item);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = item;
                }
            }

            return closest;
        }

    }


}
