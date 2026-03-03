package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.PlayerShape;

import java.util.UUID;
import java.util.function.Supplier;

public class SaveClanDataPacket {

    private final PlayerClanData data;

    public SaveClanDataPacket(PlayerClanData data) {
        this.data = data;
    }

    public static SaveClanDataPacket decode(FriendlyByteBuf buf) {
        PlayerClanData data = new PlayerClanData();
        CompoundTag tag = buf.readNbt();
        if (tag != null) {
            data.loadNBT(tag);
        }
        return new SaveClanDataPacket(data);
    }

    public static void encode(SaveClanDataPacket packet, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        packet.data.saveNBT(tag);
        buf.writeNbt(tag);
    }

    public static void handle(SaveClanDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            String oldMorphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMorphName).orElse(player.getName().getString());

            UUID currentClanUUID = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

            WCGenetics.GeneticalVariants currentGeneticVariants = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getPlayerGeneticalVariants).orElse(new WCGenetics.GeneticalVariants());

            WCGenetics currentGenetics = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getPlayerGenetics).orElse(new WCGenetics());

            boolean onGeneticalSkn = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::isOnGeneticalSkin).orElse(false);

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                cap.copyFrom(packet.data);
                cap.setCurrentClanUUID(currentClanUUID);

                cap.setPlayerGenetics(currentGenetics);
                cap.setPlayerGeneticalVariants(currentGeneticVariants);
                cap.setOnGeneticalSkin(onGeneticalSkn);
            });


            int shapeData = packet.data.getVariantData();
            int shapeAge = 0;
            boolean isApprentice = false;
            PlayerClanData.Age age = packet.data.getMorphAge();

            if (age == PlayerClanData.Age.KIT) {
                shapeAge = -1000;
            } else if (age == PlayerClanData.Age.APPRENTICE) {
                shapeAge = -500;
                isApprentice = true;

            } else if (age == PlayerClanData.Age.ADULT){
                shapeAge = 0;
            }


            WCatEntity shape = createShape(ModEntities.WCAT.get(), player.level(), shapeData, player);

            shape.setAppScale(isApprentice);
            shape.setAge(shapeAge);

            PlayerShape.updateShapes(player, shape);

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);

                if (cap.getMateUUID() != null) {
                    if (!cap.getMateUUID().equals(WCatEntity.emptyUUID)) {
                        Entity entity = ((ServerLevel) player.level()).getEntity(cap.getMateUUID());
                        if (entity instanceof WCatEntity cat) {
                            cat.setMate(Component.literal(cap.getMorphName()));
                        }
                    }
                }
            });

            ClanData data = ClanData.get(player.serverLevel());

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                data.playerMorphNames.put(player.getUUID(), cap.getMorphName());
                data.playerMorphData.put(player.getUUID(), cap.getVariantData());

                ClanData.Clan clan = data.getClan(cap.getCurrentClanUUID());
                if (clan != null) {

                    Component message = Component.empty()
                                    .append(Component.literal(oldMorphName).withStyle(ChatFormatting.AQUA))
                                            .append(" has updated their profile. ")
                                                    .append(" New name: ")
                                                            .append(Component.literal(cap.getMorphName()).withStyle(ChatFormatting.AQUA));

                    data.registerLog(player.serverLevel(), clan.clanUUID, message);

                    if (clan.members.get(player.getUUID()) == ClanData.ClanPlayerRank.LEADER) {
                        clan.leaderName = cap.getMorphName();
                    }

                    cap.setClanName(clan.name);
                }

                data.setDirty();
            });


        });

        ctx.get().setPacketHandled(true);
    }

    private static WCatEntity createShape(EntityType<WCatEntity> type, Level level, int data, ServerPlayer player) {
        WCatEntity cat = new WCatEntity(type, level);

        String shapeNameString = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName)
                .orElse("undefined");

        PlayerClanData.Age shapeAge = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphAge)
                .orElse(PlayerClanData.Age.ADULT);

        int genderValue = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getGenderData)
                .orElse(0);

        String genderS;
        if (genderValue == 0) {
            genderS = " ♂";
        } else if (genderValue == 1){
            genderS = " ♀";
        } else {
            genderS = "";
        }

        int age = 0;
        boolean isAppScale = false;
        boolean isBaby = false;

        if (shapeAge == PlayerClanData.Age.KIT) {
            age = -1000;
            isBaby = true;
            isAppScale = false;
        } else if (shapeAge == PlayerClanData.Age.APPRENTICE) {
            age = -500;
            isAppScale = true;
            isBaby = true;
        } else if (shapeAge == PlayerClanData.Age.ADULT) {
            age = 0;
            isAppScale = false;
            isBaby = false;
        }

        Component name = Component.literal(shapeNameString + genderS);

        cat.setVariant(data);

        cat.setCustomName(name);
        cat.setCustomNameVisible(true);
        cat.setShowMorphName(true);


        cat.setAge(age);
        cat.setBaby(isBaby);
        cat.setAppScale(isAppScale);

        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            if (cap.isOnGeneticalSkin()) {
                cat.setGenetics(cap.getPlayerGenetics());
                WCGenetics.GeneticalVariants variants = cap.getPlayerGeneticalVariants();
                cat.setGeneticalVariants(variants.eyeColorLeft, variants.eyeColorRight, variants.rufousingVariant
                        ,variants.blueRufousingVariant, variants.orangeVar, variants.whiteVar, variants.tabbyVar
                        ,variants.albinoVar, variants.leftEyeVar, variants.rightEyeVar, variants.noise, variants.size);
                cat.setOnGeneticalSkin(true);
                cat.setGender(1);
            }
        });

        cat.setPlayerBoundUuid(player.getUUID());

        return cat;
    }
}
