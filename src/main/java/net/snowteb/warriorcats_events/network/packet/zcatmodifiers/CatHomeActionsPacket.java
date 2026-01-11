package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;

import java.util.function.Supplier;

public class CatHomeActionsPacket {
    private final int entityId;
    private final int key;

    public CatHomeActionsPacket(int entityId, int key) {
        this.entityId = entityId;
        this.key = key;
    }

    public static void encode(CatHomeActionsPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.key);
    }

    public static CatHomeActionsPacket decode(FriendlyByteBuf buf) {
        return new CatHomeActionsPacket(
                buf.readInt(),
                buf.readInt()
        );
    }

    public static void handle(CatHomeActionsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            Entity e = level.getEntity(msg.entityId);
            int actionID = msg.key;

            String message;


            if (e instanceof WCatEntity cat) {
                String catName = cat.hasCustomName() ? "<" + cat.getCustomName().getString() + "> " : "<???> ";

                if (actionID == 0) {
                    BlockPos homePos = cat.getHomePosition();
                    if (homePos == null || homePos.equals(BlockPos.ZERO)) {
                        player.sendSystemMessage(Component.literal("This cat doesn't have a home set.").withStyle(ChatFormatting.RED));

                        cat.mode = cat.lastMode;
                        if (cat.lastMode != WCatEntity.CatMode.SIT) {
                            cat.setInSittingPose(false);
                        }
                        cat.lastMode = cat.mode;

                        return;
                    }

                    if (cat.distanceToSqr(homePos.getX(), homePos.getY(), homePos.getZ()) > 159000) {
                        if (cat.getRank() != WCatEntity.Rank.KIT) {
                            message = switch (cat.getPersonality()) {
                                case INDEPENDENT -> "U-uh... Where are we again?";
                                case SHY -> "I-I don't want to get lost...";
                                case FRIENDLY -> "Sorry, I think we are too far.";
                                case HUMBLE -> "Sorry, I don't think I can make it.";
                                case GRUMPY ->  "Don't be a mouse-brained, have you seen where we are?";
                                case RECKLESS -> "You must be joking.";
                                case CALM -> "Sorry, but we are too far.";
                                case AMBITIOUS -> "I think we are too far.";
                                case CAUTIOUS -> "We are too far, I don't want to get lost.";
                                case NONE -> "";
                            };
                            player.sendSystemMessage(Component.literal(catName + message));
                        }

                        cat.mode = cat.lastMode;
                        if (cat.lastMode != WCatEntity.CatMode.SIT) {
                            cat.setInSittingPose(false);
                        }
                        cat.lastMode = cat.mode;

                        player.sendSystemMessage(Component.literal("Home is too far.").withStyle(ChatFormatting.GRAY));
                        return;
                    }
                    if (cat.getRank() != WCatEntity.Rank.KIT) {
                        message = switch (cat.getPersonality()) {
                            case INDEPENDENT -> "I will find my way home, see you later.";
                            case SHY -> "O-okay, see you later then";
                            case FRIENDLY -> "Sure! Good luck on your journey!";
                            case HUMBLE -> "Sure, I will go back home.";
                            case GRUMPY ->  "Huh, if you say so.";
                            case RECKLESS -> "Don't get lost on your way back!";
                            case CALM -> "Sure, good luck.";
                            case AMBITIOUS -> "Pft. Fine.";
                            case CAUTIOUS -> "I will be on guard when I get back.";
                            case NONE -> "";
                        };
                        player.sendSystemMessage(Component.literal(catName + message));
                    }

                    cat.mode = WCatEntity.CatMode.WANDER;
                    cat.setInSittingPose(false);
                    cat.setOrderedToSit(false);
                    cat.returnHomeFlag = true;

                } else if (actionID == 1) {

                    if (cat.getRank() != WCatEntity.Rank.KIT) {
                        message = switch (cat.getPersonality()) {
                            case INDEPENDENT -> "I can remember on my own, thanks.";
                            case SHY -> "O-okay! I will remember.";
                            case FRIENDLY -> "Sure! This will be my home now.";
                            case HUMBLE -> "I couldn't ask for a better place.";
                            case GRUMPY ->  "This place? Huh, alright.";
                            case RECKLESS -> "This will be my place then!";
                            case CALM -> "Sure, I will remember this place.";
                            case AMBITIOUS -> "I hope we expand this place soon!";
                            case CAUTIOUS -> "If you think it's safe, then it's alright.";
                            case NONE -> "";
                        };
                        player.sendSystemMessage(Component.literal(catName + message));
                    }

                    cat.mode = cat.lastMode;
                    if (cat.lastMode != WCatEntity.CatMode.SIT) {
                        cat.setInSittingPose(false);
                    }
                    cat.lastMode = cat.mode;

                    player.sendSystemMessage(Component.literal("Home position set for " +  catName).withStyle(ChatFormatting.GREEN));
                    cat.setHomePosition(cat.blockPosition());

                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
