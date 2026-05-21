package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.diseases.DiseaseManager;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class CtSSwitchShape {

    public CtSSwitchShape() {

    }

    public CtSSwitchShape(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            int variantData = player
                    .getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(cap -> cap.getVariantData())
                    .orElse(0);

                WCatEntity cat = create(ModEntities.WCAT.get(), player.level(), variantData, player);
                cat.setVariant(variantData);

                LivingEntity current = PlayerShape.getCurrentShape(player);

                if (!(current instanceof WCatEntity)) {
                    PlayerShape.updateShapes(player, cat);
                    player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(skillProvider -> {
                        PlayerSkill.reviveAttributes(player, skillProvider);
                    });
                } else {
                    PlayerShape.updateShapes(player, null);
                    player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(skillProvider -> {
                        PlayerSkill.removeAttributes(player);
                    });
                }

                DiseaseManager.refreshData(player);


        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    public WCatEntity create(EntityType<WCatEntity> type, Level level, int data, ServerPlayer player) {
        WCatEntity cat = new WCatEntity(type, level);

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
                        , variantsChimera.albinoVar, variantsChimera.noise, variantsChimera.silverVar);

                cat.setOnGeneticalSkin(true);
                cat.setGender(1);

                cat.setIdlePose(cap.getIdlePose());

            } else {
                cat.setNonGeneticalValues(cap.getPlayerGenetics(), cap.getPlayerGeneticalVariants().size);
            }
        });

        cat.setPlayerBoundUuid(player.getUUID());

        return cat;
    }
}
