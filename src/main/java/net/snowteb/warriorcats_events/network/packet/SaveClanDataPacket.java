package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WCEConfig;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.integration.WCatTypeProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSSwitchShape;
import tocraft.walkers.api.PlayerShape;

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

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                cap.copyFrom(packet.data);
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
            int genderValue = packet.data.getGenderData();

            String genderS;
            if (genderValue == 0) {
                genderS = " ♂";
            } else {
                genderS = " ♀";
            }

            String nameToString = packet.data.getMorphName();
            Component shapeName = Component.literal(nameToString + genderS);


//            WCatEntity shape = new WCatTypeProvider().create(ModEntities.WCAT.get(), player.level(), shapeData);
            WCatEntity shape = createShape(ModEntities.WCAT.get(), player.level(), shapeData, player);

            shape.setAppScale(isApprentice);
            shape.setAge(shapeAge);
//            shape.setCustomName(shapeName);
//            shape.setCustomNameVisible(true);

            PlayerShape.updateShapes(player, shape);

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
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
        } else {
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

        if (WCEConfig.COMMON.VISIBLE_MORPH_NAME.get()) cat.setCustomName(name);
        cat.setCustomNameVisible(WCEConfig.COMMON.VISIBLE_MORPH_NAME.get());

        cat.setAge(age);
        cat.setBaby(isBaby);
        cat.setAppScale(isAppScale);

        return cat;
    }
}
