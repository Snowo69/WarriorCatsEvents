//package net.snowteb.warriorcats_events.entity.custom;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.util.Mth;
//import net.minecraft.world.DifficultyInstance;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.control.LookControl;
//import net.minecraft.world.entity.ai.control.MoveControl;
//import net.minecraft.world.entity.ai.goal.*;
//import net.minecraft.world.entity.ai.targeting.TargetingConditions;
//import net.minecraft.world.entity.animal.Animal;
//import net.minecraft.world.entity.animal.Cat;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.monster.Enemy;
//import net.minecraft.world.entity.monster.Phantom;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.ServerLevelAccessor;
//import net.minecraft.world.level.levelgen.Heightmap;
//import net.minecraft.world.phys.Vec3;
//import net.snowteb.warriorcats_events.item.ModItems;
//import software.bernie.geckolib.animatable.GeoEntity;
//import software.bernie.geckolib.core.animatable.GeoAnimatable;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.*;
//import software.bernie.geckolib.core.animation.AnimationState;
//import software.bernie.geckolib.core.object.PlayState;
//import software.bernie.geckolib.util.GeckoLibUtil;
//import tocraft.walkers.api.PlayerShape;
//
//import java.util.Comparator;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.function.Predicate;
//
//public class EagleEntity extends Phantom implements GeoEntity {
//
//    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//
//    EagleEntity.AttackPhase attackPhase = EagleEntity.AttackPhase.CIRCLE;
//    Vec3 moveTargetPoint = Vec3.ZERO;
//    BlockPos anchorPoint = BlockPos.ZERO;
//
//    private boolean isLatching = false;
//    private int latchingTimer = 300;
//
//
//    public EagleEntity(EntityType<? extends EagleEntity> type, Level world) {
//        super(type, world);
//        this.moveControl = new EagleMoveControl(this);
//        this.lookControl = new EagleLookControl(this);
//    }
//
//    public static enum AttackPhase {
//        CIRCLE,
//        SWOOP;
//    }
//
//
//    public class EagleAttackStrategy extends Goal {
//        private int nextSweepTick;
//
//        public boolean canUse() {
//            LivingEntity livingentity = EagleEntity.this.getTarget();
//            return livingentity != null ? EagleEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT) : false;
//        }
//
//        public void start() {
//            this.nextSweepTick = this.adjustedTickDelay(10);
//            EagleEntity.this.attackPhase = EagleEntity.AttackPhase.CIRCLE;
//            this.setAnchorAboveTarget();
//        }
//
//        public void stop() {
//            EagleEntity.this.anchorPoint = EagleEntity.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EagleEntity.this.anchorPoint).above(10 + EagleEntity.this.random.nextInt(20));
//        }
//
//        public void tick() {
//            if (EagleEntity.this.attackPhase == EagleEntity.AttackPhase.CIRCLE) {
//                --this.nextSweepTick;
//                if (this.nextSweepTick <= 0) {
//                    EagleEntity.this.attackPhase = EagleEntity.AttackPhase.SWOOP;
//                    this.setAnchorAboveTarget();
//                    this.nextSweepTick = this.adjustedTickDelay((8 + EagleEntity.this.random.nextInt(4)) * 20);
//                    EagleEntity.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + EagleEntity.this.random.nextFloat() * 0.1F);
//                }
//            }
//
//        }
//
//        private void setAnchorAboveTarget() {
//            EagleEntity.this.anchorPoint = EagleEntity.this.getTarget().blockPosition().above(20 + EagleEntity.this.random.nextInt(20));
//            if (EagleEntity.this.anchorPoint.getY() < EagleEntity.this.level().getSeaLevel()) {
//                EagleEntity.this.anchorPoint = new BlockPos(EagleEntity.this.anchorPoint.getX(), EagleEntity.this.level().getSeaLevel() + 1, EagleEntity.this.anchorPoint.getZ());
//            }
//
//        }
//    }
//
//    public abstract class EagleMoveToTarget extends Goal {
//        public EagleMoveToTarget() {
//            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
//        }
//
//        protected boolean touchingTarget() {
//            return EagleEntity.this.moveTargetPoint.distanceToSqr(EagleEntity.this.getX(), EagleEntity.this.getY(), EagleEntity.this.getZ()) < 4.0D;
//        }
//    }
//
//    public class EagleSweepAttackGoal extends EagleMoveToTarget {
//
//        public boolean canUse() {
//            return EagleEntity.this.getTarget() != null && EagleEntity.this.attackPhase == AttackPhase.SWOOP && !EagleEntity.this.isLatching;
//        }
//
//        public boolean canContinueToUse() {
//            LivingEntity livingentity = EagleEntity.this.getTarget();
//            if (livingentity == null) {
//                return false;
//            } else if (!livingentity.isAlive()) {
//                return false;
//            } else {
//                if (livingentity instanceof Player player) {
//                    if (!(PlayerShape.getCurrentShape(player) instanceof WCatEntity)) {
//                        return false;
//                    }
//
//                    if (player.isSpectator() || player.isCreative()) {
//                        return false;
//                    }
//                } else if (livingentity instanceof WCatEntity cat) {
//                    if (!cat.isBaby()) {
//                        return false;
//                    }
//                }
//
//
//                if (!this.canUse()) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        public void start() {
//        }
//
//        public void stop() {
//            EagleEntity.this.setTarget(null);
//            EagleEntity.this.attackPhase = EagleEntity.AttackPhase.CIRCLE;
//        }
//
//        public void tick() {
//            LivingEntity livingentity = EagleEntity.this.getTarget();
//            if (livingentity != null) {
//
//                EagleEntity.this.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
//                if (EagleEntity.this.getBoundingBox().inflate(0.2F).intersects(livingentity.getBoundingBox())) {
//                    EagleEntity.this.doHurtTarget(livingentity);
//
//                    livingentity.startRiding(EagleEntity.this);
//                    EagleEntity.this.isLatching = true;
//                    if (livingentity instanceof WCatEntity cat && cat.isBaby()) {
//                        EagleEntity.this.latchingTimer = 60;
//                    }
//                    EagleEntity.this.moveTargetPoint =
//                            EagleEntity.this.position().add(0, 20, 0);
//
//                    EagleEntity.this.attackPhase = EagleEntity.AttackPhase.CIRCLE;
//
//                    if (!EagleEntity.this.isSilent()) {
//                        EagleEntity.this.level().levelEvent(1039, EagleEntity.this.blockPosition(), 0);
//                    }
//                } else if (EagleEntity.this.horizontalCollision || EagleEntity.this.hurtTime > 0) {
//                    EagleEntity.this.attackPhase = EagleEntity.AttackPhase.CIRCLE;
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public void tick() {
//        if (this.isLatching) {
//            latchingTimer--;
//            if (this.tickCount % 20 == 0) {
//                if (EagleEntity.this.getFirstPassenger() != null) {
//                    if (this.getFirstPassenger() instanceof WCatEntity cat) {
//                        cat.hurt(this.damageSources().genericKill(), 0.01f);
//                    } else {
//                        EagleEntity.this.getFirstPassenger().hurt(EagleEntity.this.damageSources().genericKill(), 1f);
//                    }
//                }
//            }
//
//            if (latchingTimer <= 0) {
//                this.isLatching = false;
//                this.setTarget(null);
//                if (this.getFirstPassenger() != null) {
//                    this.getFirstPassenger().stopRiding();
//                }
//            }
//        } else {
//            this.isLatching = false;
//            this.latchingTimer = 80;
//        }
//
//
//        super.tick();
//    }
//
//    @Override
//    public boolean shouldRiderSit() {
//        return false;
//    }
//
//    @Override
//    public double getMyRidingOffset() {
//        return -0.0D;
//    }
//
//    @Override
//    public double getPassengersRidingOffset() {
//        return -0.1D;
//    }
//
//    @Override
//    public boolean canRiderInteract() {
//        return false;
//    }
//
//    @Override
//    public void rideTick() {
//
//        Entity passenger = this.getFirstPassenger();
//        if (passenger == null) return;
//
//        float yawDeg;
//        float pitchDeg;
//
//        if (passenger instanceof WCatEntity || passenger instanceof Player player && PlayerShape.getCurrentShape(player) instanceof WCatEntity) {
//
//            yawDeg = this.yBodyRot;
//            pitchDeg = this.getXRot();
//
//            double yaw = Math.toRadians(-yawDeg);
//
//            double dirX = Math.sin(yaw);
//            double dirZ = Math.cos(yaw);
//
//            double distance = 0.66;
//
//            double offsetY = 0.15;
//
//            double pitch = Math.toRadians(pitchDeg);
//
//            double verticalOffset = Math.sin(-pitch) * 0.4;
//
//            double offsetX = dirX * distance + (verticalOffset / 5);
//            double offsetZ = dirZ * distance + (verticalOffset / 5);
//
//            passenger.setPos(
//                    this.getX() + offsetX,
//                    this.getY() + offsetY + verticalOffset,
//                    this.getZ() + offsetZ
//            );
//
//            float sideYaw = yawDeg + 200F;
//
//            passenger.setYRot(sideYaw);
//            passenger.setYHeadRot(sideYaw);
//
//        }
//
//        super.rideTick();
//    }
//
//    public class EagleCircleAroundAnchorGoal extends EagleMoveToTarget {
//        private float angle;
//        private float distance;
//        private float height;
//        private float clockwise;
//
//        public boolean canUse() {
//            return EagleEntity.this.getTarget() == null || EagleEntity.this.attackPhase == EagleEntity.AttackPhase.CIRCLE;
//        }
//
//        public void start() {
//            this.distance = 5.0F + EagleEntity.this.random.nextFloat() * 10.0F;
//            this.height = -4.0F + EagleEntity.this.random.nextFloat() * 9.0F;
//            this.clockwise = EagleEntity.this.random.nextBoolean() ? 1.0F : -1.0F;
//            this.selectNext();
//        }
//
//        public void tick() {
//            if (EagleEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
//                this.height = -4.0F + EagleEntity.this.random.nextFloat() * 9.0F;
//            }
//
//            if (EagleEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
//                ++this.distance;
//                if (this.distance > 15.0F) {
//                    this.distance = 5.0F;
//                    this.clockwise = -this.clockwise;
//                }
//            }
//
//            if (EagleEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
//                this.angle = EagleEntity.this.random.nextFloat() * 2.0F * (float)Math.PI;
//                this.selectNext();
//            }
//
//            if (this.touchingTarget()) {
//                this.selectNext();
//            }
//
//            if (EagleEntity.this.moveTargetPoint.y < EagleEntity.this.getY() && !EagleEntity.this.level().isEmptyBlock(EagleEntity.this.blockPosition().below(1))) {
//                this.height = Math.max(1.0F, this.height);
//                this.selectNext();
//            }
//
//            if (EagleEntity.this.moveTargetPoint.y > EagleEntity.this.getY() && !EagleEntity.this.level().isEmptyBlock(EagleEntity.this.blockPosition().above(1))) {
//                this.height = Math.min(-1.0F, this.height);
//                this.selectNext();
//            }
//
//        }
//
//        private void selectNext() {
//            if (BlockPos.ZERO.equals(EagleEntity.this.anchorPoint)) {
//                EagleEntity.this.anchorPoint = EagleEntity.this.blockPosition();
//            }
//
//            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
//            EagleEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(EagleEntity.this.anchorPoint).add((double)(this.distance * Mth.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * Mth.sin(this.angle)));
//        }
//    }
//
//
//    public class EagleAttackTargetsGoal extends Goal {
//        private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
//        private int nextScanTick = reducedTickDelay(20);
//
//        /**
//         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
//         * method as well.
//         */
//        public boolean canUse() {
//            if (this.nextScanTick > 0) {
//                --this.nextScanTick;
//                return false;
//            } else {
//                this.nextScanTick = reducedTickDelay(60);
//                Predicate<LivingEntity> predicate =
//                        entity -> (entity instanceof Player player && PlayerShape.getCurrentShape(player) instanceof WCatEntity)
//                                || (entity instanceof WCatEntity cat && cat.isBaby());
//
//                List<LivingEntity> list2 = EagleEntity.this.level().getEntitiesOfClass(LivingEntity.class,  EagleEntity.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D), predicate);
//                if (!list2.isEmpty()) {
//                    list2.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());
//
//                    for(LivingEntity entity : list2) {
//                        if (EagleEntity.this.canAttack(entity, TargetingConditions.DEFAULT)) {
//                            EagleEntity.this.setTarget(entity);
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }
//        }
//
//        /**
//         * Returns whether an in-progress EntityAIBase should continue executing
//         */
//        public boolean canContinueToUse() {
//            LivingEntity livingentity = EagleEntity.this.getTarget();
//            return livingentity != null && EagleEntity.this.canAttack(livingentity, TargetingConditions.DEFAULT);
//        }
//    }
//
//    @Override
//    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingdata, CompoundTag tag) {
//        this.anchorPoint = this.blockPosition().above(5);
//        return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
//    }
//
//
//
//    public static AttributeSupplier.Builder setAttributes() {
//        return Animal.createMobAttributes()
//                .add(Attributes.MAX_HEALTH, 30D)
//                .add(Attributes.ATTACK_SPEED, 1.0f)
//                .add(Attributes.ATTACK_DAMAGE, 1f)
//                .add(Attributes.MOVEMENT_SPEED, 2.0f)
//                .add(Attributes.FLYING_SPEED, 2.0f);
//    }
//
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new EagleAttackStrategy());
//        this.goalSelector.addGoal(2, new EagleSweepAttackGoal());
//        this.goalSelector.addGoal(3, new EagleCircleAroundAnchorGoal());
//        this.targetSelector.addGoal(1, new EagleAttackTargetsGoal());
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>
//                (this, "controller", 0, this::predicate));
//
//    }
//
//    @Override
//    public int getExperienceReward() {
//        return 30 + 5*this.random.nextInt(4);
//    }
//
//
//    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
//        if (this.isFlapping()) {
//            tAnimationState.getController().setAnimation(RawAnimation.begin()
//                    .then("animation.pigeon.fly", Animation.LoopType.LOOP));
//            return PlayState.CONTINUE;
//        }
//
//        if (tAnimationState.isMoving()) {
//            tAnimationState.getController().setAnimation(RawAnimation.begin()
//                    .then("animation.pigeon.walk", Animation.LoopType.LOOP));
//            return PlayState.CONTINUE;
//        }
//
//        tAnimationState.getController().setAnimation(RawAnimation.begin()
//                .then("animation.pigeon.idle", Animation.LoopType.LOOP));
//        return PlayState.CONTINUE;
//    }
//
//
//    @Override
//    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
//        ItemStack itemEarned = new ItemStack(ModItems.PIGEON_FEATHER.get());
//        if (itemEarned.isEmpty()) return;
//        ItemEntity drop = new ItemEntity(
//                this.level(),
//                this.getX(),
//                this.getY() + 0.2,
//                this.getZ(),
//                itemEarned.copyWithCount(this.getRandom().nextInt(4))
//        );
//        if (this.level() instanceof ServerLevel sLevel) {
//            sLevel.addFreshEntity(drop);
//        }
//
//        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
//    }
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
//
//    @Override
//    public void addAdditionalSaveData(CompoundTag pCompound) {
//        super.addAdditionalSaveData(pCompound);
//        pCompound.putInt("AX", this.anchorPoint.getX());
//        pCompound.putInt("AY", this.anchorPoint.getY());
//        pCompound.putInt("AZ", this.anchorPoint.getZ());
//    }
//
//    @Override
//    public void readAdditionalSaveData(CompoundTag pCompound) {
//        super.readAdditionalSaveData(pCompound);
//        if (pCompound.contains("AX")) {
//            this.anchorPoint = new BlockPos(pCompound.getInt("AX"), pCompound.getInt("AY"), pCompound.getInt("AZ"));
//        }
//
//    }
//
//    @Override
//    protected boolean isSunBurnTick() {
//        return false;
//    }
//
//    @Override
//    public void aiStep() {
//        super.aiStep();
//    }
//
//    public class EagleLookControl extends LookControl {
//        public EagleLookControl(Mob pMob) {
//            super(pMob);
//        }
//
//        public void tick() {
//        }
//    }
//
//    public class EagleMoveControl extends MoveControl {
//        private float speed = 0.1F;
//
//        public EagleMoveControl(EagleEntity pMob) {
//            super(pMob);
//        }
//
//        public void tick() {
//            if (EagleEntity.this.horizontalCollision) {
//                EagleEntity.this.setYRot(EagleEntity.this.getYRot() + 180.0F);
//                this.mob.setSpeed(0.1F);
//            }
//
//            double d0 = EagleEntity.this.moveTargetPoint.x - EagleEntity.this.getX();
//            double d1 = EagleEntity.this.moveTargetPoint.y - EagleEntity.this.getY();
//            double d2 = EagleEntity.this.moveTargetPoint.z - EagleEntity.this.getZ();
//            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
//            if (Math.abs(d3) > (double)1.0E-5F) {
//                double d4 = 1.0D - Math.abs(d1 * (double)0.7F) / d3;
//                d0 *= d4;
//                d2 *= d4;
//                d3 = Math.sqrt(d0 * d0 + d2 * d2);
//                double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
//                float f = EagleEntity.this.getYRot();
//                float f1 = (float)Mth.atan2(d2, d0);
//                float f2 = Mth.wrapDegrees(EagleEntity.this.getYRot() + 90.0F);
//                float f3 = Mth.wrapDegrees(f1 * (180F / (float)Math.PI));
//                EagleEntity.this.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
//                EagleEntity.this.yBodyRot = EagleEntity.this.getYRot();
//                if (Mth.degreesDifferenceAbs(f, EagleEntity.this.getYRot()) < 3.0F) {
//                    this.mob.setSpeed(Mth.approach(this.mob.getSpeed(), 1.8F, 0.005F * (1.8F / this.mob.getSpeed())));
//                } else {
//                    this.mob.setSpeed(Mth.approach(this.mob.getSpeed(), 0.2F, 0.025F));
//                }
//
//                float f4 = (float)(-(Mth.atan2(-d1, d3) * (double)(180F / (float)Math.PI)));
//                EagleEntity.this.setXRot(f4);
//                float f5 = EagleEntity.this.getYRot() + 90.0F;
//                double d6 = (double)(this.mob.getSpeed() * Mth.cos(f5 * ((float)Math.PI / 180F))) * Math.abs(d0 / d5);
//                double d7 = (double)(this.mob.getSpeed() * Mth.sin(f5 * ((float)Math.PI / 180F))) * Math.abs(d2 / d5);
//                double d8 = (double)(this.mob.getSpeed() * Mth.sin(f4 * ((float)Math.PI / 180F))) * Math.abs(d1 / d5);
//                Vec3 vec3 = EagleEntity.this.getDeltaMovement();
//                EagleEntity.this.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2D)));
//            }
//
//        }
//    }
//
//}
