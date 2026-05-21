package net.snowteb.warriorcats_events.network.packet.c2s.cats;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.util.GeneticsForVariant;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.UUID;
import java.util.function.Supplier;

import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.Rank.KIT;

public class CtSNameKitPacket {
    private final String kitPrefix;
    private final int kitID;

    public CtSNameKitPacket(String kitPrefix, int kitID) {
        this.kitPrefix = kitPrefix;
        this.kitID = kitID;
    }

    public CtSNameKitPacket(FriendlyByteBuf buf) {
        this.kitPrefix = buf.readUtf(64);
        this.kitID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.kitPrefix);
        buf.writeInt(this.kitID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            Player player = ctx.getSender();
            String prefix = this.kitPrefix;

            if (player instanceof ServerPlayer sPlayer) {
                ServerLevel level = ((ServerLevel) sPlayer.level());

                WCatEntity kit = (WCatEntity) level.getEntity(this.kitID);

                if (kit != null) {

                    String finalName;

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

                    kit.setNameColor(kit.getRank());

                }


            }
        });

        ctx.setPacketHandled(true);
    }
}

