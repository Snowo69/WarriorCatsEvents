package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.attachments.WCEPlayerData;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.attachments.PlayerSkill;
import tocraft.walkers.api.PlayerShape;

import java.util.UUID;

public class SaveClanDataPacket implements CustomPacketPayload {

    private final WCEPlayerData data;

    public SaveClanDataPacket(WCEPlayerData data) {
        this.data = data;
    }

    public static SaveClanDataPacket decode(FriendlyByteBuf buf) {
        WCEPlayerData data = new WCEPlayerData();
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

    public static void handle(SaveClanDataPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            String oldMorphName = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphName();

            UUID currentClanUUID = player.getData(ModAttachments.PLAYER_WCE_DATA).getCurrentClanUUID();

            UUID currentMateUUID = player.getData(ModAttachments.PLAYER_WCE_DATA).getMateUUID();
            Component currentMateComponent  = player.getData(ModAttachments.PLAYER_WCE_DATA).getMateName();

            String currentBio  = player.getData(ModAttachments.PLAYER_WCE_DATA).getCharacterBio();

            String currentGender  = player.getData(ModAttachments.PLAYER_WCE_DATA).getGenderText();

            WCGenetics.GeneticalVariants currentGeneticVariants = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerGeneticalVariants();

            WCGenetics currentGenetics = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerGenetics();

            WCGenetics currentChimeraGens = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerChimeraGenetics();

            WCGenetics.GeneticalChimeraVariants currentChimeraVariants = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerChimeraVariants();

            boolean onGeneticalSkn = player.getData(ModAttachments.PLAYER_WCE_DATA).isOnGeneticalSkin();

            int morphPose = player.getData(ModAttachments.PLAYER_WCE_DATA).getIdlePose();

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
                cap.copyFrom(packet.data);
                cap.setCurrentClanUUID(currentClanUUID);
                cap.setMateUUID(currentMateUUID);
                cap.setMateName(currentMateComponent);
                cap.setCharacterBio(currentBio);
                cap.setGenderText(currentGender);
                cap.setIdlePose(morphPose);

                cap.setPlayerGenetics(currentGenetics);
                cap.setPlayerGeneticalVariants(currentGeneticVariants);
                cap.setPlayerChimeraGenetics(currentChimeraGens);
                cap.setPlayerChimeraVariants(currentChimeraVariants);

                cap.setOnGeneticalSkin(onGeneticalSkn);
            });


            int shapeData = packet.data.getVariantData();
            int shapeAge = 0;
            boolean isApprentice = false;
            WCEPlayerData.Age age = packet.data.getMorphAge();

            if (age == WCEPlayerData.Age.KIT) {
                shapeAge = -1000;
            } else if (age == WCEPlayerData.Age.APPRENTICE) {
                shapeAge = -500;
                isApprentice = true;

            } else if (age == WCEPlayerData.Age.ADULT){
                shapeAge = 0;
            }


            WCatEntity shape = createShape(ModEntities.WCAT.get(), player.level(), shapeData, player);

            shape.setAppScale(isApprentice);
            shape.setAge(shapeAge);

            PlayerShape.updateShapes(player, shape);

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_SKILL, cap -> {
                PlayerSkill.reviveAttributes(player, cap);
            });

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
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

            ClanData data = ClanData.get(player.serverLevel().getServer().overworld());

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
                data.playerMorphNames.put(player.getUUID(), cap.getMorphName());

                WCGenetics.PackedGeneticData morphData =
                        new WCGenetics.PackedGeneticData(cap.getPlayerGenetics(),
                                cap.getPlayerGeneticalVariants(),
                                cap.getPlayerChimeraGenetics(),
                                cap.getPlayerChimeraVariants(),
                                cap.isOnGeneticalSkin(), cap.getVariantData());

                data.playerMorphData.put(player.getUUID(), morphData);

                ClanData.Clan clan = data.getClan(cap.getCurrentClanUUID());
                if (clan != null) {

                    Component message = Component.empty()
                            .append(Component.literal(oldMorphName).withStyle(ChatFormatting.AQUA))
                            .append(" has updated their profile. ")
                            .append(" New name: ")
                            .append(Component.literal(cap.getMorphName()).withStyle(ChatFormatting.AQUA));

                    data.registerLog(player.serverLevel().getServer().overworld(), clan.clanUUID, message);

                    if (clan.members.get(player.getUUID()) == ClanData.ClanPlayerRank.LEADER) {
                        clan.leaderName = cap.getMorphName();
                    }

                    cap.setClanName(clan.name);
                }

                data.setDirty();
            });


        });

    }

    private static WCatEntity createShape(EntityType<WCatEntity> type, Level level, int data, ServerPlayer player) {
        WCatEntity cat = new WCatEntity(type, level);

        String shapeNameString = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphName();

        WCEPlayerData.Age shapeAge = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphAge();

        int genderValue = player.getData(ModAttachments.PLAYER_WCE_DATA).getGenderData();

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

        CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
            if (cap.isOnGeneticalSkin()) {
                cat.setGenetics(cap.getPlayerGenetics());
                WCGenetics.GeneticalVariants variants = cap.getPlayerGeneticalVariants();
                cat.setGeneticalVariants(variants.eyeColorLeft, variants.eyeColorRight, variants.rufousingVariant
                        ,variants.blueRufousingVariant, variants.orangeVar, variants.whiteVar, variants.tabbyVar
                        ,variants.albinoVar, variants.leftEyeVar, variants.rightEyeVar, variants.noise,
                        variants.size, variants.silverVar, variants.scars);
                cat.setOnGeneticalSkin(true);
                cat.setChimeraGenetics(cap.getPlayerChimeraGenetics());
                WCGenetics.GeneticalChimeraVariants variantsChimera = cap.getPlayerChimeraVariants();
                cat.setGeneticalVariantsChimera(variantsChimera.chimeraVariant, variantsChimera.rufousingVariant,
                        variantsChimera.blueRufousingVariant, variantsChimera.orangeVar, variantsChimera.whiteVar, variantsChimera.tabbyVar
                        , variantsChimera.albinoVar, variantsChimera.noise, variantsChimera.silverVar);
                cat.setGender(1);
                cat.setIdlePose(cap.getIdlePose());
            }
        });

        cat.setPlayerBoundUuid(player.getUUID());

        return cat;
    }

    public static final Type<SaveClanDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "save_clan_data"));

    public static final StreamCodec<FriendlyByteBuf, SaveClanDataPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
