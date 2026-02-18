package net.snowteb.warriorcats_events.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSPerformLeapPacket;
import net.snowteb.warriorcats_events.sound.ModSounds;
import tocraft.walkers.api.PlayerShape;

import javax.annotation.Nullable;
import java.util.UUID;

public class LeapClientState {
    private static int shiftKeyDownCounter = 0;
    private static int leapPowerCounter = 0;
    private static boolean attackWasDown = false;

    @Nullable
    private static Entity lockedLookEntity = null;
    private static boolean wasLookKeyDown = false;
    private static boolean lockingTarget = false;


    private static final Component STICK_GREEN =
            Component.literal("▋").withStyle(ChatFormatting.GREEN);
    private static final Component STICK_GRAY =
            Component.literal("▋").withStyle(ChatFormatting.DARK_GRAY);


    public static void tick(boolean shifting) {


        if (Minecraft.getInstance().player != null) {
            if (!(PlayerShape.getCurrentShape(Minecraft.getInstance().player) instanceof Animal)) return;
        }

        if (shifting && Minecraft.getInstance().player.onGround()) {

            if (shiftKeyDownCounter > 142 || !Minecraft.getInstance().player.onGround()) return;

            boolean lookKeyDown = Minecraft.getInstance().options.keyUse.isDown();
            if (lookKeyDown && !wasLookKeyDown && Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                lockedLookEntity = getEntityPlayerIsLookingAtClient(Minecraft.getInstance().player, 15.0D);
            }

            if (lockedLookEntity != null) {
                if (!lockedLookEntity.isAlive()
                        || lockedLookEntity.distanceTo(Minecraft.getInstance().player) > 17.0F) {
                    lockedLookEntity = null;
                    lockingTarget = false;
                } else {
                    if (lockingTarget) {
                        forceLookAt(lockedLookEntity, Minecraft.getInstance().getFrameTime());
                    }
                }
            }

            boolean attackDown = Minecraft.getInstance().options.keyAttack.isDown();

            shiftKeyDownCounter++;

            if (shiftKeyDownCounter >= 20) {
                leapPowerCounter+=2;
                leapPowerCounter = Math.min(leapPowerCounter, 100);
                assert Minecraft.getInstance().player != null;
                if (shiftKeyDownCounter <= 140 && Minecraft.getInstance().player.onGround()) {
                    Minecraft.getInstance().player.playSound(ModSounds.SHORT_WOOSH.get(), 0.3f, 0.7f);
                } else {
                    leapPowerCounter = 0;
                    lockingTarget = false;
                }

                if (leapPowerCounter > 0) {
                    if (shiftKeyDownCounter <= 140 && Minecraft.getInstance().player.onGround()
                            && attackDown && !attackWasDown) {
                        lockedLookEntity = null;
                        wasLookKeyDown = false;
                        lockingTarget = false;
                        ModPackets.sendToServer(new CtSPerformLeapPacket(leapPowerCounter));
                        Minecraft.getInstance().player.displayClientMessage(
                                Component.literal("Leap!").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC), true);
                        leapPowerCounter = 0;
                        shiftKeyDownCounter = 0;
                    }
                }
                wasLookKeyDown = lookKeyDown;
                attackWasDown = attackDown;

            }

        } else {
            lockedLookEntity = null;
            wasLookKeyDown = false;
            lockingTarget = false;
            leapPowerCounter = 0;
            shiftKeyDownCounter = 0;

        }



    }


//    public static Component buildLeapPowerBar(int leapPowerCounter) {
//        int greenCount = Mth.clamp(leapPowerCounter/2, 0, 50);
//
//        MutableComponent bar = Component.empty();
//
//        for (int i = 0; i < greenCount; i++) {
//            bar.append(STICK_GREEN);
//        }
//
//        for (int i = greenCount; i < 50; i++) {
//            bar.append(STICK_GRAY);
//        }
//
//        return bar;
//    }


    @Nullable
    public static Entity getEntityPlayerIsLookingAtClient(LocalPlayer player, double range) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(range));

        AABB searchBox = player.getBoundingBox()
                .expandTowards(lookVec.scale(range))
                .inflate(2.0D);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                reachVec,
                searchBox,
                e -> e.isAlive() && e != player
                        && !(e instanceof TamableAnimal cat && cat.isTame() && (cat.getOwner() == player || isClanedWith(cat, player)))
        );

        if (result != null) {
            if (!lockingTarget) {
                lockingTarget = true;
                player.displayClientMessage(Component.literal("Target Locked").withStyle(ChatFormatting.GRAY), true);
            } else {
                lockingTarget = false;
                player.displayClientMessage(Component.literal("Target unlocked").withStyle(ChatFormatting.DARK_GRAY), true);
            }
        } else {
            if (lockingTarget) {
                player.displayClientMessage(Component.literal("Target unlocked").withStyle(ChatFormatting.DARK_GRAY), true);
                lockingTarget = false;
            }
        }

        return result != null ? result.getEntity() : null;
    }


    public static void forceLookAt(Entity target, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || target == null) return;

        Vec3 eyes = player.getEyePosition(partialTick);
        Vec3 targetPos = target.getEyePosition(partialTick);

        Vec3 dir = targetPos.subtract(eyes);

        double dx = dir.x;
        double dy = dir.y;
        double dz = dir.z;

        double distXZ = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90F);
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, distXZ)));


        player.setYRot(Mth.lerp(0.25F, player.getYRot(), yaw));
//        player.setXRot(Mth.lerp(0.25F, player.getXRot(), pitch));
        player.yRotO = yaw;
        player.xRotO = pitch;
    }




    public static int getLeapPowerCounter() {
        return leapPowerCounter;
    }

    public static boolean isLeapActive() {
        return shiftKeyDownCounter < 144;
    }

    public static boolean isLockingTarget() {
        return lockingTarget;
    }

    public static boolean isClanedWith(TamableAnimal animal, LocalPlayer player) {
        if (animal instanceof WCatEntity cat && cat.isTame() && !cat.isOwnedBy(player)) {
            UUID clanUUID = ClientClanData.get().getCurrentClanUUID();
            if (!clanUUID.equals(ClanData.EMPTY_UUID)) {
                if (cat.getClanUUID().equals(clanUUID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void setCanceled(){
        shiftKeyDownCounter = 150;
    }
}
