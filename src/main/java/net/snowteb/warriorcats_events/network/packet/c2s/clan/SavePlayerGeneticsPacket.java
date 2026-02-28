package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class SavePlayerGeneticsPacket {

    private final WCGenetics genetics;
    private final WCGenetics.GeneticalVariants variants;
    private final boolean onGeneticalSkin;
    private final int defaultVariant;

    public SavePlayerGeneticsPacket(boolean geneticalSkin, WCGenetics genetics, WCGenetics.GeneticalVariants variants, int defaultVariant) {
        this.genetics = genetics;
        this.variants = variants;
        this.onGeneticalSkin = geneticalSkin;
        this.defaultVariant = defaultVariant;
    }

    public static SavePlayerGeneticsPacket decode(FriendlyByteBuf buf) {

        boolean geneticalSkin = buf.readBoolean();

        WCGenetics genetics = WCGenetics.decode(buf);
        WCGenetics.GeneticalVariants variants = WCGenetics.GeneticalVariants.decode(buf);

        int defaultVariant = buf.readInt();

        return new SavePlayerGeneticsPacket(geneticalSkin, genetics, variants, defaultVariant);
    }

    public static void encode(SavePlayerGeneticsPacket packet, FriendlyByteBuf buf) {

        buf.writeBoolean(packet.onGeneticalSkin);

        packet.genetics.encode(buf);
        packet.variants.encode(buf);

        buf.writeInt(packet.defaultVariant);
    }

    public static void handle(SavePlayerGeneticsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            PlayerShape.updateShapes(player, null);

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                cap.setFirstLoginHandled(true);
                cap.setPlayerGenetics(packet.genetics);
                cap.setPlayerGeneticalVariants(packet.variants.eyeColorLeft, packet.variants.eyeColorRight,
                        packet.variants.rufousingVariant, packet.variants.blueRufousingVariant,
                        packet.variants.orangeVar, packet.variants.whiteVar, packet.variants.tabbyVar,
                        packet.variants.albinoVar, packet.variants.leftEyeVar, packet.variants.rightEyeVar,
                        packet.variants.noise, packet.variants.size);
                cap.setOnGeneticalSkin(packet.onGeneticalSkin);
                cap.setVariantData(packet.defaultVariant);

                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
            });

            int shapeData = packet.defaultVariant;
            int shapeAge = 0;
            boolean isApprentice = false;
            PlayerClanData.Age age = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphAge).orElse(PlayerClanData.Age.ADULT);

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

        if (WCEServerConfig.SERVER.VISIBLE_MORPH_NAME.get()) cat.setCustomName(name);
        cat.setCustomNameVisible(WCEServerConfig.SERVER.VISIBLE_MORPH_NAME.get());

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

        return cat;
    }
}
