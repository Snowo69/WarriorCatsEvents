package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.others.OpenPlayerCatDataScreenPacket;
import net.snowteb.warriorcats_events.util.ItemWithToolTip;

import java.util.UUID;

public class WhiskersItem extends ItemWithToolTip {

    public WhiskersItem(Properties properties, String tooltipKey) {
        super(properties, tooltipKey);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        if (pInteractionTarget instanceof Player target) {
            if (!pPlayer.level().isClientSide()) {

                String name = target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphName).orElse("Unnamed");
                String clanName = target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getClanName).orElse("No clan");
                int gender = target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getGenderData).orElse(2);

                String mateName = (target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMateName).orElse(Component.literal("No mate"))).getString();

                WCEPlayerData.Age age = target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphAge).orElse(WCEPlayerData.Age.ADULT);

                int targetKitCooldown = target.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getPlayerKitsCooldown).orElse(1);

                int myKitCooldown = pPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getPlayerKitsCooldown).orElse(1);

                WCEPlayerData.PackedData targetData =
                        new WCEPlayerData.PackedData(name, clanName, gender, mateName, age, targetKitCooldown);

                UUID targetUUID = target.getUUID();

                pPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(data -> {
                    ModPackets.sendToPlayer(new S2CSyncClanDataPacket(data), (ServerPlayer) pPlayer);
                });

                ModPackets.sendToPlayer(new OpenPlayerCatDataScreenPacket(targetData, targetUUID, myKitCooldown), (ServerPlayer) pPlayer);

            }

            return InteractionResult.SUCCESS;
        }


        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
