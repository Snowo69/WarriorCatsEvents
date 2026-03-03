package net.snowteb.warriorcats_events.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.entity.client.WCModel;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.zconfig.WCEConfig;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.variant.TypeProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;

/**
 * Provides all the available variants of the wild cat.
 */
public class WCatTypeProvider extends TypeProvider<WCatEntity> {

    @Override
    public int getVariantData(WCatEntity wCatEntity) {
        return wCatEntity.getVariant();
    }

    @Override
    public WCatEntity create(EntityType<WCatEntity> type, Level world, int data) {
        return null;
    }

    @Override
    public WCatEntity create(EntityType<WCatEntity> type, Level level, int data, Player player) {
        WCatEntity cat = new WCatEntity(type, level);

        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            cap.setVariantData(data);
        });

        if (player instanceof ServerPlayer serverPlayer) {
            ClanData clanData = ClanData.get(serverPlayer.serverLevel());
            clanData.playerMorphData.put(player.getUUID(), data);
            clanData.setDirty();
        }

        String shapeNameString = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName)
                .orElse("undefined");

        PlayerClanData.Age shapeAge = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphAge)
                .orElse(PlayerClanData.Age.ADULT);

        int genderValue = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getGenderData)
                .orElse(0);

        String genderS = "";
        if (genderValue == 0) {
            genderS = " ♂";
        } else if (genderValue == 1) {
            genderS = " ♀";
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

        cat.setPlayerBoundUuid(player.getUUID());

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

        return cat;
    }

    @Override
    public int getFallbackData() {
        return 0;
    }

    @Override
    public int getRange() {
        return WCModel.TEXTURES.length - 1;
    }

    @Override
    public Component modifyText(WCatEntity wCatEntity, MutableComponent mutableComponent) {
        return mutableComponent.append("Variant " + wCatEntity.getVariant());
    }

    
}
