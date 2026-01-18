package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.OpenAncientStickScreenPacket;

import javax.annotation.Nullable;
import java.util.List;

public class AncientStickItem extends Item {

    public AncientStickItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if (!pLevel.isClientSide) {

            List<Integer> catIDs = pLevel.getEntitiesOfClass(
                    WCatEntity.class,
                    pPlayer.getBoundingBox().inflate(45),
                    cat -> cat.isAlive() && cat.getOwner() == pPlayer
            ).stream().map(Entity::getId).toList();

            if (pPlayer instanceof ServerPlayer sPlayer) {
                ModPackets.sendToPlayer(new OpenAncientStickScreenPacket(catIDs), sPlayer);
            }

//
//            float pitch = 0.9f;
//            PlayerClanData.Age morphAge = pPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
//                    .map(PlayerClanData::getMorphAge).orElse(PlayerClanData.Age.ADULT);
//            switch (morphAge) {
//                case KIT ->  pitch = 1.3f;
//                case APPRENTICE ->  pitch = 1.1f;
//                case ADULT -> pitch = 0.9f;
//            }
//
//            if (!(PlayerShape.getCurrentShape(pPlayer) instanceof Animal)) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
//
//            int catsAffected = 0;
//
//            ItemStack stack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
//
//
//            AABB box = pPlayer.getBoundingBox().inflate(45);
//            List<WCatEntity> cats = pLevel.getEntitiesOfClass(
//                    WCatEntity.class,
//                    box,
//                    cat -> cat.isAlive() && cat.getOwner() == pPlayer
//            );
//            if (cats.isEmpty() && !stack.getTag().getBoolean("dismissClanSwitchActive")) {
//                pLevel.playSound(null,pPlayer.blockPosition(), ModSounds.LEADER_CALL.get(),  SoundSource.PLAYERS, 0.8F, pitch);
//                pPlayer.displayClientMessage(Component.literal("There are no cats around.").withStyle(ChatFormatting.GRAY), true);
//                pPlayer.getCooldowns().addCooldown(this, 20 * 4);
//                return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
//            }
//
//            pPlayer.getCooldowns().addCooldown(this, 20 * 10);
//
//
//            if (pPlayer.isShiftKeyDown()) {
//                if (stack.getTag().getBoolean("dismissClanSwitchActive")) {
//                    pLevel.playSound(null,pPlayer.blockPosition(), SoundEvents.CAT_STRAY_AMBIENT,  SoundSource.PLAYERS, 1F, pitch);
//                    for (WCatEntity cat : cats) {
//                        if (cat.isTame() && cat.isOwnedBy(pPlayer)) {
//                            catsAffected++;
//
//                            cat.leaderCallingToFollowFlag = false;
//                            cat.leaderCallingToSitFlag = false;
//                            cat.mode = WCatEntity.CatMode.WANDER;
//                            cat.setOrderedToSit(false);
//                            cat.wanderCenter = cat.blockPosition();
//
//                        }
//                    }
//                    pPlayer.displayClientMessage(Component.literal(catsAffected + " cats dismissed.").withStyle(ChatFormatting.GRAY), true);
//
//                } else {
//                    pLevel.playSound(null,pPlayer.blockPosition(), ModSounds.LEADER_CALL.get(),  SoundSource.PLAYERS, 0.8F, pitch);
//                    for (WCatEntity cat : cats) {
//                        if (cat.isTame() && cat.isOwnedBy(pPlayer)) {
//                            catsAffected++;
//
//                            cat.leaderCallingToSitFlag = true;
//
//                            pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
//
//                        }
//                    }
//                    pPlayer.displayClientMessage(Component.literal(catsAffected + " cats called to sit.").withStyle(ChatFormatting.AQUA), true);
//
//                }
//
//            } else {
//                if (stack.getTag().getBoolean("dismissClanSwitchActive")) {
//                    pLevel.playSound(null,pPlayer.blockPosition(), SoundEvents.CAT_STRAY_AMBIENT,  SoundSource.PLAYERS, 1F, pitch);
//                    for (WCatEntity cat : cats) {
//                        if (cat.isTame() && cat.isOwnedBy(pPlayer)) {
//                            catsAffected++;
//
//                            cat.leaderCallingToFollowFlag = false;
//                            cat.leaderCallingToSitFlag = false;
//                            cat.mode = WCatEntity.CatMode.WANDER;
//                            cat.setOrderedToSit(false);
//                            cat.wanderCenter = cat.blockPosition();
//
//                        }
//                    }
//                    pPlayer.displayClientMessage(Component.literal(catsAffected + " cats dismissed.").withStyle(ChatFormatting.GRAY), true);
//
//                } else {
//                    pLevel.playSound(null,pPlayer.blockPosition(), ModSounds.LEADER_CALL.get(),  SoundSource.PLAYERS, 0.8F, pitch);
//                    for (WCatEntity cat : cats) {
//                        if (cat.isTame() && cat.isOwnedBy(pPlayer)) {
//                            catsAffected++;
//
//                            cat.leaderCallingToFollowFlag = true;
//
//                            pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
//
//                        }
//                    }
//                    pPlayer.displayClientMessage(Component.literal(catsAffected + " cats called to follow.").withStyle(ChatFormatting.AQUA), true);
//                }
//            }
//
//            if (stack.hasTag()) {
//                if (stack.getTag().getBoolean("dismissClanSwitchActive")) {
//                    stack.getTag().putBoolean("dismissClanSwitchActive", false);
//                } else {
//                    stack.getTag().putBoolean("dismissClanSwitchActive", true);
//
//                }
//            }
//
//
        }
        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

        boolean active = stack.hasTag()
                && stack.getTag().getBoolean("dismissClanSwitchActive");

        if (active) {
            tooltip.add(Component.literal("Right-click to command all or certain cats around").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Right-click to command all or certain cats around").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean("dismissClanSwitchActive");
    }


}
