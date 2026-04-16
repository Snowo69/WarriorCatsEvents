package net.snowteb.warriorcats_events.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.entity.client.WCModel;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
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

        player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            cap.setVariantData(data);
        });

        if (player instanceof ServerPlayer serverPlayer) {
            ClanData clanData = ClanData.get(serverPlayer.serverLevel().getServer().overworld());
            clanData.playerMorphData.put(player.getUUID(), data);
            clanData.setDirty();
        }

        String shapeNameString = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName)
                .orElse("undefined");

        WCEPlayerData.Age shapeAge = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphAge)
                .orElse(WCEPlayerData.Age.ADULT);

        int genderValue = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getGenderData)
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

        if (shapeAge == WCEPlayerData.Age.KIT) {
            age = -1000;
            isBaby = true;
            isAppScale = false;
        } else if (shapeAge == WCEPlayerData.Age.APPRENTICE) {
            age = -500;
            isAppScale = true;
            isBaby = true;
        } else if (shapeAge == WCEPlayerData.Age.ADULT) {
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

        player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            if (cap.isOnGeneticalSkin()) {
                cat.setGenetics(cap.getPlayerGenetics());
                WCGenetics.GeneticalVariants variants = cap.getPlayerGeneticalVariants();
                cat.setGeneticalVariants(variants.eyeColorLeft, variants.eyeColorRight, variants.rufousingVariant
                ,variants.blueRufousingVariant, variants.orangeVar, variants.whiteVar, variants.tabbyVar
                ,variants.albinoVar, variants.leftEyeVar, variants.rightEyeVar, variants.noise,
                        variants.size, variants.silverVar, variants.scars);

                cat.setChimeraGenetics(cap.getPlayerChimeraGenetics());

                WCGenetics.GeneticalChimeraVariants variantsChimera = cap.getPlayerChimeraVariants();
                cat.setGeneticalVariantsChimera(variantsChimera.chimeraVariant, variantsChimera.rufousingVariant,
                        variantsChimera.blueRufousingVariant, variantsChimera.orangeVar, variantsChimera.whiteVar, variantsChimera.tabbyVar
                        ,variantsChimera.albinoVar, variantsChimera.noise, variantsChimera.silverVar);

                cat.setOnGeneticalSkin(true);
                cat.setGender(1);

                cat.setIdlePose(cap.getIdlePose());
            } else {
                cat.setNonGeneticalValues(cap.getPlayerGenetics(), cap.getPlayerGeneticalVariants().size);
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
