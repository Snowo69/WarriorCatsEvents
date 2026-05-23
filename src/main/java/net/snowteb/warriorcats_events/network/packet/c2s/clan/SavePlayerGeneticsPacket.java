package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.attachments.WCEPlayerData;
import net.snowteb.warriorcats_events.diseases.Diseaseable;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import tocraft.walkers.api.PlayerShape;

public class SavePlayerGeneticsPacket implements CustomPacketPayload {

    private final WCGenetics genetics;
    private final WCGenetics.GeneticalVariants variants;

    private final WCGenetics chimeraGenetics;
    private final WCGenetics.GeneticalChimeraVariants chimeraVariants;

    private final boolean onGeneticalSkin;
    private final int defaultVariant;

    public SavePlayerGeneticsPacket(boolean geneticalSkin, WCGenetics genetics, WCGenetics.GeneticalVariants variants,
                                    WCGenetics chimeraGens, WCGenetics.GeneticalChimeraVariants chimeraVariants, int defaultVariant) {
        this.genetics = genetics;
        this.variants = variants;
        this.chimeraGenetics = chimeraGens;
        this.chimeraVariants = chimeraVariants;
        this.onGeneticalSkin = geneticalSkin;
        this.defaultVariant = defaultVariant;
    }

    public static SavePlayerGeneticsPacket decode(FriendlyByteBuf buf) {

        boolean geneticalSkin = buf.readBoolean();

        WCGenetics genetics = WCGenetics.decode(buf);
        WCGenetics.GeneticalVariants variants = WCGenetics.GeneticalVariants.decode(buf);

        WCGenetics chimeraGens = WCGenetics.decode(buf);
        WCGenetics.GeneticalChimeraVariants chimeraVariants = WCGenetics.GeneticalChimeraVariants.decode(buf);

        int defaultVariant = buf.readInt();

        return new SavePlayerGeneticsPacket(geneticalSkin, genetics, variants, chimeraGens, chimeraVariants, defaultVariant);
    }

    public static void encode(SavePlayerGeneticsPacket packet, FriendlyByteBuf buf) {

        buf.writeBoolean(packet.onGeneticalSkin);

        packet.genetics.encode(buf);
        packet.variants.encode(buf);
        packet.chimeraGenetics.encode(buf);
        packet.chimeraVariants.encode(buf);

        buf.writeInt(packet.defaultVariant);
    }

    public static void handle(SavePlayerGeneticsPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            PlayerShape.updateShapes(player, null);

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
                cap.setFirstLoginHandled(true);
                cap.setPlayerGenetics(packet.genetics);

                cap.setPlayerGeneticalVariants(packet.variants);
//                cap.setPlayerGeneticalVariants(packet.variants.eyeColorLeft, packet.variants.eyeColorRight,
//                        packet.variants.rufousingVariant, packet.variants.blueRufousingVariant,
//                        packet.variants.orangeVar, packet.variants.whiteVar, packet.variants.tabbyVar,
//                        packet.variants.albinoVar, packet.variants.leftEyeVar, packet.variants.rightEyeVar,
//                        packet.variants.noise, packet.variants.size, packet.variants.silverVar, packet.variants.scars);
                cap.setOnGeneticalSkin(packet.onGeneticalSkin);
                cap.setVariantData(packet.defaultVariant);
                cap.setPlayerChimeraGenetics(packet.chimeraGenetics);
                cap.setPlayerChimeraVariants(packet.chimeraVariants);

                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
            });

            int shapeData = packet.defaultVariant;

            WCatEntity shape = createShape(ModEntities.WCAT.get(), player.level(), shapeData, player);

            if (!PlayerShape.updateShapes(player, shape)) {
                player.sendSystemMessage(Component.literal("Couldn't update your morph"));
            }

            if (player instanceof Diseaseable<?> diseaseable) diseaseable.onChange();


        });

    }

    private static WCatEntity createShape(EntityType<WCatEntity> type, Level level, int defaultVariant, ServerPlayer player) {
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

        cat.setVariant(defaultVariant);

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


            ClanData data = ClanData.get(((ServerLevel) level).getServer().overworld());
            data.playerMorphData.put(player.getUUID(), new WCGenetics.PackedGeneticData(cap.getPlayerGenetics(),
                    cap.getPlayerGeneticalVariants(), cap.getPlayerChimeraGenetics(),
                    cap.getPlayerChimeraVariants(), cap.isOnGeneticalSkin(), cap.getVariantData()));
            data.setDirty();

            cat.setIdlePose(cap.getIdlePose());
        });

        cat.setPlayerBoundUuid(player.getUUID());


        return cat;
    }

    public static final Type<SavePlayerGeneticsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "save_player_genetics"));

    public static final StreamCodec<FriendlyByteBuf, SavePlayerGeneticsPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
