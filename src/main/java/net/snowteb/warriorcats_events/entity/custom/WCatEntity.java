package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.sound.ModSounds;
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

public class WCatEntity extends TamableAnimal implements GeoEntity{

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
    private static final int WANDER_RADIUS = 15;
    int maxVariants = 20;
    private final int KITTINGTIME = 20 * 60 * 8; // 8min
    private boolean wasBaby = this.isBaby();

    private boolean playingAnimation = false;





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

   private String[] getPrefixSetForVariant(int variant) {
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

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }



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

        @Override
        public void tick() {
            if (owner == null) return;

            double dist = cat.distanceTo(owner);

            cat.getLookControl().setLookAt(owner, 10.0F, cat.getMaxHeadXRot());

            if (dist > 25) {
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
            double len = Math.sqrt(dx*dx + dz*dz);

            if (len < 0.001) len = 0.001;

            double ratio = stopDistance / len;

            double targetX = owner.getX() + dx * ratio;
            double targetZ = owner.getZ() + dz * ratio;
            double targetY = owner.getY();

            cat.getNavigation().moveTo(targetX, targetY, targetZ, speed);
        }

    }



    public class CasualBlockSeekGoal extends RandomStrollGoal {

        private final Block targetBlock;
        private final int searchRadius;
        private final double chance;
        private final WCatEntity cat;

        public CasualBlockSeekGoal(WCatEntity cat, double speed, Block targetBlock, int searchRadius, double chance) {
            super(cat, speed);
            this.cat = cat;
            this.targetBlock = targetBlock;
            this.searchRadius = searchRadius;
            this.chance = chance;
        }

        @Override
        protected Vec3 getPosition() {
            if (this.mob.getRandom().nextDouble() < this.chance) {
                Vec3 pos = findRandomBlockPos();
                if (pos != null) return pos;
            }

            return super.getPosition();
        }

        @Override
        public boolean canUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (wcat.mode == CatMode.SIT) return false;
            if (cat.isOrderedToSit()) return false;


            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = cat;

            if (wcat.mode == CatMode.SIT) return false;
            if (cat.isOrderedToSit()) return false;

            return super.canContinueToUse();
        }

        private Vec3 findRandomBlockPos() {
            BlockPos mobPos = this.mob.blockPosition();
            Level level = this.mob.level();

            List<BlockPos> validPositions = new ArrayList<>();

            Block chosenBlock = switch (cat.getRank()) {
                case KIT -> ModBlocks.MOSSBED.get();
                case APPRENTICE -> ModBlocks.MOSSBED.get();
                case WARRIOR -> ModBlocks.MOSSBED.get();
                case MEDICINE -> ModBlocks.STONECLEFT.get();
                case NONE -> this.targetBlock;
            };

            for (int x = -searchRadius; x <= searchRadius; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -searchRadius; z <= searchRadius; z++) {
                        BlockPos checkPos = mobPos.offset(x, y, z);
                        if (level.getBlockState(checkPos).is(chosenBlock)) {
                            validPositions.add(checkPos);
                        }
                    }
                }
            }

            if (validPositions.isEmpty()) return null;

            BlockPos chosen = validPositions.get(this.mob.getRandom().nextInt(validPositions.size()));

            return Vec3.atCenterOf(chosen);
        }
    }




    private class BoundedWanderGoal extends WaterAvoidingRandomStrollGoal {

        private final TamableAnimal cat;

        public BoundedWanderGoal(TamableAnimal cat, double speed) {
            super(cat, speed);
            this.cat = cat;
        }

        @Override
        public boolean canUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (!cat.isTame()) return false;
            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

            if (wcat.wanderCenter != null &&
                    cat.blockPosition().distSqr(wcat.wanderCenter) > WANDER_RADIUS * WANDER_RADIUS) {
                return false;
            }

            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

            if (wcat.wanderCenter != null &&
                    cat.blockPosition().distSqr(wcat.wanderCenter) > WANDER_RADIUS * WANDER_RADIUS) {
                return false;
            }

            return super.canContinueToUse();
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
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new CatFollowOwnerGoal(this, 1.2D, 1.0F, 7.0F)); // prioridad alta para seguir si toca
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(6, this.preyTarget);
        this.goalSelector.addGoal(7, new WCAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(8, new BoundedWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new CasualBlockSeekGoal(this,1.0D,ModBlocks.MOSSBED.get(),15,0.10D));

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
                (entityType, world, reason, pos, random) ->
                        world.getDifficulty() != Difficulty.PEACEFUL &&
                                world.getBlockState(pos.below()).isSolid()
        );
    }




    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
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


                if (!this.hasCustomName()) {
                    int variant = this.getVariant();
                    String[] prefixSet = getPrefixSetForVariant(variant);

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

        if (this.isTame() && pPlayer.isShiftKeyDown() && pPlayer.getUUID().equals(this.getOwnerUUID()) && itemstack.is(ModItems.WHISKERS.get())) {
            Rank current = this.getRank();

            switch (current) {
                case NONE:
                    this.setRank(Rank.APPRENTICE);
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


        if (this.isTame() && itemstack.is(ModItems.CATMINT.get())) {
            if (!pPlayer.getAbilities().instabuild) itemstack.shrink(1);

            if (hasValidMateNearby()) {
                this.setInLove(pPlayer);
            }

            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && itemstack.is(ModItems.WARRIORNAMERANDOMIZER.get())) {

            if (!this.level().isClientSide()) {
                itemstack.hurtAndBreak(1, pPlayer, (p) ->
                        p.broadcastBreakEvent(pHand));


                int variantS = this.getVariant();
                String[] prefixSet = getPrefixSetForVariant(variantS);

                String genderV ;
                if (this.getGender() == 0) {
                    genderV = " ♂";
                } else  {
                    genderV = " ♀";
                }

                int i = this.random.nextInt(prefixSet.length);
                int j = this.random.nextInt(SUFIX.length);


                String finalName;

                if (this.isBaby()){finalName = prefixSet[i] + "kit" + genderV;}
                else if (this.getRank() == Rank.APPRENTICE){finalName = prefixSet[i] + "paw" + genderV;}
                else {finalName = prefixSet[i] + SUFIX[j] + genderV;}

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
                float kittingTime = (9600 - this.getKittingTicks())/(20f * 60f);
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
                float moonsCalc = (float)((this.getAge() + 108000) / 9000.0);
                this.entityData.set(AGE_SYNC, moonsCalc);
            }


            if (this.getAge() < 0) {
                    moons = this.entityData.get(AGE_SYNC);

                    ageText = String.format("%.2f moons", moons);;
                } else {
                    ageText = "Fully grown";
                }

            if (this.getKittingTicks() > 20) {KitTime = String.format("%.2f min", kittingTime);}
                else { KitTime = "Not expecting";}



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

        return super.mobInteract(pPlayer, pHand);
    }



    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {

        if (otherParent instanceof WCatEntity partner && this.isTame() && ((WCatEntity) otherParent).isTame()) {


            if (this.getGender() == 1) {
                int otherParentGender = partner.getGender();
                if (otherParentGender == 0) {
                    this.setExpectingKits(true);
                }

                this.resetLove();
            }
            Component MateName = otherParent.getCustomName();
            this.setMate(MateName);

        }

        return null;
    }


    @Override
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isExpectingKits()) {
            this.setKittingTicks(this.getKittingTicks() + 1);

            if (this.getKittingTicks() >= KITTINGTIME) {
                this.setExpectingKits(false);
                this.setKittingTicks(0);

                Kitting();

            }
        }


        if (!this.level().isClientSide()) {
            if (this.wasBaby && !this.isBaby()) {
                this.onGrewUp();
            }
            this.wasBaby = this.isBaby();
        }


        if (mode == CatMode.SIT) {
            if (!this.isOrderedToSit()) this.setOrderedToSit(true);
            this.getNavigation().stop();
        } else {
            if (this.isOrderedToSit()) this.setOrderedToSit(false);
        }


        if (mode == CatMode.FOLLOW && this.isTame()) {
            LivingEntity owner = this.getOwner();

            if (owner != null) {
                double dist = this.distanceTo(owner);

                if (dist > 25) {
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
        if (tag.contains("kitBorn")) {
            kitBorn = tag.getBoolean("kitBorn");
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


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>
                (this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>
                (this, "attackController", 0, this::attackPredicate));
        controllers.add(new AnimationController<>
                (this, "playerController", 0, this::playerPredicate));

    }

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
                }
                else if (anim6 == 1) {
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

        if(!this.isAttacking()){
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }



    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

       if (!this.onGround() && !this.isInWater()) {
           tAnimationState.getController().setAnimation(RawAnimation.begin().
                   then("animation.wcat.falling", Animation.LoopType.LOOP));
           animPlayed = false;
           return PlayState.CONTINUE;
       }

       if (tAnimationState.isMoving() && !this.isCrouching()) {
           tAnimationState.getController().setAnimation(RawAnimation.begin().
                   then("animation.wcat.walk", Animation.LoopType.LOOP));
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

           } else if (rand == 2 && !animPlayed){

               tAnimationState.getController().setAnimation(RawAnimation.begin()
                       .then("animation.wcat.stretch", Animation.LoopType.PLAY_ONCE));
               animPlayed = true;
           }
           else if (rand == 3 && !animPlayed){

               tAnimationState.getController().setAnimation(RawAnimation.begin()
                       .then("animation.wcat.roll", Animation.LoopType.PLAY_ONCE));
               animPlayed = true;
           }


           return PlayState.CONTINUE;

        }
        if (animPlayed && tAnimationState.getController().hasAnimationFinished()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin()
                    .then("animation.wcat.idle", Animation.LoopType.LOOP));
            animPlayed = false;

            return PlayState.CONTINUE;
        }

       if (this.isCrouching()){
           if (tAnimationState.isMoving() && this.isCrouching()) {
               tAnimationState.getController().setAnimation(RawAnimation.begin().
                       then("animation.wcat.crouchingwalk", Animation.LoopType.LOOP));
           } else {
           tAnimationState.getController().setAnimation(RawAnimation.begin().
                   then("animation.wcat.crouchingidle", Animation.LoopType.LOOP));
           }
           animPlayed = false;
       }

        else if (!animPlayed) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().
                    then("animation.wcat.idle", Animation.LoopType.LOOP));
        }

             return PlayState.CONTINUE;

   }




    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }





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

    private void Kitting() {
        if (!(this.level() instanceof ServerLevel server)) return;
        this.setExpectingKits(false);
        LivingEntity owner = this.getOwner();

        int litterSize = 1 + this.random.nextInt(3);

        for (int i = 0; i < litterSize; i++) {
            this.resetLove();
            WCatEntity kit = ModEntities.WCAT.get().create(server);

            if (kit != null) {
                kit.setPos(this.getX(), this.getY(), this.getZ());
                int minutes = 90;
                int growingTicks = minutes * 20 * 60;
                kit.setAge(-growingTicks);
                kit.setTame(true);
                kit.setRank(Rank.KIT);
                kit.kitBorn = true;


                if (!kit.hasCustomName()) {
                    int variant = kit.getVariant();
                    String[] prefixSet = getPrefixSetForVariant(variant);

                    String genderS;
                    if (kit.getGender() == 0) {
                        genderS = " ♂";
                    } else {
                        genderS = " ♀";
                    }

                    int k = kit.random.nextInt(prefixSet.length);

                    String finalName = prefixSet[k] + "kit" + genderS;
                    kit.setCustomName(Component.literal(finalName));
                    kit.setCustomNameVisible(true);

                    kit.setPrefix(Component.literal(prefixSet[k]));

                }


                if (owner instanceof Player player) {
                    kit.setOwnerUUID(player.getUUID());
                }

                int randomVariant = this.random.nextInt(maxVariants);
                kit.setVariant(randomVariant);


                server.addFreshEntity(kit);
            }
        }

    }

    private void onGrewUp() {
        Component prefix = this.getPrefix();
        if (prefix != null) {
            String genderV = this.getGender() == 0 ? " ♂" : " ♀";
            int i = this.random.nextInt(SUFIX.length);

            String newName = prefix.getString() + SUFIX[i] + genderV;

            this.setCustomName(Component.literal(newName));
            this.setCustomNameVisible(true);
            this.setAppScale(false);
            this.level().broadcastEntityEvent(this, (byte) 6);


        }
    }






    private boolean hasValidMateNearby() {
        if (!this.isTame()) return false;

        List<WCatEntity> list = this.level().getEntitiesOfClass(
                WCatEntity.class,
                this.getBoundingBox().inflate(16)
        );

        for (WCatEntity cat : list) {
            if (cat != this && cat.getGender() != this.getGender()) {

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





    public void setAttacking(boolean attacking) {
       this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
       return this.entityData.get(ATTACKING);
    }







    public int getVariant() {
        return this.entityData.get(VARIANT);
    }
    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    float scale = switch (variant) {
            case 0 -> 0.5f;
            case 1 -> 0.6f;
            case 2 -> 0.6f;
            case 3 -> 0.5f;
            case 4 -> 0.5f;
            case 5 -> 0.6f;
            case 6 -> 0.7f;
            case 7 -> 0.7f;
            case 8 -> 0.5f;
            case 9 -> 0.7f;
            case 10 -> 0.6f;
            case 11 -> 0.5f;
            case 12 -> 0.5f; //chestnutpatch
            case 13 -> 0.6f; //ratstar
            case 14 -> 0.5f; //twitchstream
            case 15 -> 0.7f; //blazepit
            case 16 -> 0.6f; //bengalpelt
            case 17 -> 0.7f; //sparrowstar
            case 18 -> 0.5f; //foxeater
            case 19 -> 0.6f; //willowsong
            default -> 0.5f;
        };
        this.entityData.set(SCALE, scale);
    }

    private boolean apprenticeAge = false;


    public Vec3 clientMovement = Vec3.ZERO;
    private Vec3 lastClientPos = Vec3.ZERO;

    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide()) {
            if (!apprenticeAge && this.getAge() >= -54000 && kitBorn) {
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


                this.setRank(Rank.APPRENTICE);
                this.level().broadcastEntityEvent(this, (byte) 6);
                kitBorn=false;
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

    public void setPlayingAnimation(boolean value) {
        this.playingAnimation = value;
    }

    public boolean isPlayingAnimation() {
        return this.playingAnimation;
    }











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
    public boolean isFood(ItemStack itemstack) {
        return itemstack.is(ModItems.CATMINT.get());
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

    @Override
    public void setPose(Pose pose) {
        super.setPose(pose);
        this.refreshDimensions();
    }





}
