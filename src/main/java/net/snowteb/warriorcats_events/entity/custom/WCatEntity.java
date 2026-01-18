package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.snowteb.warriorcats_events.WCEConfig;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.OpenCatDataScreenPacket;
import net.snowteb.warriorcats_events.network.packet.S2CSyncClanDataPacket;
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

    public static final UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private Goal preyTarget;
    private Goal monsterTarget;
    public boolean kitBorn = false;
    boolean animPlayed;
    public CatMode mode = CatMode.WANDER;
    public CatMode lastMode;
    public BlockPos wanderCenter = null;
    int maxVariants = 30;
    private boolean wasBaby = this.isBaby();
    int catSniffTickCooldown = 0;

    private int grumpyAtOwnerTick = 0;
    private int scentTick = 0;
    private int soundTick = 0;
    private Vec3 scentDirection = null;
    private Vec3 scentStartPos = null;
    private double scentDistance = 0;
    private double scentMaxDistance = 5;
    private double scentStep = 0.2;
    private boolean moodLoaded = false;

    public boolean returnHomeFlag = false;
    public boolean leaderCallingToFollowFlag = false;
    public boolean leaderCallingToSitFlag = false;
    public boolean lookAtLeaderFlag = false;
    public boolean isLookingAtLeader = false;
    public boolean isBeingCarried = false;

    private UUID motherUUID = null;
    private UUID fatherUUID = null;
    private UUID mateUUID = null;
    private int generation = 0;

    private boolean forbidFutureGenerationsFromMatingPlayer = false;
    private boolean forbiddenFromMatingPlayer = false;
    private UUID forbiddenPlayer = null;

    private int lovingParticlesTicks = 0;


    public Component getMother() {
        return this.entityData.get(MOTHER).orElse(Component.literal("None"));
    }

    public void setMother(@Nullable Component name) {
        this.entityData.set(MOTHER, Optional.ofNullable(name));
    }

    public Component getFather() {
        return this.entityData.get(FATHER).orElse(Component.literal("None"));
    }

    public void setFather(@Nullable Component name) {
        this.entityData.set(FATHER, Optional.ofNullable(name));
    }

    public UUID getMotherUUID() {
        return motherUUID;
    }

    public void setMotherUUID(UUID uuid) {
        this.motherUUID = uuid;
    }


    public UUID getFatherUUID() {
        return fatherUUID;
    }


    public void setFatherUUID(UUID uuid) {
        this.fatherUUID = uuid;
    }

    public UUID getMateUUID() {
        return mateUUID;
    }

    public void setMateUUID(UUID uuid) {
        this.mateUUID = uuid;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }


    // RULES
    public void setForbiddingFutureGensFromMatingPlayer(boolean value) {
        this.forbidFutureGenerationsFromMatingPlayer = value;
    }

    public boolean isForbiddingFutureGensFromMatingPlayer() {
        return this.forbidFutureGenerationsFromMatingPlayer;
    }

    public void setForbiddenPlayer(UUID uuid) {
        this.forbiddenPlayer = uuid;
    }

    public UUID getForbiddenPlayer() {
        return this.forbiddenPlayer;
    }

    public void setForbiddenFromMatingPlayer(boolean value) {
        this.forbiddenFromMatingPlayer = value;
    }

    public boolean isForbiddenFromMatingPlayer() {
        return forbiddenFromMatingPlayer;
    }


    private BlockPos homePosition = BlockPos.ZERO;

    private final SimpleContainer inventory = new SimpleContainer(3);

    public static final EntityDataAccessor<ItemStack> HEAD_ARMOR =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<ItemStack> CHEST_ARMOR =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<ItemStack> LEGS_ARMOR =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<ItemStack> FEET_ARMOR =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.ITEM_STACK);


//    @Nullable
//    private Vec3 leaderCallTarget;


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

    private static final EntityDataAccessor<Optional<Component>> CLAN =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Integer> PERSONALITY =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MOOD =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> INTERACTION_COOLDOWN =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> KITTING_COOLDOWN =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<Component>> FATHER =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    private static final EntityDataAccessor<Optional<Component>> MOTHER =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);

    private static final EntityDataAccessor<Boolean> IS_AN_IMAGE =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.BOOLEAN);

    public void setAnImage(boolean isAnImage) {
        this.entityData.set(IS_AN_IMAGE, isAnImage);
    }
    public boolean isAnImage() {
        return this.entityData.get(IS_AN_IMAGE);
    }

    public enum CatInteraction {
        GIVE_ITEM,
        TALK,
        SHOW_AFFECTION,
    }

    public enum Mood {
        HAPPY,
        CALM,
        STRESSED,
        SAD
    }

    private final Map<String, List<String>> dialoguePool = new HashMap<>();

    private void loadDialogueMap() {

        dialoguePool.put("CALM.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Oh! Thank you, <morph.name>!",
                "It looks good! I appreciate it!",
                "It's just what I needed, thank you, <<morph.name>!",
                "Aww! This is exactly what I wanted!",
                "I will definitely enjoy this.",
                "You are so kind! Thank you!",
                "That's very kind of you, <morph.name>.",
                "You brought this for me? That’s kind of you, <morph.name>.",
                "This came at a good time. Thank you.",
                "I’m glad you shared this with me. Thanks.",
                "I didn’t expect this, but I appreciate it a lot."
        ));
        dialoguePool.put("CALM.GIVE_ITEM.FAIL", Arrays.asList(
                "Oh, I'm good, thank you anyway.",
                "Err.. Thanks, but maybe for later?",
                "I don't really need that.",
                "Don't worry, you can keep it.",
                "Maybe give this to someone else.",
                "Thanks, but it's unnecessary.",
                "I'll pass for now, thank you!",
                "No, thank you. I don’t really need it.",
                "I’m fine without it, but thank you.",
                "Maybe save it for someone who needs it more.",
                "Another time, perhaps."
        ));

        dialoguePool.put("CALM.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "Oh! That's comforting. Thank you, <morph.name>.",
                "I appreciate your kindness.",
                "I appreciate you too!",
                "That feels nice!",
                "Thank you, <morph.name>. That makes me feel better",
                "You’re very considerate, <morph.name>."
        ));
        dialoguePool.put("CALM.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Not right now, please.",
                "I'm not really in the mood for that...",
                "Please give me some space.",
                "Er.. Okay?",
                "Please don't-",
                "I appreciate my personal space more for now.",
                "I appreciate it, but I’m not in the mood.",
                "Another time, maybe.",
                "I’d prefer some distance for now."
        ));

        dialoguePool.put("CALM.TALK.SUCCESS", Arrays.asList(
                "It's a good day, isn't it, <morph.name>?",
                "The wind feels peaceful today.",
                "I've been thinking… Things are finally settling down.",
                "I enjoy moments like this. Quiet. Simple.",
                "You are always so serene… it's comforting.",
                "I remember when I was a kit... Oh, no I don't remember-",
                "Did you see that huge mouse today, <morph.name>?",
                "I smell rain, don't you, <morph.name>?",
                "I once caught a pigeon the size of a cat!",
                "You know... Sometimes I feel like the trees are whispering... Are they, <morph.name>?",
                "This place feels quiet today, doesn’t it, <morph.name>?"
        ));
        dialoguePool.put("CALM.TALK.FAIL", Arrays.asList(
                "I'm... not really in the mood to talk right now.",
                "Let's leave it for later.",
                "I’d rather stay quiet for now.",
                "I need some space to think.",
                "Not now, please.",
                "Sorry, not right now.",
                "Sorry, what did you say?",
                "Let’s talk another time.",
                "Sorry… I’m focusing on something.",
                "Maybe later. I’m not in a talking mood."
        ));


        dialoguePool.put("GRUMPY.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Fine… I guess this is alright.",
                "Hmph. Thanks… I suppose.",
                "It'll do.",
                "Yeah… thanks, <morph.name>.",
                "I might use this, but don't get used to it.",
                "Well, something good at last.",
                "As long as it's not crow-food...",
                "Huh… better than nothing.",
                "I’ve seen worse. Thanks, <morph.name>.",
                "Okay… I’ll admit it’s decent."
        ));
        dialoguePool.put("GRUMPY.GIVE_ITEM.FAIL", Arrays.asList(
                "I don't want that.",
                "Don't you have anything better to do?",
                "Why would I want this?",
                "Take it back.",
                "Nah.",
                "It smells like crow-food...",
                "Uh, I'll pass.",
                "Someone else might eat this, but not me.",
                "Give it to someone else.",
                "Do I have to?",
                "No thanks."
        ));

        dialoguePool.put("GRUMPY.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "Tch… fine. Just for a second.",
                "Don't make a big deal out of it.",
                "…Thanks.",
                "Alright. That's enough.",
                "Whatever. I don't hate it.",
                "I... Okay?",
                "Alright… if it makes you feel better.",
                "Just this once.",
                "Okay… but only for a moment.",
                "…Thanks, I guess.",
                "Hmph… fine."
        ));
        dialoguePool.put("GRUMPY.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Hands off.",
                "No touchies.",
                "Don't, thank you.",
                "Back away.",
                "I said no.",
                "I'm not that type of cat.",
                "I'd rather eat crow-food.",
                "Get off, mouse-brain.",
                "Not happening.",
                "Find someone else for that."
        ));

        dialoguePool.put("GRUMPY.TALK.SUCCESS", Arrays.asList(
                "I smell trouble today, don't you, <morph.name>?",
                "See that cat over there? They are mouse-brained.",
                "If anything causes trouble, they'll have to answer to my claws.",
                "Tch… everything feels off around here.",
                "If something goes wrong, I’ll be ready.",
                "Once, I heard rustling in the grass behind me. I just stood still. Turned out to be a mouse. Still didn’t like that it got so close without me knowing.",
                "This place smelled better yesterday...",
                "I don’t enjoy company… but you’re tolerable, <morph.name>.",
                "I don’t trust the quiet… Do you, <morph.name>?",
                "I don’t like how the territory feels lately.",
                "Hey, <morph.name>, how is prey running?",
                "I once clawed a cat's ears off for getting too close. Beware.",
                "Nothing stays peaceful for long."
        ));
        dialoguePool.put("GRUMPY.TALK.FAIL", Arrays.asList(
                "Hmph. Don’t bother me.",
                "I’m not here to chat.",
                "Chatting catches no prey, you should know it.",
                "Save it.",
                "Not interested.",
                "Go talk to someone else.",
                "Someone else might want to hear all your meowing.",
                "I don’t feel like talking.",
                "Go bother someone else.",
                "I’ve got better things to do.",
                "Leave the chatter for someone else."
        ));


        dialoguePool.put("CAUTIOUS.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Thank you, <morph.name>. I appreciate it.",
                "Smells nice, thank you!.",
                "Seems fine, thanks.",
                "Alright… I’ll take it.",
                "Thanks, <morph.name>.",
                "I appreciate the gesture, <morph.name>.",
                "I’ll take it. Thanks, <morph.name>.",
                "Thanks. I wasn’t expecting this, but I’m grateful.",
                "Did you catch this yourself? Thanks, <morph.name>.",
                "If you're sure... I'll take it."
        ));
        dialoguePool.put("CAUTIOUS.GIVE_ITEM.FAIL", Arrays.asList(
                "I'd rather not take that for now.",
                "Err... No, thanks, <morph.name>.",
                "No… sorry.",
                "No, you can have it, <morph.name>.",
                "Better not.",
                "Maybe another time.",
                "I appreciate the thought, but I’ll refuse this time.",
                "Better if someone else takes it instead.",
                "Oh! No, you can keep it. Thanks anyway.",
                "I think I’ll pass for now."
        ));

        dialoguePool.put("CAUTIOUS.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "…Alright. That’s okay.",
                "I’m okay with that.",
                "That’s alright.",
                "That feels… alright.",
                "Well... I think I trust you, <morph.name>.",
                "Thank you. I’m comfortable.",
                "I’m okay with this… I think.",
                "Thank you… That was reassuring.",
                "That's sweet of you, <morph.name>."
        ));
        dialoguePool.put("CAUTIOUS.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Too close.",
                "Wait… no.",
                "Please step back.",
                "Not now, sorry.",
                "Err, not for now.",
                "Sorry… I don’t feel comfortable with that.",
                "I’d rather keep some distance for the moment."
        ));

        dialoguePool.put("CAUTIOUS.TALK.SUCCESS", Arrays.asList(
                "Do you… hear that, <morph.name>?",
                "I’ve been keeping my guard up, In case a fox shows up.",
                "I hear things in the night... Don't you, <morph.name>?",
                "I’m watching the area… just in case.",
                "I heard the bushes rustling the other day, didn't you, <morph.name>?",
                "I hope this place is safe enough for us.",
                "Do you like this place too, <morph.name>?",
                "No threats yet... but I’ll stay aware.",
                "Once, I followed scent trail but stopped halfway. The earth dipped suddenly into a hollow. If I had rushed forward, I would’ve slipped... That's why you must always be careful.",
                "I trust careful paws more than lucky ones. Don't you, <morph.name>?",
                "Quiet day so far.",
                "I sometimes mark the places where a loner passes. It sounds strange, I know… but one day it will be of use."
        ));
        dialoguePool.put("CAUTIOUS.TALK.FAIL", Arrays.asList(
                "Not now.",
                "Sorry, I'm up to something.",
                "I need to stay alert, we can talk later.",
                "Let’s talk later… maybe.",
                "I'm a little busy, let's leave it for later.",
                "I’ve been keeping watch… Things feel quiet, but I’m not fully convinced.",
                "Calm days make me think something might be approaching.",
                "Let’s talk later. I’m paying attention to something.",
                "We can leave it for later."
        ));


        dialoguePool.put("INDEPENDENT.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Thanks. I could've gotten it myself… but fine.",
                "I’ll take it this time.",
                "Seems fine. Appreciated.",
                "Thanks, I'll handle the rest on my own.",
                "Thanks, <morph.name>.",
                "Only this once...",
                "Well... Maybe this time. Thanks, <morph.name>.",
                "I’ll accept this time."
        ));
        dialoguePool.put("INDEPENDENT.GIVE_ITEM.FAIL", Arrays.asList(
                "I can hunt by myself.",
                "Keep it. I can manage myself.",
                "Unnecessary.",
                "No. I’ll obtain my own prey.",
                "I prefer independence, thanks anyway.",
                "Keep it. I’ll catch my own.",
                "I’m fine without it.",
                "Maybe someone else might need it."
        ));

        dialoguePool.put("INDEPENDENT.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "…Alright. Just a moment.",
                "You are so sweet, thanks, <morph.name>.",
                "Fine. Briefly.",
                "I can allow this… for now.",
                "Well, it's not that bad.",
                "I appreciate you too, <morph.name>."
        ));
        dialoguePool.put("INDEPENDENT.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Maybe another time.",
                "That kind of closeness isn’t for me.",
                "Personal space, please.",
                "Not right now.",
                "I'm good, thanks."
        ));

        dialoguePool.put("INDEPENDENT.TALK.SUCCESS", Arrays.asList(
                "Greetings, <morph.name>, how is your day?",
                "A good cat can always survive on their own. Don't you think, <morph.name>?",
                "I survived on my own for a long time in the past. Have you, <morph.name>?",
                "Relying on someone else would weaken any cat. Good thing you are different, <morph.name>.",
                "I always prefer to catch my own prey. Don't you, <morph.name>?",
                "The wild always taught me to rely on my own paws.",
                "I remember when I was a kit, I had to learn to hunt on my own.",
                "How is prey running, <morph.name>?",
                "Long time ago I spent a leaf-fall season living near a fallen log. Prey was scarce, but I learned every sound the forest made. Hunger teaches you to listen… and to trust your instincts.",
                "I almost ate deathberries when I was a kit, did you know? I immediately spit them out when they tasted quite... Weird.",
                "I don’t avoid others. I just… return to myself every now and then. The world feels clearer when it’s only you and your paws for a little..."
        ));
        dialoguePool.put("INDEPENDENT.TALK.FAIL", Arrays.asList(
                "I don’t need conversation.",
                "I prefer silence for now.",
                "Not now, I'm quite busy.",
                "I’ll pass for now.",
                "I’m fine on my own for now.",
                "Not in the mood to chat."
        ));


        dialoguePool.put("FRIENDLY.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Wow! Thanks, <morph.name>!",
                "You’re always so thoughtful! Thank you!",
                "It looks good! I appreciate it, <morph.name>!",
                "It's just what I wanted, thank you!",
                "Aww! This is exactly what I wanted!",
                "You're amazing, thank you, <morph.name>!",
                "I love it!",
                "This is great! Thanks, <morph.name>!",
                "You always bring good prey, thanks, <morph.name>."
        ));
        dialoguePool.put("FRIENDLY.GIVE_ITEM.FAIL", Arrays.asList(
                "Err… maybe not the best this time.",
                "That’s kind, but I'm good, thank you, <morph.name>!",
                "I'm good. Thanks anyway!",
                "Maybe next time, okay?",
                "Not right now, but thanks, <morph.name>!",
                "I appreciate it, but I’m fine for now."
        ));

        dialoguePool.put("FRIENDLY.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "That feels nice!",
                "Thank you! You're so sweet, <morph.name>!",
                "I appreciate your company!",
                "This feels good!",
                "It makes me happy. Thank you, <morph.name>!",
                "I really appreciate the care.",
                "You’re good company, <morph.name>."
        ));
        dialoguePool.put("FRIENDLY.SHOW_AFFECTION.FAIL", Arrays.asList(
                "A little too much!",
                "Err… slow down!",
                "Sorry, not right now.",
                "Oh! Too close!",
                "Not right now, sorry!",
                "Not at the moment, sorry.",
                "Maybe later, okay?"
        ));

        dialoguePool.put("FRIENDLY.TALK.SUCCESS", Arrays.asList(
                "Hey! Nice to see you again, <morph.name>!",
                "I was hoping you'd come by!",
                "Hi! how has your day been, <morph.name>?",
                "I was just thinking about you, <morph.name>! How are things going?",
                "Am I crazy or did I smell a fox last night... Anyway, how are you, <morph.name>?",
                "Hey! How is it going, <morph.name>?",
                "I would really love a walk. Wouldn't you?",
                "I’ve had a pretty calm day so far. How about you?",
                "Hi, <morph.name>! Have you seen anything interesting today?",
                "I was just thinking about exploring nearby later."
        ));
        dialoguePool.put("FRIENDLY.TALK.FAIL", Arrays.asList(
                "Sorry, I’m a bit tired to talk.",
                "Maybe later, okay?",
                "We can talk later, sorry, <morph.name>!",
                "I don’t really feel like talking now.",
                "Could we talk another time?",
                "Sorry… I’m a bit worn out."
        ));


        dialoguePool.put("SHY.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Wow… Thank you, <morph.name>.",
                "I… appreciate it.",
                "You didn’t have to…",
                "Thanks… really, <morph.name>.",
                "This means a lot to me. Thank you, <morph.name>.",
                "For me...? Thanks, <morph.name>!",
                "You’re very kind… thank you, <morph.name>.",
                "I didn’t expect this… thank you.",
                "This feels special… thanks.",
                "I’m really thankful, <morph.name>."
        ));
        dialoguePool.put("SHY.GIVE_ITEM.FAIL", Arrays.asList(
                "S… sorry… I can't take it.",
                "I can't take that right now.",
                "I-I'm good, thanks, <morph.name>.",
                "I'm not hungry. Sorry, <morph.name>.",
                "Maybe not now…",
                "I don’t think I should take that."
        ));

        dialoguePool.put("SHY.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "…Okay.",
                "That’s… nice.",
                "Thank you… <morph.name>.",
                "I'm… alright with this.",
                "Oh! You're so sweet, <morph.name>!",
                "It helps… more than I expected."
        ));
        dialoguePool.put("SHY.SHOW_AFFECTION.FAIL", Arrays.asList(
                "N-not right now…",
                "S-sorry, I'm not that affective.",
                "Not now, maybe another time?",
                "Sorry, not now.",
                "Not right now… Sorry.",
                "Maybe not right now…",
                "I'm not really in the mood, sorry."
        ));

        dialoguePool.put("SHY.TALK.SUCCESS", Arrays.asList(
                "E-err… Hi, <morph.name>!",
                "I used to be always alone, talking to the trees, to the moon... Until you came,  <morph.name>, and you made me feel I truly belonged...",
                "I’m glad you’re here, <morph.name>!",
                "Back in the days, I barely spoke to anyone. One evening, another cat sat beside me without saying a word. We just watched the sky. That's something I still remember...",
                "I don’t talk much… but I like listening. How has your day been, <morph.name>?",
                "I once practiced speaking... alone, among some bushes. I repeated greetings to myself until I felt brave enough to use them. I think I've improved, heh...",
                "It's nice being near you. How are you, <morph.name>?",
                "H-hi, <morph.name>! Have you caught any prey today?",
                "Are we going on an adventure? I heard there is so much out there!",
                "Hi <morph.name>! I-I was just watching the clouds.",
                "H-hey! Um… how has your day been?",
                "Sometimes silence feels comfortable... I think I even learned to talk to myself!"
        ));
        dialoguePool.put("SHY.TALK.FAIL", Arrays.asList(
                "I… um… excuse me I got to go-",
                "S-sorry, I can't talk right now",
                "*quietly stares into your soul*",
                "I’d rather stay in the corner for now.",
                "Maybe later, okay?"
        ));


        dialoguePool.put("AMBITIOUS.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Yes! Thank you, <morph.name>!",
                "Good. This will do.",
                "Smells good. Thank you, <morph.name>.",
                "Just what I needed. Thank you, <morph.name>.",
                "I will return the favour."
        ));
        dialoguePool.put("AMBITIOUS.GIVE_ITEM.FAIL", Arrays.asList(
                "Thanks, but I don't need it right now.",
                "Maybe another time.",
                "I might take it later.",
                "Not now, but thanks.",
                "I can catch my own, thanks.",
                "I don’t need that at the moment."
        ));

        dialoguePool.put("AMBITIOUS.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "Alright… just for a moment.",
                "I appreciate your support.",
                "Oh, thank you, <morph.name>.",
                "This makes me feel better.",
                "I'm grateful.",
                "Fine… I’ll allow it.",
                "Thank you, <morph.name>."
        ));
        dialoguePool.put("AMBITIOUS.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Aren't you too close?",
                "No distractions.",
                "A little space, please.",
                "Later. I’m a little busy.",
                "This isn't the time."
        ));

        dialoguePool.put("AMBITIOUS.TALK.SUCCESS", Arrays.asList(
                "I’m going to be the best warrior… you’ll see.",
                "When I was younger... I tried to impress everyone by running ahead of everyone. I slipped and almost ate all the mud from the forest, and I learned something important... Strength is about patience.",
                "The clan needs cats who dream bigger. Like you, <morph.name>.",
                "Someday, I’ll lead something greater.",
                "There’s always more to learn. And there is always a better you to be. Never stop chasing your dreams, <morph.name>.",
                "Those Badgers will never stand a chance!.",
                "I had always wanted to be someone others can rely on. You can always count on me, <morph.name>.",
                "If I see those dogs, I will teach them a lesson!.",
                "How is your day, <morph.name>? Have you thought about expanding our territory?",
                "I don’t dream of power, you know? I dream of being useful when it matters most, of being there for everyone when they need it the most, just as they would be for me."
        ));
        dialoguePool.put("AMBITIOUS.TALK.FAIL", Arrays.asList(
                "I don’t have time for distractions.",
                "I need to focus.",
                "Talking will catch no prey.",
                "Not now. I’m thinking.",
                "We can talk when it's something important."
        ));


        dialoguePool.put("HUMBLE.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Thank you, <morph.name>. I truly appreciate it.",
                "You are very kind.",
                "This means a lot to me.",
                "Thank you from the heart.",
                "Thanks, <morph.name>. I appreciate it lots. ",
                "I’m grateful.",
                "If you're sharing it with me... thanks."
        ));
        dialoguePool.put("HUMBLE.GIVE_ITEM.FAIL", Arrays.asList(
                "I don't want to waste it.",
                "Someone else might need it more.",
                "Please, keep it.",
                "Maybe another time.",
                "Thank you… but not now.",
                "You should keep it. You might need it more than I do.",
                "I'm fine without it, but thank you.",
                "Save it for someone who needs it more."
        ));

        dialoguePool.put("HUMBLE.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "Thank you… truly.",
                "That brings me peace.",
                "You’re very kind, <morph.name>.",
                "I'm thankful for you.",
                "That warms my heart.",
                "Thank you. I'm glad you're here."
        ));
        dialoguePool.put("HUMBLE.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Maybe another time.",
                "Not now, sorry.",
                "Save that for someone better.",
                "No need to. Thanks anyway, <morph.name>!",
                "I shouldn't accept this.",
                "I appreciate it, but I'm fine."
        ));

        dialoguePool.put("HUMBLE.TALK.SUCCESS", Arrays.asList(
                "I try to do my best… every day. Have I been a good warrior, <morph.name>?",
                "I’m grateful for what I have. A home and friends to rely on.",
                "I don’t need much… just peace and a good friend like you, <morph.name>!",
                "Little moments like this mean a lot.",
                "As long as I’m useful, I’m happy to be here.",
                "I’m grateful to share my days with you and the clan, <morph.name>.",
                "Thank you for thinking of me, <morph.name>.",
                "I'm glad to be part of your clan.",
                "I like simple days. A clean den, a calm clan, and knowing I'm trying my best to be a honorable cat.",
                "When I was younger, I tried so hard to impress others... Now I just try to be useful, to be my best.",
                "I still remember my first successful hunt. It was small scrawny bird… barely enough for one. But I carried it like i just caught a fox."
        ));
        dialoguePool.put("HUMBLE.TALK.FAIL", Arrays.asList(
                "We can talk when I get some time.",
                "Tell me about it another time.",
                "We can leave it for later, okay?",
                "Sorry, not now. I need to think quietly.",
                "Maybe later… if that’s alright.",
                "I don't think I have much to talk right now."
        ));


        dialoguePool.put("RECKLESS.GIVE_ITEM.SUCCESS", Arrays.asList(
                "Nice! Thank you, <morph.name>.",
                "Did you catch this yourself?! Thank you, <morph.name>!",
                "Looks amazing! Thanks, <morph.name>.",
                "Yes! Just what I wanted!",
                "Throw it to me!",
                "Alright. Works for me."
        ));
        dialoguePool.put("RECKLESS.GIVE_ITEM.FAIL", Arrays.asList(
                "Oh come on! I could have caught a better one!",
                "I can help you hunt next time if that's all you could find.",
                "Do you need me to eat this?",
                "Give me something exciting.",
                "I'll pass, thank you anyway.",
                "Nah, I'll manage without it."
        ));

        dialoguePool.put("RECKLESS.SHOW_AFFECTION.SUCCESS", Arrays.asList(
                "Oh! Alright!",
                "Come here!",
                "I appreciate you too, <morph.name>.",
                "Aren't you a sweet smelly furball, <morph.name>?",
                "That's so sweet of you, <morph.name>!"
        ));
        dialoguePool.put("RECKLESS.SHOW_AFFECTION.FAIL", Arrays.asList(
                "Hey, too close.",
                "Don't hold me back!",
                "Do you know about personal space?",
                "You smell like mouse-bile.",
                "We can leave that for later."
        ));

        dialoguePool.put("RECKLESS.TALK.SUCCESS", Arrays.asList(
                "Shall we go hunt?",
                "If a badger shows up, I’m jumping first!",
                "Have you heard the dogs too? I promise I'll teach them a lesson!",
                "Standing still is boring. Could we go hunt?",
                "And trust me, if anything goes wrong... I'll deal with it!",
                "When I was a kit, they always warned me about bees. I never believed them, until one day I decided to bite one... I tell this story to every kit I see now.",
                "Once I chased a squirrel across the river. Next thing I knew was that my fur was already soaked and cold. I’d probably do it again...",
                "One time a fox snapped at me and I snapped back before thinking. My paws moved faster than I could think. That happens a lot to me.",
                "I once jumped over a fallen tree without knowing what was on the other side. Until I found myself stuck in brambles...",
                "When I was young, I climbed a tree higher than I should have. The view was beautiful. Getting down wasn’t so much."
        ));
        dialoguePool.put("RECKLESS.TALK.FAIL", Arrays.asList(
                "Talking? Boring!",
                "I’d rather be fighting or hunting.",
                "Nope. I’m busy being alive.",
                "Save the chatter for latter.",
                "I’m out. Peace.",
                "Sorry, I have better things to do."
        ));


        dialoguePool.put("CALM.GIFT", Arrays.asList(
                "Hey, <morph.name>, I thought you might like this.",
                "<morph.name>! This is for you!",
                "For you, I hope you like it.",
                "Here, this reminded me of you.",
                "Hey, <morph.name>! I got something for you."
        ));
        dialoguePool.put("GRUMPY.GIFT", Arrays.asList(
                "Just don't make a fuss over it.",
                "I thought you might like this.",
                "I don't expect a \"Thank you\".",
                "I hope you appreciate it.",
                "Here, enjoy, you're welcome."
        ));
        dialoguePool.put("CAUTIOUS.GIFT", Arrays.asList(
                "Hey, <morph.name>, this is for you.",
                "I got something for you.",
                "I wasn’t sure if you’d like it, but… here.",
                "Here… I think it might be useful.",
                "If you don’t like it, I can take it back."
        ));
        dialoguePool.put("INDEPENDENT.GIFT", Arrays.asList(
                "I got this myself. Hope you like it.",
                "<morph.name>, This is for you.",
                "I hope you like it.",
                "Here, enjoy.",
                "Sharing is caring, right?"
        ));
        dialoguePool.put("FRIENDLY.GIFT", Arrays.asList(
                "<morph.name>! I got something for you!",
                "Hey, this is for you.",
                "You are a nice friend, you deserve this.",
                "Hey, <morph.name>, I thought you might like this.",
                "I was thinking of giving you this for a while."
        ));
        dialoguePool.put("SHY.GIFT", Arrays.asList(
                "H-hey <morph.name>,  I thought you might like this.",
                "This is for you! Thank you for always being so nice.",
                "I hope this shows how much I appreciate you.",
                "For you!",
                "Um… I hope it’s okay."
        ));
        dialoguePool.put("AMBITIOUS.GIFT", Arrays.asList(
                "Now it's yours, I hope you appreciate it.",
                "Take this, you might need it more than me.",
                "Use it well.",
                "I expect you to make good use of it.",
                "Consider it an investment in you."
        ));
        dialoguePool.put("HUMBLE.GIFT", Arrays.asList(
                "It may not be much, but I tried my best.",
                "This is for you, I hope you like it!",
                "I hope this shows how much I appreciate your friendship.",
                "I told you I would return the favour!",
                "A little gift for a great friend like you."
        ));
        dialoguePool.put("RECKLESS.GIFT", Arrays.asList(
                "Hey, <morph.name>! See if this fits your likes!",
                "You looked a little lost in your thoughts. Maybe this will make you feel better!",
                "Hey, clumsy furball! Catch this!",
                "A little gift for a little furball.",
                "Hey, <morph.name>! Think fast!"
        ));


        dialoguePool.put("TALK.MATE.COMMON.SUCCESS", Arrays.asList(
                "Do you remember the day we met? I keep thinking about how peaceful it felt... just us.",
                "You always know how to make me feel better... I'm glad you're here.",
                "I trust you, <morph.name>. More than anyone else. I'm happy to be with you, I wanted you to know that.",
                "I saw something funny while I was out hunting the other day. I wished you were there so I could tell you about it.",
                "I didn't say it at the time... But when you came back safely that day, I felt so relieved.",
                "<morph.name>! I was waiting for you, any good news?",
                "You can always rely on me, <morph.name>. I'll always be by your side, no matter what.",
                "<morph.name>! How are you? Any good news?",
                "I’ve been thinking about our future lately, <morph.name>. Where do you think we’ll be seasons from now?",
                "Did you know that I love you, <morph.name>? Do not ever forget it.",
                "You have done so much for me, for us... One day I will return the favour.",
                "Hey, <morph.name>! Tell me about your day. I love listening to you.",
                "And remember, if something is ever bothering you, you can tell me. I will always want to carry it with you."
        ));
        dialoguePool.put("TALK.MATE.UNUSUAL.SUCCESS", Arrays.asList(
                "There was a time when I thought I’d never let anyone this close. Then you showed up... and everything changed.",
                "To me, you will always be my beloved dumb furball.",
                "Remember that day I let that squirrel escape? I still think you should have been faster than me, lazy furball!",
                "Hey, <morph.name>! I've missed you, where have you been?",
                "You can always rely on me, <morph.name>. I'll always be by your side, no matter what.",
                "Remember how we didn't get along at first? Who could have guessed...",
                "Sometimes I'm sorry I was so reluctant to be your friend at first, and then I remember you were always an annoying furball, but a really lovely one...",
                "<morph.name>! How are you? Have you eaten? Anything I could help you with?",
                "You stink a little, but I still love you, <morph.name>. I will always do.",
                "I have bad days sometimes... But you will always be who I care the most.",
                "Thank you for staying with me, <morph.name>, even when I’m so complicated.",
                "I still don't understand how you could choose such an annoying cat like me! But... I'm glad you did.",
                "I always love when we go out together. We should do it more often!"
        ));
        dialoguePool.put("TALK.MATE.SHY.SUCCESS", Arrays.asList(
                "<morph.name>! I-I wanted to ask... Do you feel as happy as I feel when you're with me too?",
                "H-hey! I missed you, where have you been? Have you eaten yet?",
                "Sometimes I feel lonely without you... I hope I can always stay by your side.",
                "I like quiet moments… where we don't need to talk at all, but everything still feels... Right.",
                "<morph.name>! Y-you have a little feather on your fur!",
                "If it wasn't for you... I probably would not talk to anyone at all...",
                "And if you ever feel lost... Or lonely like I used to, remember I will always love you, <morph.name>.",
                "O-oh, hey! I knew I could smell something sweet in the air.",
                "Thank you, <morph.name>… for staying by my side, even when I’m awkward with my feelings.",
                "I like being with you… even if we don’t talk much. It feels nice.",
                "I feel braver when you're here… like nothing bad could reach us.",
                "I might not say it too often but... I love you, <morph.name>!",
                "The other day I saw a butterfly, it was beautiful and it looked just like you! I wanted to catch it and save it forever, but then I remembered I already have you.",
                "You can always rely on me, always remember that, you sweet furball."
        ));

    }

/*
    private static final Map<Personality, Set<Item>> DESIRED_ITEMS = Map.of(
            Personality.CALM, Set.of(
                    Items.POPPY,
                    Items.ROSE_BUSH,


                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.GRUMPY, Set.of(
                    Items.BOOK,
                    Items.LIGHT_BLUE_DYE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.CAUTIOUS, Set.of(
                    Items.COOKED_SALMON,
                    Items.SWEET_BERRIES
            ),

            Personality.INDEPENDENT, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE
            ),

            Personality.FRIENDLY, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.SHY, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.AMBITIOUS, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.HUMBLE, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            ),

            Personality.RECKLESS, Set.of(
                    Items.COOKED_COD,
                    Items.CAKE,

                    ModItems.SQUIRREL_FOOD.get(),
                    ModItems.MOUSE_FOOD.get(),
                    ModItems.PIGEON_FOOD.get(),
                    Items.MUTTON,
                    Items.RABBIT,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.COD,
                    Items.SALMON,
                    Items.CHICKEN
            )
    );
    */


    public enum Personality {

        NONE, // 0

        // 1
        /**
         * Interacts normally. Normal cat, doesn't change anything.
         */
        CALM,

        // 2
        /**
         * Attacks monsters
         * <p>
         * Attacks the player for a little if it gets attacked, hisses sometimes
         * <p>
         * Responds aggresively sometimes, higher chance of interaction failed
         */
        GRUMPY,

        //3
        /**
         * Extremely low taming probability
         * Lower rate of picking up items
         */
        CAUTIOUS,

        //4
        /**
         * Attacks monsters <p>
         * Double wander radius, might not ask or lower chance of asking for food and herbs. <P>
         * Double stop and start distance in follow
         */
        INDEPENDENT,

        //5
        /**
         * 100% chance of taming
         * Purrs sometimes
         */
        FRIENDLY,

        //6
        /**
         * Low taming probability
         * Triple follow and stop distance
         * Mrrows sometimes
         */
        SHY,

        //7
        /**
         * Attacks monsters <p>
         * Higher rate of picking it up. Might end up eating it or consuming it.
         */
        AMBITIOUS,

        //8
        /**
         * ItemPickup goal, lower rate of picking up items
         */
        HUMBLE,

        //9
        /**
         * Attacks monsters 20 blocks<p>
         * Slightly higher rate of picking up items
         */
        RECKLESS,
    }

    public String getRandomMateDialogue(Personality personality, InteractionResult result) {

        loadDialogueMap();
        String firstKey = "";

        firstKey = switch (personality) {
            case NONE -> "MATE.COMMON.";
            case CALM -> "MATE.COMMON.";
            case GRUMPY -> "MATE.UNUSUAL.";
            case CAUTIOUS -> "MATE.UNUSUAL.";
            case INDEPENDENT -> "MATE.COMMON.";
            case FRIENDLY -> "MATE.COMMON.";
            case SHY -> "MATE.SHY.";
            case AMBITIOUS -> "MATE.COMMON.";
            case HUMBLE -> "MATE.COMMON.";
            case RECKLESS -> "MATE.COMMON.";
        };

        String key = "TALK." + firstKey + result.name();

        List<String> options = dialoguePool.getOrDefault(key, Arrays.asList("..."));

        return options.get(this.random.nextInt(options.size()));
    }


    public String getRandomDialogue(Personality personality, CatInteraction type, InteractionResult result) {

        loadDialogueMap();

        String key = personality.name() + "." + type.name() + "." + result.name();

        List<String> options = dialoguePool.getOrDefault(key, Arrays.asList("..."));

        return options.get(this.random.nextInt(options.size()));
    }

    public String getRandomGiftDialogue(Personality personality) {
        loadDialogueMap();
        String key = personality.name() + ".GIFT";
        List<String> options = dialoguePool.getOrDefault(key, Arrays.asList("..."));
        return options.get(this.random.nextInt(options.size()));
    }


    public static final EntityDataAccessor<Integer> FRIENDSHIP_SYNC =
            SynchedEntityData.defineId(WCatEntity.class, EntityDataSerializers.INT);


    private final Map<UUID, Integer> friendshipMap = new HashMap<>();

    public void syncFriendshipToPlayer(ServerPlayer player) {
        int value = getFriendshipLevel(player.getUUID());
        this.entityData.set(FRIENDSHIP_SYNC, value);
    }


    public int getFriendshipLevel(UUID playerUUID) {
        return friendshipMap.getOrDefault(playerUUID, 0);
    }

    public void setFriendshipLevel(UUID playerUUID, int value) {
        value = Math.max(0, Math.min(100, value));
        friendshipMap.put(playerUUID, value);
        if (!level().isClientSide) {
            Player player = level().getPlayerByUUID(playerUUID);
            if (player instanceof ServerPlayer sp) {
                syncFriendshipToPlayer(sp);
            }
        }
    }

    public float getMoodInteractionAddition() {
        Mood currentMood = this.getMood();
        switch (currentMood) {
            case HAPPY -> {
                return 0.2f;
            }
            case CALM -> {
                return 0f;
            }
            case STRESSED -> {
                return -0.3f;
            }
            case SAD -> {
                return -0.2f;
            }
        }
        return 0;
    }

    public void sendInteractionMessage(UUID playerUUID, String result) {
        ServerPlayer player = this.level().getServer().getPlayerList().getPlayer(playerUUID);
        String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(player.getName().toString());
        String resultCooked = result.replace("<morph.name>", morphName);

        if (player != null) {
            Component name = this.hasCustomName() ?
                    Component.literal("<").append(this.getCustomName().copy().withStyle(ChatFormatting.WHITE)).append("> ")
                    :
                    Component.literal("<???> ");
            if (this.getRank() != KIT && this.getRank() != APPRENTICE) {
                player.sendSystemMessage(Component.empty().append(name.copy()).append(Component.literal(resultCooked)));
            }
        }
    }

    public int getKittingInteractCooldown() {
        return this.entityData.get(KITTING_COOLDOWN);
    }

    public void setKittingInteractCooldown(int value) {
        this.entityData.set(KITTING_COOLDOWN, value);
    }

    public int getInteractionCooldown() {
        return this.entityData.get(INTERACTION_COOLDOWN);
    }

    public void setInteractionCooldown(int value) {
        this.entityData.set(INTERACTION_COOLDOWN, value);
    }

    public void randomImproveMood(UUID playerUUID) {
        if (this.random.nextFloat() <= (0.2 + (double) getFriendshipLevel(playerUUID) / 300)) {
            int randomMood = this.random.nextInt(1);
            Mood[] values = Mood.values();
            if (randomMood == 0) {
                this.entityData.set(MOOD, values[0].ordinal());
            } else {
                this.entityData.set(MOOD, values[1].ordinal());
            }
        }
    }

    public boolean randomInteractionResultProcess(UUID playerUUID, CatInteraction interaction) {

        if (interaction == CatInteraction.TALK) {
            if (this.getMateUUID() != null) {
                if (this.getMateUUID().equals(playerUUID)) {
                    if (this.random.nextFloat() <= 0.85 + getMoodInteractionAddition()) {
                        String dialogue = this.getRandomMateDialogue(this.getPersonality(), InteractionResult.SUCCESS);
                        this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                        this.sendInteractionMessage(playerUUID, dialogue);
                        this.randomImproveMood(playerUUID);
                        return true;
                    } else {
                        String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.TALK, InteractionResult.FAIL);
                        this.sendInteractionMessage(playerUUID, dialogue);
                        return false;
                    }
                }
            }
            if (this.getPersonality() == Personality.CALM) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);
                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.TALK, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    return false;
                }
            } else if (this.getPersonality() == Personality.GRUMPY) {
                if (this.random.nextFloat() <= 0.35 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 5);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);
                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.TALK, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 2);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.CAUTIOUS) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.TALK, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 2);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.INDEPENDENT) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.TALK, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.FRIENDLY) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 4);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.TALK, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.SHY) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 3);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.TALK, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 1);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.AMBITIOUS) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.TALK, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 1);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.HUMBLE) {
                if (this.random.nextFloat() <= 0.9 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.TALK, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.RECKLESS) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.TALK, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.TALK, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            }

        } else if (interaction == CatInteraction.GIVE_ITEM) {

            if (this.getPersonality() == Personality.CALM) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 4);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.GRUMPY) {
                if (this.random.nextFloat() <= 0.35 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 5);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 2);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.CAUTIOUS) {
                if (this.random.nextFloat() <= 0.3 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 4);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 3);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.INDEPENDENT) {
                if (this.random.nextFloat() <= 0.2 - getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.FRIENDLY) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 4);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 1);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.SHY) {
                if (this.random.nextFloat() <= 0.6 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 5);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.AMBITIOUS) {
                if (this.random.nextFloat() <= 0.9 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);
                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 3);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.HUMBLE) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 5);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.RECKLESS) {
                if (this.random.nextFloat() <= 0.5 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.GIVE_ITEM, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 3);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.GIVE_ITEM, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            }

        } else if (interaction == CatInteraction.SHOW_AFFECTION) {


            if (this.getPersonality() == Personality.CALM) {
                if (this.random.nextFloat() <= 0.7 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 3);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CALM, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.GRUMPY) {
                if (this.random.nextFloat() <= 0.1 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 7);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.GRUMPY, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 3);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.CAUTIOUS) {
                if (this.random.nextFloat() <= 0.3 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 4);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.CAUTIOUS, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 2);

                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.INDEPENDENT) {
                if (this.random.nextFloat() <= 0.6 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.INDEPENDENT, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.FRIENDLY) {
                if (this.random.nextFloat() <= 0.9 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 6);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.FRIENDLY, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.SHY) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.SHY, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.AMBITIOUS) {
                if (this.random.nextFloat() <= 0.6 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 1);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.AMBITIOUS, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.HUMBLE) {
                if (this.random.nextFloat() <= 0.8 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 3);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.HUMBLE, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            } else if (this.getPersonality() == Personality.RECKLESS) {
                if (this.random.nextFloat() <= 0.6 + getMoodInteractionAddition() + ((double) this.getFriendshipLevel(playerUUID) / 300)) {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.SHOW_AFFECTION, InteractionResult.SUCCESS);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) + 2);
                    this.sendInteractionMessage(playerUUID, dialogue);
                    this.randomImproveMood(playerUUID);

                    return true;
                } else {
                    String dialogue = this.getRandomDialogue(Personality.RECKLESS, CatInteraction.SHOW_AFFECTION, InteractionResult.FAIL);
                    this.setFriendshipLevel(playerUUID, this.getFriendshipLevel(playerUUID) - 1);
                    this.sendInteractionMessage(playerUUID, dialogue);

                    return false;
                }
            }

        }

        return false;
    }


    public Mood getMood() {
        int value = this.entityData.get(MOOD);
        if (value < 0 || value >= Mood.values().length) {
            return Mood.CALM;
        }
        return Mood.values()[value];
    }

    public void setRandomMood(RandomSource random) {
        Mood[] values = Mood.values();
        int index = random.nextInt(values.length);
        this.entityData.set(MOOD, values[index].ordinal());
    }

    public void setSpecificMood(Mood mood) {
        this.entityData.set(MOOD, mood.ordinal());
    }


    public void assignRandomPersonality(RandomSource random) {
        Personality[] values = Personality.values();
        int index = 1 + random.nextInt(values.length - 1);
        setPersonality(values[index]);
    }


    public Personality getPersonality() {
        int value = this.entityData.get(PERSONALITY);
        if (value < 0 || value >= Personality.values().length) {
            return Personality.NONE;
        }
        return Personality.values()[value];
    }

    public void setPersonality(Personality personality) {
        this.entityData.set(PERSONALITY, personality.ordinal());
    }

    public Component getClan() {
        return this.entityData.get(CLAN).orElse(Component.literal("None"));
    }

    public void setClan(Component clanName) {
        this.entityData.set(CLAN, Optional.of(clanName));
    }


    private static final String[] PREFIX_1 = {
            // 1, 10, 11
            //MultiColor
            "Leaf", "Marble", "Fleck", "Dapple", "Spotted",
            "Tawny", "Mottle", "Speckle", "Brindle", "Splotch",
            "Bumble", "Bright", "Sun", "Rainbow", "Pollen",
            "Drift", "Freckle", "Blotch", "Petal", "Shimmer",
            "Bloom"

    };
    private static final String[] PREFIX_2 = {
            // 2, 3, 9
            // Gray and cream
            "Pearl", "Mist", "Ivory", "Silk", "Feather",
            "Leaf", "Ash", "Fawn", "Soft", "Frost", "Snow",
            "Cloud", "Storm", "Sparrow", "Rat", "Bengal",
            "Willow", "Sand", "Dove", "Smoke", "Haze",
            "Drizzle", "Silver"
    };
    private static final String[] PREFIX_3 = {
            // 4, 8
            // Tabbies
            "Tiger", "Flame", "Ember", "Bracken", "Fire",
            "Oak", "Rust", "Maple", "Amber", "Hare",
            "Lion", "Dawn", "Dark", "Bumble", "Mole",
            "Sun", "Blaze", "Chestnut", "Fox", "Cinder",
            "Scorch", "Rowan", "Thorn", "Soot", "Red"
    };
    private static final String[] PREFIX_4 = {
            // 5, 6, 7, 12
            // Stone colors
            "Stone", "Jay", "Pebble", "Dark", "Ash",
            "Night", "Dust", "Mist", "Swift", "Shade",
            "Holly", "Mole", "Storm", "Sparrow", "Twitch",
            "Slate", "Raven", "Crow", "Dusk"


    };


    private static final String[] SUFIX = {
            "claw", "fur", "feather", "pelt", "eye",
            "heart", "tail", "wing", "whisker", "blaze",
            "fang", "shade", "step", "fall", "song",
            "stripe", "light", "leap", "foot", "spring",
            "pit", "stream", "patch", "scar", "ear",
            "frost"
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
            case 20 -> PREFIX_2; //13
            case 21 -> PREFIX_2; //14
            case 22 -> PREFIX_4; //15
            case 23 -> PREFIX_3; //16
            case 24 -> PREFIX_2; //17
            case 25 -> PREFIX_4; //18
            case 26 -> PREFIX_3; //19
            case 27 -> PREFIX_2; //20
            case 28 -> PREFIX_3; //21
            case 29 -> PREFIX_1; //22

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
        if (this.getPersonality() == Personality.INDEPENDENT) {
            return WCEConfig.COMMON.WILDCAT_WANDER_RADIUS.get() * 2;
        }
        return WCEConfig.COMMON.WILDCAT_WANDER_RADIUS.get();
    }

    public int getKittingTime() {
        return 20 * 60 * WCEConfig.COMMON.KITTING_MINUTES.get();
    }

    public int getKitGrowthTimeMinutes() {
        return WCEConfig.COMMON.KIT_GROWTH_MINUTES.get();
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Under certain conditions, proceed to follow the owner.
     */
    private class WCatFollowOwnerGoal extends Goal {

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


            if (cat instanceof WCatEntity wCatEntity && (wCatEntity.getPersonality() == Personality.INDEPENDENT || wCatEntity.getPersonality() == Personality.SHY)) {
                if (wCatEntity.getPersonality() == Personality.SHY) {
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
            if (wcat.mode != CatMode.FOLLOW) return false;
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

            if (wcat.mode != CatMode.FOLLOW) return false;
            if (cat.isOrderedToSit()) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

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
                    kitty -> kitty.isAlive() && kitty != this.cat && kitty.mode == CatMode.FOLLOW
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
            if (returnHomeFlag) return false;

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


            if (cat.mode != CatMode.WANDER) return false;

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
            this.cooldown = 400 + cat.getRandom().nextInt(4) * 80;
        }

        private Predicate<BlockState> defineTargetPredicate() {

            Block targetBlock = switch (cat.getRank()) {
                case KIT -> ModBlocks.MOSS_BED.get();
                case APPRENTICE -> ModBlocks.MOSS_BED.get();
                case WARRIOR -> ModBlocks.MOSS_BED.get();
                case MEDICINE -> ModBlocks.STONECLEFT.get();
                default -> ModBlocks.MOSS_BED.get();
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
            if (returnHomeFlag) return false;

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            if (!(cat instanceof WCatEntity)) return false;
            WCatEntity wcat = (WCatEntity) cat;

//            if (!cat.isTame()) return false;
            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

//            if (cat.isTame()) {
//                if (wcat.wanderCenter != null &&
//                        cat.blockPosition().distSqr(wcat.wanderCenter) > getWanderRadius() * getWanderRadius()) {
//                    return false;
//                }
//            } else {
//                if (wcat.wanderCenter != null &&
//                        cat.blockPosition().distSqr(wcat.wanderCenter) > 32 * 32) {
//                    return false;
//                }
//            }


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

            for (int i = 0; i < attempts; i++) {

                double angle = cat.getRandom().nextDouble() * (Math.PI * 2);
                double r;
                if (cat.isTame()) {
                    r = cat.getRandom().nextDouble() * getWanderRadius();
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

            if (wcat.mode != CatMode.WANDER) return false;
            if (cat.isOrderedToSit()) return false;

//            if (cat.isTame()) {
//                if (wcat.wanderCenter != null &&
//                        cat.blockPosition().distSqr(wcat.wanderCenter) > getWanderRadius() * getWanderRadius()) {
//                    return false;
//                }
//            } else {
//                if (wcat.wanderCenter != null &&
//                        cat.blockPosition().distSqr(wcat.wanderCenter) > 32 * 32) {
//                    return false;
//                }
//            }

            return !cat.getNavigation().isDone();
        }
    }


    static class WCatGiveRandomItemGoal extends Goal {
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
            if (cat.getMood() == Mood.SAD || cat.getMood() == Mood.STRESSED) return false;

            if (this.cat.getRank() != WARRIOR) return false;

            if (this.cat.mode != CatMode.WANDER) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            if (this.cat.random.nextFloat() > ((float) 1 / (20 * 60 * 15))) return false;

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
            int randomPool = this.cat.random.nextInt(4);

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


    static class WCatRunWithPlayerGoal extends Goal {
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

            if (this.cat.mode != CatMode.FOLLOW) return false;

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


    public class WCatLeaderCallsGoal extends Goal {
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
            this.cat.mode = CatMode.WANDER;
//            this.cat.getNavigation().moveTo(ownerPosition.getX(), ownerPosition.getY(), ownerPosition.getZ(), speed);
        }

        @Override
        public void stop() {
            if (cat.leaderCallingToSitFlag) {
                this.obeyingLeaderCallForTicks = BASE_OBEY_TICKS;
                cat.leaderCallingToFollowFlag = false;
                cat.leaderCallingToSitFlag = false;
                cat.mode = CatMode.SIT;
                cat.setOrderedToSit(true);
                cat.lookAtLeaderFlag = true;
                this.ownerPosition = null;
            }
            if (cat.leaderCallingToFollowFlag) {
                this.obeyingLeaderCallForTicks = BASE_OBEY_TICKS;
                cat.leaderCallingToFollowFlag = false;
                cat.leaderCallingToSitFlag = false;
                cat.mode = CatMode.FOLLOW;
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


    public BlockPos getHomePosition() {
        return homePosition;
    }

    public void setHomePosition(BlockPos pos) {
        this.homePosition = (pos == null) ? BlockPos.ZERO : pos;
    }


    public class ReturnHomeGoal extends Goal {
        private final WCatEntity cat;
        private final double speed;
        private BlockPos homeTarget;
        private int stuckTicks = 0;
        private Vec3 lastPos = Vec3.ZERO;

        private int tickCounterUntilHorizontalImpulse;
        private boolean countUntilHorizontalImpulse;


        public ReturnHomeGoal(WCatEntity cat, double speed) {
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

            return cat.mode == CatMode.WANDER && cat.distanceToSqr(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ()) > 5 * 5;
        }

        @Override
        public void start() {
            this.cat.getNavigation().moveTo(homeTarget.getX(), homeTarget.getY(), homeTarget.getZ(), speed);
        }

        @Override
        public void stop() {
            cat.wanderCenter = cat.blockPosition();
            cat.mode = CatMode.WANDER;
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

            if (dist < 4.0) {
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


    public class WCatMoveToMateGoal extends Goal {
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

            if (cat.mode != CatMode.WANDER) return false;

            if (cat.getMateUUID() == null) return false;
            if (cat.getMateUUID().equals(emptyUUID)) return false;

            if (cat.getMood() == Mood.SAD || cat.getMood() == Mood.STRESSED) return false;

            if (cat.getRandom().nextFloat() > 0.0001667f) return false;

            return findMate();
        }

        @Override
        public boolean canContinueToUse() {
            return targetMate != null && cat.mode == CatMode.WANDER;
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

                float chance = cat.random.nextFloat();
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


    public class WCatRandomLookAroundGoal extends RandomLookAroundGoal {
        private final WCatEntity cat;

        public WCatRandomLookAroundGoal(WCatEntity pMob) {
            super(pMob);
            this.cat = pMob;
        }

        @Override
        public boolean canUse() {
            if (this.cat.lookAtLeaderFlag && this.cat.isLookingAtLeader) return false;
            return super.canUse();
        }
    }


    public class WCatSeekShelterGoal extends Goal {
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
            if (returnHomeFlag) return false;

            if (cat.getTarget() != null && cat.getTarget().isAlive()) return false;

            if (cat.mode != CatMode.WANDER || cat.getTarget() != null) return false;
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


    public class MedicineHealsCats extends Goal {

        private final WCatEntity cat;
        private LivingEntity target;
        private int cooldown = 400;
        private int keepTicks = 0;
        private final int BASE_COOLDOWN = 600;
        private int healCooldown = 0;

        public MedicineHealsCats(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (returnHomeFlag) return false;

            if (cat.getMood() == Mood.SAD) return false;

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
                if (!hasPoultice()) {
                    if (!tryMakePoultice()) {
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
        private static final int BASE_COOLDOWN = 45;

        public WCatPickupItemGoal(WCatEntity cat) {
            this.cat = cat;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (returnHomeFlag) return false;

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
                cooldown = (int) ((BASE_COOLDOWN + cat.getRandom().nextInt(10)) * itemPickupChanceMultiplier());
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
            if (cat.distanceTo(target) < 1.42D) {
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

    /**
     * Depending on the rank, different conditions.
     * Then for every slot in the cat's inventory, verify that:
     * 1) If the slot is not empty, the item in the slot is the same as the target item, and the item count is less than 32, return true.
     * 2) If the slot is empty, return true as well.
     * If the for naturally ends, return false.
     */
    public boolean canAccept(ItemStack stack) {

        if (this.getRank() == MEDICINE) {
            if (!stack.is(ModTags.Items.HERBS)) return false;
        } else if (this.isBaby() && this.getRank() == KIT) {
            if (!(stack.is(Items.STICK) || stack.is(Items.MOSS_BLOCK) || stack.is(Items.SLIME_BALL))) return false;
        } else {
            if (!stack.is(ModTags.Items.PREY)) return false;
        }

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);

            if (!slot.isEmpty() && ItemStack.isSameItemSameTags(slot, stack) && slot.getCount() < 32) {
                return true;
            }

            if (slot.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean tryMakePoultice() {
        int emptySlotIndex = -1;
        int dockLeavesSlotIndex = -1;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(ModItems.DOCK_LEAVES.get())) {
                dockLeavesSlotIndex = i;
            }
            if (slot.isEmpty()) {
                emptySlotIndex = i;
            }
        }

        if (emptySlotIndex == -1 || dockLeavesSlotIndex == -1) return false;

        ItemStack ingredient = inventory.getItem(dockLeavesSlotIndex);

        ingredient.shrink(1);

        if (ingredient.isEmpty()) {
            inventory.setItem(dockLeavesSlotIndex, ItemStack.EMPTY);
        }

        inventory.setItem(emptySlotIndex, new ItemStack(ModItems.DOCK_POULTICE.get(), 2));

        this.level().playSound(null, this.blockPosition(),
                SoundEvents.SLIME_JUMP, SoundSource.NEUTRAL, 0.6F, 1.4F);

        return true;
    }

    private boolean hasPoultice() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(ModItems.DOCK_POULTICE.get())) {

                return true;
            }
        }
        return false;
    }

    private boolean tryHealClanmante(LivingEntity catToHeal) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.is(ModItems.DOCK_POULTICE.get())) {
                inventory.getItem(i).shrink(1);
                catToHeal.setHealth(catToHeal.getHealth() + 6F);
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

        if (this.getMateUUID() != null) {
            Entity mate = ((ServerLevel) this.level()).getEntity(this.getMateUUID());
            if (mate instanceof WCatEntity catMate) {
                catMate.entityData.set(MOOD, Mood.SAD.ordinal());
                catMate.setMateUUID(emptyUUID);
                catMate.setMate(Component.empty().append(this.getCustomName().copy()).append(Component.literal(" (deceased)").withStyle(ChatFormatting.GRAY)));
            }
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

        dropArmor(EquipmentSlot.HEAD);
        dropArmor(EquipmentSlot.CHEST);
        dropArmor(EquipmentSlot.LEGS);
        dropArmor(EquipmentSlot.FEET);

    }


    private double getThreatDetectionRange() {
        double range = 8D;

        switch (getPersonality()) {
            case RECKLESS -> range = 14D;
            case GRUMPY, INDEPENDENT, AMBITIOUS -> range = 8D;
        }

        return range;
    }

    private boolean willAttackMonsters() {
        boolean value = false;
        switch (getPersonality()) {
            case RECKLESS -> value = true;
            case GRUMPY -> value = true;
            case INDEPENDENT -> value = true;
            case AMBITIOUS -> value = true;
        }
        return value;
    }

    private double itemPickupChanceMultiplier() {
        double value = 1;
        switch (getPersonality()) {
            case RECKLESS -> value = 0.7;
            case AMBITIOUS -> value = 0.3;
            case CAUTIOUS -> value = 1.6;
            case HUMBLE -> value = 1.3;
        }
        return value;
    }


    @Override
    protected void registerGoals() {
        this.preyTarget = new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (target) -> {
            return mode == CatMode.WANDER && !this.returnHomeFlag && (target instanceof MouseEntity || target instanceof PigeonEntity || target instanceof SquirrelEntity);
        });

        this.monsterTarget = new NearestAttackableTargetGoal<>(this, LivingEntity.class,
                10, false, false,
                target -> target instanceof Monster && !(target instanceof Creeper || target instanceof Piglin || target instanceof ZombifiedPiglin || target instanceof PiglinBrute)
                        && this.distanceTo(target) <= getThreatDetectionRange() && willAttackMonsters() && this.getRank() != KIT
                        && this.mode != CatMode.SIT && !this.returnHomeFlag && target.isAlive() && !target.isDeadOrDying());


        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new ReturnHomeGoal(this, 1.1D));
        this.goalSelector.addGoal(0, new WCatLeaderCallsGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(3, new WCatSeekShelterGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new MedicineHealsCats(this));
        this.goalSelector.addGoal(4, new WCatPickupItemGoal(this));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WCatRunWithPlayerGoal(this, 1f));
        this.goalSelector.addGoal(6, new WCatFollowOwnerGoal(this, 1.2D, 1.2F, 7.0F));
        this.targetSelector.addGoal(7, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(8, this.monsterTarget);
        this.targetSelector.addGoal(9, this.preyTarget);
        this.goalSelector.addGoal(10, new WCAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(11, new WCatMoveToMateGoal(this));
        this.goalSelector.addGoal(12, new BoundedWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(13, new WCatGiveRandomItemGoal(this));
        this.goalSelector.addGoal(13, new WCatRandomLookAroundGoal(this));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(14, new CasualBlockSeekGoal(this, 1.0D, 15, 0.07D));

    }


    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ARMOR, 1.0D);
    }


//    public static void initSpawn(EntityType<WCatEntity> type) {
//        SpawnPlacements.register(
//                type,
//                SpawnPlacements.Type.ON_GROUND,
//                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//                (entityType, level, reason, pos, random)
//                        -> level.getBlockState(pos.below()).isSolid()
//        );
//    }

    private void dropArmor(EquipmentSlot slot) {
        ItemStack stack = getItemBySlot(slot);
        if (!stack.isEmpty()) {
            spawnAtLocation(stack.copy());
            setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    @Override
    protected void hurtArmor(DamageSource source, float amount) {
        if (amount <= 0) return;

        damageArmor(EquipmentSlot.HEAD, amount);
        damageArmor(EquipmentSlot.CHEST, amount);
        damageArmor(EquipmentSlot.LEGS, amount);
        damageArmor(EquipmentSlot.FEET, amount);
    }

    private void damageArmor(EquipmentSlot slot, float damage) {
        ItemStack stack = getItemBySlot(slot);
        if (stack.isEmpty()) return;

        int durabilityLoss = Math.max(1, Math.round(damage));

        stack.hurtAndBreak(
                durabilityLoss,
                this,
                e -> setItemSlot(slot, ItemStack.EMPTY)
        );
    }


    /**
     * This is just too much, if you have any questions about the mobinteract just ask me, i aint writing all this :sob:
     */
    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (this.getRank() == MEDICINE && this.isTame() && this.getOwner() == pPlayer) {
            if (PlayerShape.getCurrentShape(pPlayer) instanceof Animal) {
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
        }

        if ((itemstack.is(ModItems.CLAWS.get()))) {

            if (!level().isClientSide && pPlayer instanceof ServerPlayer sPlayer) {

                if (PlayerShape.getCurrentShape(sPlayer) instanceof Animal) {
                    if (!this.isBaby() && pPlayer.isShiftKeyDown() && this.isTame() && (this.getOwner() == pPlayer)) {
                        Component catInvName = this.getCustomName();
                        NetworkHooks.openScreen(
                                sPlayer,
                                new SimpleMenuProvider(
                                        (id, inv, player) -> new WCatMenu(id, inv, this.inventory, this),
                                        Component.literal(catInvName.getString())
                                ),
                                buf -> buf.writeInt(this.getId())
                        );
                    }

                    if (this.isBaby() && this.getRank() == KIT) {
                        if (!level().isClientSide) {
                            this.startRiding(pPlayer, true);
                            this.isBeingCarried = true;
                        }
                        return InteractionResult.sidedSuccess(level().isClientSide);
                    }
                }
            }

            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (!this.isTame() && itemstack.is(ModItems.FRESHKILL_AND_HERBS_BUNDLE.get())) {

            if (!this.level().isClientSide()) {

                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                int tameRoll;

                if (this.getPersonality() == Personality.CAUTIOUS) {
                    tameRoll = this.random.nextInt(6);
//                    pPlayer.sendSystemMessage(Component.literal("Cautious"));
                } else if (this.getPersonality() == Personality.SHY) {
                    tameRoll = this.random.nextInt(4);
//                    pPlayer.sendSystemMessage(Component.literal("Shy"));
                } else if (this.getPersonality() == Personality.FRIENDLY) {
                    tameRoll = 0;
//                    pPlayer.sendSystemMessage(Component.literal("Friendly"));
                } else {
                    tameRoll = this.random.nextInt(2);
//                    pPlayer.sendSystemMessage(Component.literal("Other"));
                }

                if (tameRoll == 0) {
                    this.tame(pPlayer);
                    this.level().broadcastEntityEvent(this, (byte) 7);

                    pPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                        String clanName = cap.getClanName();
                        this.setClan(Component.literal(clanName));
                    });


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

                        String finalName;
                        if (this.isBaby()) {
                            finalName = prefixSet[i] + "paw" + genderS;
                            this.setRank(APPRENTICE);
                        } else {
                            finalName = prefixSet[i] + SUFIX[j] + genderS;
                            this.setRank(WARRIOR);
                        }
                        //String finalName = prefixSet[i] + SUFIX[j] + genderS;

                        this.setCustomName(Component.literal(finalName));
                        this.setCustomNameVisible(true);
                        this.setPrefix(Component.literal(prefixSet[i]));

                        this.setNameColor(this.getRank());

                    }

                    this.setNameColor(this.getRank());


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
                    this.setRank(WARRIOR);
                    break;
                case WARRIOR:
                    this.setRank(MEDICINE);
                    break;
                case MEDICINE:
                    this.setRank(NONE);
                    break;
            }

            this.setNameColor(this.getRank());

            sendRankMessage(pPlayer);
            return InteractionResult.SUCCESS;
        }


        if (this.isTame() && itemstack.is(ModItems.CATMINT.get()) && this.getRank() != MEDICINE && !this.isExpectingKits() && !this.isBaby()) {
            if (!this.level().isClientSide()) {
                if (((ServerLevel) this.level()).getEntity(this.getMateUUID()) instanceof Player) {
                    return InteractionResult.PASS;
                }
            }

            if (!pPlayer.getAbilities().instabuild) itemstack.shrink(1);

            this.setInLove(pPlayer);


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

                    this.setNameColor(this.getRank());

                } else {
                    return InteractionResult.PASS;
                }


            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isTame() && itemstack.is(ModItems.MYSTIC_FLOWERS_BOUQUET.get())) {

            if (!this.level().isClientSide) {
                if (pPlayer instanceof ServerPlayer sPlayer && PlayerShape.getCurrentShape(sPlayer) instanceof WCatEntity) {
                    String sPlayerMorphName = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMorphName).orElse(sPlayer.getName().toString());
                    PlayerClanData.Age playerMorphAge = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMorphAge).orElse(PlayerClanData.Age.ADULT);

//                    if (sPlayerGender == this.getGender()) {
//                        pPlayer.sendSystemMessage(Component.literal(sPlayerMorphName + " is the same gender as "
//                                + (this.hasCustomName() ? this.getCustomName().getString() : "this cat"))
//                                .withStyle(ChatFormatting.YELLOW));
//                        return InteractionResult.PASS;
//                    } else {
//
//                    }
                    if (playerMorphAge != PlayerClanData.Age.ADULT) {
                        pPlayer.sendSystemMessage(Component.literal(sPlayerMorphName + " is not old enough for this.")
                                .withStyle(ChatFormatting.RED));
                        return InteractionResult.FAIL;
                    }
                    if (this.isBaby()) {
                        pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat")
                                        + " is not an adult!")
                                .withStyle(ChatFormatting.RED));
                        pPlayer.hurt(this.damageSources().magic(), 10f);
                        return InteractionResult.FAIL;
                    }

                    if (this.isForbiddenFromMatingPlayer() && this.getForbiddenPlayer() == pPlayer.getUUID()) {
                        pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat")
                                        + " is one of your descendants.")
                                .withStyle(ChatFormatting.GRAY));
                        return InteractionResult.FAIL;
                    }

                    if (this.getFriendshipLevel(sPlayer.getUUID()) < 98) {
                        pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat") + " doesn't like you that much!")
                                .withStyle(ChatFormatting.GRAY));
                        return InteractionResult.FAIL;
                    }
                    Entity currentMate = ((ServerLevel) this.level()).getEntity(this.getMateUUID());
                    if (currentMate instanceof Player) {
                        if (currentMate == pPlayer) {
                            pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat")
                                            + " is already " + sPlayerMorphName + "'s mate!")
                                    .withStyle(ChatFormatting.YELLOW));
                        } else {
                            pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat")
                                            + " can't take another mate!")
                                    .withStyle(ChatFormatting.YELLOW));
                        }
                        return InteractionResult.FAIL;
                    }

                    this.setMate(Component.literal(sPlayerMorphName));
                    this.setMateUUID(sPlayer.getUUID());
                    pPlayer.sendSystemMessage(Component.literal((this.hasCustomName() ? this.getCustomName().getString() : "This cat") + " and "
                                    + sPlayerMorphName + " are now a beautiful couple!")
                            .withStyle(ChatFormatting.GREEN));
                    pPlayer.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                        cap.setMateUUID(this.getUUID());
                        cap.setMateName(this.hasCustomName() ? this.getCustomName() : Component.literal("Undefined"));
                        ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), sPlayer);
                    });
                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_PURREOW, SoundSource.NEUTRAL, 0.6F, 1.0F);
                    this.lovingParticlesTicks = 600;

                    this.setForbiddingFutureGensFromMatingPlayer(true);
                    this.setForbiddenPlayer(pPlayer.getUUID());
                    this.entityData.set(MOOD, Mood.HAPPY.ordinal());

                    return InteractionResult.SUCCESS;

                }
            }

        }

        if (itemstack.getItem() == ModItems.WARRIOR_NAMETAG.get() && this.isTame()) {

            if (itemstack.hasCustomHoverName()) {
                itemstack.shrink(1);

                String fullName = itemstack.getHoverName().getString();

                String[] parts = fullName.split(" ");

                String genderV;
                if (this.getGender() == 0) {
                    genderV = " ♂";
                } else {
                    genderV = " ♀";
                }

                if (parts.length >= 2) {
                    String prefix = parts[0];
                    String sufix = parts[1];

                    this.setPrefix(Component.literal(prefix));

                    if (this.isBaby()) {
                        if (this.getRank() == APPRENTICE) {
                            this.setCustomName(Component.literal(prefix + "paw" + genderV));
                        } else {
                            this.setCustomName(Component.literal(prefix + "kit" + genderV));
                        }
                    } else {
                        this.setCustomName(Component.literal(prefix + sufix + genderV));
                    }

                } else {
                    String prefix = parts[0];

                    this.setPrefix(Component.literal(prefix));
                    this.setCustomName(Component.literal(prefix + genderV));

                }

                this.setNameColor(this.getRank());

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

                this.setNameColor(this.getRank());

            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (itemstack.is(ModItems.WHISKERS.get())) {
            if (!(PlayerShape.getCurrentShape(pPlayer) instanceof Animal)) return InteractionResult.PASS;


            if (!level().isClientSide()) {
                itemstack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(pHand));


                if (this.isTame() && this.getOwner() == pPlayer) {
                    if (this.getPersonality() == Personality.NONE || this.getPersonality() == null) {
                        this.assignRandomPersonality(this.random);
                    }
                    pPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                        String clanName = cap.getClanName();
                        if (this.getClan().equals(Component.literal("None"))
                                || !this.getClan().equals(Component.literal(cap.getClanName()))) {
                            this.setClan(Component.literal(clanName));
                        }
                    });

                    if (this.getMood() == null) {
                        this.setRandomMood(this.random);
                    }

                    Component clanText = this.getClan();
                    this.getPersonality();

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

                    String personalityText = switch (this.getPersonality()) {
                        case NONE -> "None";
                        case CALM -> "Calm";
                        case GRUMPY -> "Grumpy";
                        case CAUTIOUS -> "Cautious";
                        case INDEPENDENT -> "Independent";
                        case FRIENDLY -> "Friendly";
                        case SHY -> "Shy";
                        case AMBITIOUS -> "Ambitious";
                        case HUMBLE -> "Humble";
                        case RECKLESS -> "Reckless";
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

                    } else {
                        ageText = "Fully grown";
                    }

                    if (this.getKittingTicks() > 20) {
                        KitTime = String.format("%.2f min", kittingTime);
                    } else {
                        KitTime = "Not expecting";
                    }

                    this.lastMode = this.mode;
                    this.mode = CatMode.SIT;
                    this.setInSittingPose(true);
                    this.lookAtLeaderFlag = true;

                    if (!pPlayer.level().isClientSide && pPlayer instanceof ServerPlayer sPlayer) {
                        ModPackets.INSTANCE.send(
                                PacketDistributor.PLAYER.with(() -> sPlayer),
                                new OpenCatDataScreenPacket(this.getId())
                        );
                    }


//                Component msg = Component.literal(
//                                "=========================").withStyle(ChatFormatting.GRAY)
//                        .append("\n- Displaying cat information -").withStyle(ChatFormatting.WHITE)
//                        .append("\n=========================").withStyle(ChatFormatting.GRAY)
//                        .append("\nName: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(name).withStyle(ChatFormatting.WHITE))
//                        .append("\nClan: ").withStyle(ChatFormatting.GOLD)
//                        .append(clanText.copy().withStyle(ChatFormatting.WHITE))
//                        .append("\nGender: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(genderText).withStyle(ChatFormatting.WHITE))
//                        .append("\nRole: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(rankText).withStyle(ChatFormatting.WHITE))
//                        .append("\nPersonality: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(personalityText).withStyle(ChatFormatting.WHITE))
//                        .append("\nAge: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(ageText).withStyle(ChatFormatting.WHITE))
//                        .append("\nMate: ").withStyle(ChatFormatting.GOLD)
//                        .append(catMate.copy().withStyle(ChatFormatting.WHITE))
//                        .append("\nExpecting kits: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(expectingText).withStyle(ChatFormatting.WHITE))
//                        .append("\nTime until kits: ").withStyle(ChatFormatting.GOLD)
//                        .append(Component.literal(KitTime).withStyle(ChatFormatting.WHITE))
//                        .append("\n=========================").withStyle(ChatFormatting.GRAY);
//                if (this.level().isClientSide()) {
//                    pPlayer.displayClientMessage(msg, false);
//
//                }
                } else {
                    if (!pPlayer.level().isClientSide && pPlayer instanceof ServerPlayer sPlayer) {
                        ModPackets.INSTANCE.send(
                                PacketDistributor.PLAYER.with(() -> sPlayer),
                                new OpenCatDataScreenPacket(this.getId())
                        );
                    }
                }
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

            double pOffset = 0.1 + (1 / 3) * d + (0.05 * (Math.exp(0.5 * d) - 1));
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

        if (otherParent instanceof WCatEntity partner && this.isTame() && partner.isTame()) {


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

            }
            this.resetLove();
            partner.resetLove();
            this.entityData.set(MOOD, Mood.HAPPY.ordinal());
            partner.entityData.set(MOOD, Mood.HAPPY.ordinal());

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
            UUID MateUUID = otherParent.getUUID();
            UUID thisUUID = this.getUUID();

            this.setMate(MateName);
            partner.setMate(thisName);
            this.setMateUUID(MateUUID);
            partner.setMateUUID(thisUUID);

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
            if (this.getInteractionCooldown() > 0) {
                this.setInteractionCooldown(this.getInteractionCooldown() - 1);
            }
            if (this.getKittingInteractCooldown() > 0) {
                this.setKittingInteractCooldown(this.getKittingInteractCooldown() - 1);
            }

            if (this.wasBaby && !this.isBaby()) {
                this.setRank(WARRIOR);
                this.onGrewUp();
                this.applyAdultAttributes();
                this.setHealth(this.getMaxHealth());
            }
            this.wasBaby = this.isBaby();
        }

        /**
         * If the cats mode is sit:
         * if it isn't ordered to sit, order it to sit, then stop all navigations.
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
        if (this.tickCount % 20 != 0) return;
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

        ListTag list = new ListTag();

        for (var entry : friendshipMap.entrySet()) {
            CompoundTag t = new CompoundTag();
            t.putUUID("Player", entry.getKey());
            t.putInt("Level", entry.getValue());
            list.add(t);
        }

        tag.put("Friendships", list);


        if (homePosition != null) {
            tag.put("HomePos", NbtUtils.writeBlockPos(homePosition));
        }

        if (!this.entityData.get(HEAD_ARMOR).isEmpty()) {
            tag.put("HeadArmor", this.entityData.get(HEAD_ARMOR).save(new CompoundTag()));
        }
        if (!this.entityData.get(CHEST_ARMOR).isEmpty()) {
            tag.put("ChestArmor", this.entityData.get(CHEST_ARMOR).save(new CompoundTag()));
        }
        if (!this.entityData.get(LEGS_ARMOR).isEmpty()) {
            tag.put("LegsArmor", this.entityData.get(LEGS_ARMOR).save(new CompoundTag()));
        }
        if (!this.entityData.get(FEET_ARMOR).isEmpty()) {
            tag.put("FeetArmor", this.entityData.get(FEET_ARMOR).save(new CompoundTag()));
        }

        tag.putInt("Personality", this.getPersonality().ordinal());

        if (this.getClan() != null) {
            tag.putString("Clan", Component.Serializer.toJson(this.getClan()));
        }
        tag.putInt("InteractionCooldown", this.getInteractionCooldown());
        tag.putInt("KittingInteractCooldown", this.getKittingInteractCooldown());


        tag.putInt("WCatMode", mode.ordinal());
        tag.putInt("Variant", this.getVariant());
        tag.putInt("KittingTicks", this.getKittingTicks());
        tag.putInt("Rank", this.getRank().ordinal());
        tag.putBoolean("kitBorn", kitBorn);
        tag.putBoolean("AppScale", this.isAppScale());
        tag.put("Inventory", inventory.createTag());

        tag.putBoolean("ReturningHome", this.returnHomeFlag);

        tag.putBoolean("IsAnImage", this.isAnImage());


        if (this.getMate() != null) {
            tag.putString("Mate", Component.Serializer.toJson(this.getMate()));
        }
        if (this.getPrefix() != null) {
            tag.putString("Prefix", Component.Serializer.toJson(this.getPrefix()));
        }

        if (this.getMother() != null) {
            tag.putString("Mother", Component.Serializer.toJson(this.getMother()));
        }
        if (this.getFather() != null) {
            tag.putString("Father", Component.Serializer.toJson(this.getFather()));
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

        CompoundTag family = new CompoundTag();
        if (motherUUID != null)
            family.putUUID("Mother", motherUUID);
        if (fatherUUID != null)
            family.putUUID("Father", fatherUUID);
        if (mateUUID != null)
            family.putUUID("Mate", mateUUID);
        family.putInt("Generation", generation);
        tag.put("Family", family);


        tag.putBoolean("ForbidFG", this.isForbiddingFutureGensFromMatingPlayer());
        tag.putBoolean("Forbidden", this.isForbiddenFromMatingPlayer());
        if (this.getForbiddenPlayer() != null) {
            tag.putUUID("ForbiddenP", this.getForbiddenPlayer());
        }


    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);


        if (tag.contains("Family")) {
            CompoundTag family = tag.getCompound("Family");
            if (family.hasUUID("Mother"))
                motherUUID = family.getUUID("Mother");
            if (family.hasUUID("Father"))
                fatherUUID = family.getUUID("Father");
            if (family.hasUUID("Mate"))
                mateUUID = family.getUUID("Mate");
            generation = family.getInt("Generation");
        }


        friendshipMap.clear();

        if (tag.contains("HomePos")) {
            this.homePosition = NbtUtils.readBlockPos(tag.getCompound("HomePos"));
        } else {
            this.homePosition = BlockPos.ZERO;
        }

        ListTag list = tag.getList("Friendships", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            UUID playerId = t.getUUID("Player");
            int level = t.getInt("Level");
            friendshipMap.put(playerId, level);
        }

        if (tag.contains("HeadArmor")) {
            this.entityData.set(HEAD_ARMOR, ItemStack.of(tag.getCompound("HeadArmor")));
        }
        if (tag.contains("ChestArmor")) {
            this.entityData.set(CHEST_ARMOR, ItemStack.of(tag.getCompound("ChestArmor")));
        }
        if (tag.contains("LegsArmor")) {
            this.entityData.set(LEGS_ARMOR, ItemStack.of(tag.getCompound("LegsArmor")));
        }
        if (tag.contains("FeetArmor")) {
            this.entityData.set(FEET_ARMOR, ItemStack.of(tag.getCompound("FeetArmor")));
        }

        if (tag.contains("Rank")) {
            int value = tag.getInt("Rank");
            this.setRank(values()[value]);
        }

        if (tag.contains("Personality")) {
            int value = tag.getInt("Personality");
            this.setPersonality(Personality.values()[value]);
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

        if (tag.contains("InteractionCooldown")) {
            this.setInteractionCooldown(tag.getInt("InteractionCooldown"));
        }

        if (tag.contains("KittingInteractCooldown")) {
            this.setKittingInteractCooldown(tag.getInt("KittingInteractCooldown"));
        }


        if (tag.contains("Gender")) {
            this.setGender(tag.getInt("Gender"));
        }

        if (tag.contains("Mate")) {
            Component mate = Component.Serializer.fromJson(tag.getString("Mate"));
            this.setMate(mate);
        }

        if (tag.contains("Mother")) {
            Component name = Component.Serializer.fromJson(tag.getString("Mother"));
            this.setMother(name);
        }

        if (tag.contains("Father")) {
            Component name = Component.Serializer.fromJson(tag.getString("Father"));
            this.setFather(name);
        }

        if (tag.contains("Clan")) {
            Component clan = Component.Serializer.fromJson(tag.getString("Clan"));
            this.setClan(clan);
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


        if (tag.contains("ReturningHome")) {
            this.returnHomeFlag = tag.getBoolean("ReturningHome");
        }


        if (tag.contains("Variant")) {
            this.setVariant(tag.getInt("Variant"));
        }

        if (tag.contains("ForbidFG")) {
            this.setForbiddingFutureGensFromMatingPlayer(tag.getBoolean("ForbidFG"));
        }
        if (tag.contains("Forbidden")) {
            this.setForbiddenFromMatingPlayer(tag.getBoolean("Forbidden"));
        }
        if (tag.hasUUID("ForbiddenP")) {
            this.setForbiddenPlayer(tag.getUUID("ForbiddenP"));
        } else {
            this.setForbiddenPlayer(null);
        }

        if (tag.contains("IsAnImage")) {
            this.setAnImage(tag.getBoolean("IsAnImage"));
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

//        Vec3 velocity = this.getDeltaMovement();
//        float avgVelocity = (float)((Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f);
//        boolean fuckingMovingConditions = avgVelocity >= 0.015f || this.walkAnimation.speed() != 0;
//        boolean isKittyMoving = fuckingMovingConditions || tAnimationState.isMoving();

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

        if ((tAnimationState.isMoving()) && !this.isCrouching()) {

            /*
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

             */

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


        if (this.random.nextInt(1200) == 0 && (!AnimationClientData.isPlayerShape) && !this.isBeingCarried && !this.isAnImage()) {

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
            if ((tAnimationState.isMoving()) && this.isCrouching()) {
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


    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of(
                getItemBySlot(EquipmentSlot.HEAD),
                getItemBySlot(EquipmentSlot.CHEST),
                getItemBySlot(EquipmentSlot.LEGS),
                getItemBySlot(EquipmentSlot.FEET)
        );
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> entityData.get(HEAD_ARMOR);
            case CHEST -> entityData.get(CHEST_ARMOR);
            case LEGS -> entityData.get(LEGS_ARMOR);
            case FEET -> entityData.get(FEET_ARMOR);
            default -> ItemStack.EMPTY;
        };
    }


    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        switch (slot) {
            case HEAD -> entityData.set(HEAD_ARMOR, stack.copy());
            case CHEST -> entityData.set(CHEST_ARMOR, stack.copy());
            case LEGS -> entityData.set(LEGS_ARMOR, stack.copy());
            case FEET -> entityData.set(FEET_ARMOR, stack.copy());
        }
    }


    public void setNameColor(Rank rank) {
        if (!WCEConfig.COMMON.COLORED_NAMES.get()) return;

        TextColor none = TextColor.fromRgb(0xFFFFFF);
        TextColor kit = TextColor.fromRgb(0x42fcb5);
        TextColor apprentice = TextColor.fromRgb(0xeefc90);
        TextColor warrior = TextColor.fromRgb(0xffac3b);
        TextColor medicine = TextColor.fromRgb(0x56bdfc);

        TextColor colorToUse;
        switch (rank) {
            case KIT -> colorToUse = kit;
            case APPRENTICE -> colorToUse = apprentice;
            case WARRIOR -> colorToUse = warrior;
            case MEDICINE -> colorToUse = medicine;
            default -> colorToUse = none;
        }

        Component actualName = this.getCustomName();
        if (actualName != null) {
            this.setCustomName(
                    Component.literal(actualName.getString())
                            .withStyle(style -> style.withColor(colorToUse))
            );

            this.setCustomNameVisible(true);
        }

    }

    /**
     * Sends a different message depending on the cat's mode.
     */
    public void sendModeMessage(Player player) {
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

    public void sendModeMessage(Player player, CatMode cMode) {
        String name = this.getName().getString();
        switch (cMode) {
            case SIT:
                player.displayClientMessage(Component.literal(name + " will stay."), true);
                break;
            case FOLLOW:
                player.displayClientMessage(Component.literal(name + " will follow you."), true);
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
                kit.setRank(KIT);
                kit.kitBorn = true;
                String finalName = "";
                int randomVariant = this.random.nextInt(maxVariants);
                kit.setVariant(randomVariant);

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
                    if (prefixSet[k].equals(lastPrefix)) {
                        k = (int) (kit.random.nextInt(prefixSet.length) / 1.5F);
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

                kit.wanderCenter = this.blockPosition();
                kit.applyBabyAttributes();
                kit.setHealth(kit.getMaxHealth());
                kit.setNameColor(KIT);

                kit.assignRandomPersonality(kit.getRandom());

                kit.setHomePosition(this.getHomePosition());
                kit.setClan(this.getClan());

                LivingEntity father = this.getMateEntity();

                if (father != null) {
                    kit.setFatherUUID(father.getUUID());
                }
                kit.setMotherUUID(this.getUUID());
                if (father instanceof WCatEntity catFather) {
                    int gen = Math.max(this.getGeneration(), father != null ? catFather.getGeneration() : 0) + 1;
                    kit.setGeneration(gen);
                }

                kit.setMother(this.getCustomName());
                kit.setFather(this.getMate());

                kit.entityData.set(MOOD, Mood.CALM.ordinal());

                if (this.isForbiddingFutureGensFromMatingPlayer()) {
                    kit.setForbiddenPlayer(this.getForbiddenPlayer());
                    kit.setForbiddenFromMatingPlayer(true);
                    kit.setForbiddingFutureGensFromMatingPlayer(true);
                }


                server.addFreshEntity(kit);
            }
        }

    }

    public @Nullable LivingEntity getMateEntity() {

        UUID mateUUID = this.getMateUUID();
        if (mateUUID == null) return null;

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        Entity entity = serverLevel.getEntity(mateUUID);
        if (entity instanceof WCatEntity || entity instanceof Player) {
            return ((LivingEntity) entity);
        }

        return null;
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
            this.setNameColor(this.getRank());
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

        this.entityData.define(HEAD_ARMOR, ItemStack.EMPTY);
        this.entityData.define(CHEST_ARMOR, ItemStack.EMPTY);
        this.entityData.define(LEGS_ARMOR, ItemStack.EMPTY);
        this.entityData.define(FEET_ARMOR, ItemStack.EMPTY);

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

        this.entityData.define(PERSONALITY, 0);
        this.entityData.define(CLAN, Optional.empty());
        this.entityData.define(FRIENDSHIP_SYNC, 0);
        this.entityData.define(MOOD, Mood.CALM.ordinal());
        this.entityData.define(INTERACTION_COOLDOWN, 0);
        this.entityData.define(KITTING_COOLDOWN, 0);

        this.entityData.define(MOTHER, Optional.empty());
        this.entityData.define(FATHER, Optional.empty());

        this.entityData.define(IS_AN_IMAGE, false);

    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if (!this.level().isClientSide && !moodLoaded) {
            this.setRandomMood(this.level().getRandom());
            moodLoaded = true;
        }
        if (!this.level().isClientSide) {
            this.setRank(this.getRank());
            this.setSpecificMood(this.getMood());
            this.setPersonality(this.getPersonality());
        }
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

//        float scale = switch (variant) {
//            case 0 -> 1f;
//            case 1 -> 1f;
//            case 2 -> 1f;
//            case 3 -> 1f;
//            case 4 -> 1f;
//            case 5 -> 1f;
//            case 6 -> 1f;
//            case 7 -> 1f;
//            case 8 -> 1f;
//            case 9 -> 1f;
//            case 10 -> 1f;
//            case 11 -> 1f;
//            case 12 -> 1f; //chestnutpatch
//            case 13 -> 1f; //ratstar
//            case 14 -> 1f; //twitchstream
//            case 15 -> 1f; //blazepit
//            case 16 -> 1f; //bengalpelt
//            case 17 -> 1f; //sparrowstar
//            case 18 -> 1f; //foxeater
//            case 19 -> 1f; //willowsong
//            default -> 1f;
//        };
        float scale = 1f;


        this.entityData.set(SCALE, scale);
    }


    @Override
    public int getExperienceReward() {
        return 25 + 5 * this.random.nextInt(3);
    }

    @Override
    public void rideTick() {

        super.rideTick();

        Entity vehicle = this.getVehicle();
        if (vehicle == null) return;

        float yawDeg;
        float pitchDeg;

        if (vehicle instanceof LivingEntity) {
            yawDeg = ((LivingEntity) vehicle).yBodyRot;
            pitchDeg = vehicle.getXRot();

        } else {
            yawDeg = vehicle.getYRot();
            pitchDeg = vehicle.getXRot();
        }

        double yaw = Math.toRadians(-yawDeg);

        double dirX = Math.sin(yaw);
        double dirZ = Math.cos(yaw);

        double distance = 0.66;

        double offsetY = 0.15;

        double pitch = Math.toRadians(pitchDeg);

        double verticalOffset = Math.sin(-pitch) * 0.4;

        double offsetX = dirX * distance + (verticalOffset / 5);
        double offsetZ = dirZ * distance + (verticalOffset / 5);

        this.setPos(
                vehicle.getX() + offsetX,
                vehicle.getY() + offsetY + verticalOffset,
                vehicle.getZ() + offsetZ
        );

        float sideYaw = yawDeg + 200F;

        this.setYRot(sideYaw);
        this.setYHeadRot(sideYaw);

    }


    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
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

            if (this.isPassenger()) {
                Entity vehicle = this.getVehicle();
                if (vehicle instanceof Player player) {
                    if (player.isShiftKeyDown() || !(PlayerShape.getCurrentShape(player) instanceof WCatEntity)) {
                        this.stopRiding();
                        this.isBeingCarried = false;
                    }
                }
            }

            if (lovingParticlesTicks > 0) {
                lovingParticlesTicks--;
                float chance = this.random.nextFloat();
                if (chance <= 0.05f) {
                    Entity mate = ((ServerLevel) this.level()).getEntity(this.getMateUUID());
                    if (mate != null) {
                        this.getNavigation().moveTo(mate, 1f);
                        this.getLookControl().setLookAt(mate, (float) (this.getMaxHeadYRot() + 20), (float) this.getMaxHeadXRot());
                    }
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.HEART, this.getX(), this.getY(), this.getZ(), 2, 0.5f, 0.5f, 0.5f, 0.1f);
                    if (chance < 0.03) {
                        this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_PURR, SoundSource.NEUTRAL, 0.4F, 1.0F);
                    }
                }
            }

            if (this.getMood() == Mood.SAD) {
                Vec3 currentMovement = this.getDeltaMovement();
                this.setDeltaMovement(currentMovement.x * 0.7, currentMovement.y, currentMovement.z * 0.7);
            }

            if (this.mode == CatMode.SIT && this.lookAtLeaderFlag) {
                LivingEntity owner = this.getOwner();
                if (owner != null) {
                    if (this.distanceToSqr(owner) <= 100) {
                        this.getLookControl().setLookAt(this.getOwner(), (float) (this.getMaxHeadYRot() + 20), (float) this.getMaxHeadXRot());
                        this.isLookingAtLeader = true;
                    } else {
                        this.isLookingAtLeader = false;
                    }
                }
            } else if (this.mode != CatMode.SIT) {
                this.lookAtLeaderFlag = false;
                this.isLookingAtLeader = false;
            }

            if (this.getPersonality() == Personality.AMBITIOUS) {
                if (this.random.nextFloat() <= ((float) 1 / 18000)) this.eatPreyInInventory();
            }

            if (this.level().isThundering()) {
                if (this.random.nextFloat() <= 0.000013) {
                    if (this.level().canSeeSky(this.blockPosition())) {
                        this.setHealth(this.getMaxHealth() / 10);
                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level());
                        if (lightning != null) {
                            lightning.moveTo(this.getX(), this.getY(), this.getZ());
                            this.level().addFreshEntity(lightning);
                        }
                    }
                }
            }

            if (this.tickCount % 9600 == 0) {
                this.setRandomMood(this.random);
            }

            if (this.tickCount % 100 == 0) {
                if (this.getTarget() != null) {
                    if (!this.getTarget().isAlive()) {
                        this.setTarget(null);
                    }
                }


                float moonsCalc = (float) ((this.getAge() + (20 * 60 * getKitGrowthTimeMinutes())) / (100.0 * getKitGrowthTimeMinutes()));
                this.entityData.set(AGE_SYNC, moonsCalc);
            }

            if (this.tickCount % 400 == 0 && this.level() instanceof ServerLevel serverLevel) {

                UUID fatherUUID = this.getFatherUUID();
                if (fatherUUID != null && !fatherUUID.equals(emptyUUID)) {
                    Entity father = serverLevel.getEntity(fatherUUID);
                    if (father instanceof Player player) {
                        String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                                .map(PlayerClanData::getMorphName)
                                .orElse(player.getName().getString());

                        if (!morphName.equals(this.getFather().getString())) {
                            this.setFather(Component.literal(morphName));
                        }
                    }
                }

                UUID motherUUID = this.getMotherUUID();
                if (motherUUID != null && !motherUUID.equals(emptyUUID)) {
                    Entity mother = serverLevel.getEntity(motherUUID);
                    if (mother instanceof Player player) {
                        String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                                .map(PlayerClanData::getMorphName)
                                .orElse(player.getName().getString());

                        if (!morphName.equals(this.getMother().getString())) {
                            this.setMother(Component.literal(morphName));
                        }
                    }
                }
            }


            if (soundTick > 0) {
                soundTick--;
            }

            if (grumpyAtOwnerTick > 0) {
                grumpyAtOwnerTick--;

                if (grumpyAtOwnerTick == 0) {

                    if (this.getTarget() != null && this.getTarget().equals(this.getOwner())) {
                        this.setTarget(null);
                    }
                }
            }

            if (this.tickCount % 600 == 0 && this.random.nextInt(3) == 0 && soundTick == 0) {
                soundTick = 800;
                if (this.getPersonality() == Personality.GRUMPY) {
                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_HISS, SoundSource.AMBIENT, 0.4f, 0.9f);

                } else if (this.getPersonality() == Personality.SHY) {
                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_STRAY_AMBIENT, SoundSource.AMBIENT, 0.8f, 0.9f);
                } else if (this.getPersonality() == Personality.FRIENDLY) {
                    this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_PURREOW, SoundSource.AMBIENT, 1f, 1.1f);
                }
            }


            if (catSniffTickCooldown > 0) catSniffTickCooldown--;

            if (scentDirection != null && scentDistance < scentMaxDistance) {
                scentTick++;

                if (scentTick % 2 == 0) {
                    Vec3 particlePos = scentStartPos.add(scentDirection.scale(scentDistance));

                    ((ServerLevel) this.level()).sendParticles(
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

            if (!apprenticeAge && this.getAge() >= -((getKitGrowthTimeMinutes() * 60 * 20) / 2)
                    && this.kitBorn && this.isTame()) {
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
                this.setNameColor(this.getRank());
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
        if (value < 0 || value >= values().length) {
            return NONE;
        }
        return values()[value];
    }

    public void setRank(Rank rank) {
        this.entityData.set(RANK, rank.ordinal());

        if (WCEConfig.COMMON.COLORED_NAMES.get()) this.setNameColor(rank);
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
        this.wanderCenter = this.blockPosition();
        this.assignRandomPersonality(this.random);
        if (this.getAge() < 0 && this.getAge() > -25000) {
            int minutes = WCEConfig.COMMON.KIT_GROWTH_MINUTES.get();
            int growingTicks = minutes * 20 * 60;
            this.setAge((growingTicks/2) + 100);
            this.setAppScale(true);
        }

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
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);

        if (!level().isClientSide && result) {
            Entity enemy = source.getEntity();

            if (enemy instanceof LivingEntity livingEnemy) {

                if (livingEnemy.equals(this.getOwner()) && this.getPersonality() == Personality.GRUMPY) {
                    this.setTarget(livingEnemy);
                    this.grumpyAtOwnerTick = 37;
                }
                this.alertNearbyAllies(livingEnemy);
            }
        }

        return result;
    }


    private void alertNearbyAllies(LivingEntity enemy) {
        double radius = 16.0D;

        if (!this.isTame() && this.getOwner() == null) {

            List<WCatEntity> allies = level().getEntitiesOfClass(
                    WCatEntity.class,
                    this.getBoundingBox().inflate(radius),
                    cat ->
                            cat != this &&
                                    !cat.isTame() &&
                                    cat.getOwner() == null &&
                                    !cat.isDeadOrDying()
            );

            if (enemy.isAlive()) {
                for (WCatEntity ally : allies) {
                    ally.setTarget(enemy);
                    ally.setLastHurtByMob(enemy);
                }
            }

            return;

        }

        if (this.isTame() && this.getOwner() != null) {
            if (enemy == this.getOwner()) return;

            List<WCatEntity> allies = level().getEntitiesOfClass(
                    WCatEntity.class,
                    this.getBoundingBox().inflate(radius),
                    cat ->
                            cat != this &&
                                    cat.isTame() &&
                                    cat.getOwner() != null &&
                                    cat.getOwner().getUUID().equals(this.getOwner().getUUID()) &&
                                    !cat.isDeadOrDying()
            );

            if (enemy.isAlive()) {
                for (WCatEntity ally : allies) {
                    ally.setTarget(enemy);
                    ally.setLastHurtByMob(enemy);
                }
            }

        }

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

    @Override
    public float getScale() {
        return 1f;
    }

    public void eatPreyInInventory() {
        int slotIndexToConsume = 0;
        boolean itemFound = false;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);

            if (slot.is(ModTags.Items.PREY)) {
                slotIndexToConsume = i;
                itemFound = true;
            }
        }

        if (itemFound) {
            inventory.getItem(slotIndexToConsume).shrink(1);
            ServerLevel sLevel = ((ServerLevel) this.level());
            sLevel.playSound(null, this.blockPosition(), SoundEvents.CAT_EAT, SoundSource.NEUTRAL, 0.6F, 1.0F);
            sLevel.playSound(null, this.blockPosition(), SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL, 0.6F, 1.0F);
            sLevel.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 0.6F, 1.0F);
            sLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.BEEF)), this.getX(), this.getY(), this.getZ(), 30, 0.4f, 0.4f, 0.4f, 0.1f);
        }

    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.IN_WALL) && this.isBaby() && this.isBeingCarried) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }


}
