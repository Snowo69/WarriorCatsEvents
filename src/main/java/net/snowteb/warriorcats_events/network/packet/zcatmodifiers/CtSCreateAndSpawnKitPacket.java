package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WCEConfig;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.Rank.KIT;

public class CtSCreateAndSpawnKitPacket {
    private final String kitPrefix;
    private final int kitVariant;

    public CtSCreateAndSpawnKitPacket(String kitPrefix, int kitVariant) {
        this.kitPrefix = kitPrefix;
        this.kitVariant = kitVariant;
    }

    public CtSCreateAndSpawnKitPacket(FriendlyByteBuf buf) {
        this.kitPrefix = buf.readUtf(64);
        this.kitVariant = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.kitPrefix);
        buf.writeInt(this.kitVariant);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            String prefix = this.kitPrefix;
            int variant = this.kitVariant;

            if (player instanceof ServerPlayer sPlayer) {
                BlockPos clickedPos = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(PlayerClanData::getTempClickedPosData).orElse(sPlayer.blockPosition());

                String clan = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                        .map(PlayerClanData::getClanName).orElse("undefined");


                ServerLevel server = ((ServerLevel) sPlayer.level());

                WCatEntity kit = ModEntities.WCAT.get().create(server);


                if (kit != null) {
                    kit.setPos(clickedPos.getX(), clickedPos.getY(), clickedPos.getZ());

                    kit.finalizeSpawn(server, server.getCurrentDifficultyAt(clickedPos),
                            MobSpawnType.MOB_SUMMONED, null, null);

                    int minutes = WCEConfig.COMMON.KIT_GROWTH_MINUTES.get();
                    int growingTicks = minutes * 20 * 60;
                    kit.setAge(-growingTicks);

                    server.addFreshEntity(kit);

                    kit.setTame(true);
                    kit.tame(player);
                    kit.setOwnerUUID(sPlayer.getUUID());
                    kit.setRank(KIT);
                    kit.kitBorn = true;
                    String finalName = "";
                    kit.setVariant(variant);

                    kit.setGender(sPlayer.getRandom().nextInt(2));

                    if (!kit.hasCustomName()) {
                        String genderS;
                        if (kit.getGender() == 0) {
                            genderS = " ♂";
                        } else {
                            genderS = " ♀";
                        }

                        finalName = prefix + "kit" + genderS;
                        kit.setCustomName(Component.literal(finalName));
                        kit.setCustomNameVisible(true);

                        kit.setPrefix(Component.literal(kitPrefix));

                        sPlayer.sendSystemMessage(Component.literal(finalName).withStyle(ChatFormatting.GREEN)
                                .append(Component.literal(" has been born!").withStyle(ChatFormatting.WHITE))
                        );
                    }

                    kit.wanderCenter = sPlayer.blockPosition();
                    kit.applyBabyAttributes();
                    kit.setHealth(kit.getMaxHealth());
                    kit.setNameColor(KIT);

                    kit.assignRandomPersonality(kit.getRandom());
                    kit.setSpecificMood(WCatEntity.Mood.CALM);




                    kit.setHomePosition(clickedPos);
                    kit.setClan(Component.literal(clan));

                    UUID otherParentUUID = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMateUUID).orElse(null);

                    int morphGender = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getGenderData)
                            .orElse(0);

                    String morphName = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                            .map(PlayerClanData::getMorphName)
                            .orElse("Unknown");

                    Entity otherParent = null;

                    if (otherParentUUID != null) {
                        otherParent = server.getEntity(otherParentUUID);
                    }

                    if (otherParent instanceof WCatEntity) {

                        if (morphGender == 0) {
                            kit.setFatherUUID(sPlayer.getUUID());
                            kit.setMotherUUID(otherParent.getUUID());
                            kit.setMother(otherParent.hasCustomName() ? otherParent.getCustomName() : Component.literal("Unknown"));
                            kit.setFather(Component.literal(morphName));
                        } else {
                            kit.setFatherUUID(otherParent.getUUID());
                            kit.setMotherUUID(sPlayer.getUUID());
                            kit.setMother(Component.literal(morphName));
                            kit.setFather(otherParent.hasCustomName() ? otherParent.getCustomName() : Component.literal("Unknown"));
                        }
                    } else {
                        kit.setMotherUUID(sPlayer.getUUID());
                        kit.setMother(Component.literal(morphName));
                    }

                    kit.setForbiddenPlayer(sPlayer.getUUID());
                    kit.setForbiddenFromMatingPlayer(true);
                    kit.setForbiddingFutureGensFromMatingPlayer(true);
                    kit.setFriendshipLevel(sPlayer.getUUID(), 50);

                    if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.KIT_ITEM.get())) player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    server.sendParticles(ParticleTypes.HAPPY_VILLAGER, clickedPos.getX(), clickedPos.getY() + 0.5, clickedPos.getZ(), 15,0.3f,0.3f, 0.3f,0.2f);


                }


            }
        });

        ctx.setPacketHandled(true);
    }
}

