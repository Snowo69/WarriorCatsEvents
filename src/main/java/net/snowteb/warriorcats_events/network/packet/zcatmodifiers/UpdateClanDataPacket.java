package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.event.ModEvents2;
import net.snowteb.warriorcats_events.integration.WCatTypeProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSSwitchShape;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class UpdateClanDataPacket {

    private final int data;

    public UpdateClanDataPacket(int data) {
        this.data = data;
    }

    public static UpdateClanDataPacket decode(FriendlyByteBuf buf) {
        int data = buf.readInt();
        return new UpdateClanDataPacket(data);
    }

    public static void encode(UpdateClanDataPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.data);
    }

    public static void handle(UpdateClanDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (packet.data == 0) {
                player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setMorphAge(PlayerClanData.Age.APPRENTICE);
                    cap.setMorphName(cap.getPrefix() + "paw");
                    ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
                });
            }
            if (packet.data == 1) {
                player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setMorphAge(PlayerClanData.Age.ADULT);
                    cap.setMorphName(cap.getPrefix() + cap.getSufix());
                    ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
                });
            }


            int variantData = player
                    .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(cap -> cap.getVariantData())
                    .orElse(0);



            ModEvents2.schedule(10, () -> {
                WCatEntity shape = new WCatTypeProvider().create(ModEntities.WCAT.get(), player.level(), variantData, player);
                PlayerShape.updateShapes(player, shape);
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.ENCHANT,
                        player.getX(), player.getY() + 1.5, player.getZ(),
                        400, 0f, 0f, 0f, 4.0f);
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        player.getX(), player.getY() + 0.5, player.getZ(),
                        10, 0.4f, 0.4f, 0.4f, 0.3f);

                        player.level().playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7f, 1.2f);
                String playerMorphName = player
                        .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(cap -> cap.getMorphName())
                        .orElse("<player>");
                String playerMorphPrefix = player
                        .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(cap -> cap.getPrefix())
                        .orElse("<player>");
                String playerMorphClan = player
                        .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(cap -> cap.getClanName())
                        .orElse("<clan>");
                Boolean usesSufix = player
                        .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(cap -> cap.isUseSufixes())
                        .orElse(false);

                PlayerClanData.Age playerMorphAge = player
                        .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(cap -> cap.getMorphAge())
                        .orElse(PlayerClanData.Age.ADULT);

                String lastSufix = "";
                switch (playerMorphAge) {
                    case APPRENTICE -> lastSufix = "kit";
                    case ADULT ->  lastSufix = "paw";
                }

                if (usesSufix) {
                    player.getServer().getPlayerList().broadcastSystemMessage(
                            Component.empty()
                                    .append(Component.literal(playerMorphPrefix + lastSufix).withStyle(ChatFormatting.GOLD))
                                    .append(" from ")
                                    .append(Component.literal(playerMorphClan).withStyle(ChatFormatting.AQUA))
                                    .append(" has grown and shall now be known as ")
                                    .append(Component.literal(playerMorphName).withStyle(ChatFormatting.GOLD))
                                    .append("!")

                            , false
                    );
                } else {
                    player.getServer().getPlayerList().broadcastSystemMessage(
                            Component.empty()
                                    .append(Component.literal(playerMorphName).withStyle(ChatFormatting.GOLD))
                                    .append(" from ")
                                    .append(Component.literal(playerMorphClan).withStyle(ChatFormatting.AQUA))
                                    .append(" has grown!")
                            , false
                    );
                }

                player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    if (cap.getMateUUID() != null) {
                        if (!cap.getMateUUID().equals(WCatEntity.emptyUUID)) {
                            Entity entity = ((ServerLevel) player.level()).getEntity(cap.getMateUUID());
                            if (entity instanceof WCatEntity cat) {
                                cat.setMate(Component.literal(cap.getMorphName()));
                            }
                        }
                    }
                });

            });



        });

        ctx.get().setPacketHandled(true);
    }
}
