package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
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
                    .getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(cap -> cap.getVariantData())
                    .orElse(0);

//                WCatEntity cat = new WCatTypeProvider().create(ModEntities.WCAT.get(), player.level(), variantData);
                WCatEntity cat = create(ModEntities.WCAT.get(), player.level(), variantData, player);
                cat.setVariant(variantData);

                LivingEntity current = PlayerShape.getCurrentShape(player);

                if (!(current instanceof WCatEntity)) {
                    PlayerShape.updateShapes(player, cat);
                } else {
                    PlayerShape.updateShapes(player, null);
                }


        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    public WCatEntity create(EntityType<WCatEntity> type, Level level, int data, ServerPlayer player) {
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

        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            if (cap.isOnGeneticalSkin()) {
                cat.setGenetics(cap.getPlayerGenetics());
                WCGenetics.GeneticalVariants variants = cap.getPlayerGeneticalVariants();
                cat.setGeneticalVariants(variants.eyeColorLeft, variants.eyeColorRight, variants.rufousingVariant
                        ,variants.blueRufousingVariant, variants.orangeVar, variants.whiteVar, variants.tabbyVar
                        ,variants.albinoVar, variants.leftEyeVar, variants.rightEyeVar, variants.noise, variants.size, variants.silverVar);

                cat.setChimeraGenetics(cap.getPlayerChimeraGenetics());

                WCGenetics.GeneticalChimeraVariants variantsChimera = cap.getPlayerChimeraVariants();
                cat.setGeneticalVariantsChimera(variantsChimera.chimeraVariant, variantsChimera.rufousingVariant,
                        variantsChimera.blueRufousingVariant, variantsChimera.orangeVar, variantsChimera.whiteVar, variantsChimera.tabbyVar
                        , variantsChimera.albinoVar, variantsChimera.noise, variantsChimera.silverVar);

                cat.setOnGeneticalSkin(true);
                cat.setGender(1);
            } else {
                cat.setNonGeneticalValues(cap.getPlayerGenetics(), cap.getPlayerGeneticalVariants().size);
            }
        });

        cat.setPlayerBoundUuid(player.getUUID());

        return cat;
    }
}
