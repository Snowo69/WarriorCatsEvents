package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.SyncMorphStatsPacket;

public class ReqMorphStatsPacket implements CustomPacketPayload {

    public ReqMorphStatsPacket() {}

    public ReqMorphStatsPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(ReqMorphStatsPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = (ServerPlayer) ctx.player();

            int squirrelKilled = player.getStats().getValue(Stats.ENTITY_KILLED.get(ModEntities.SQUIRREL.get()));
            int mouseKilled = player.getStats().getValue(Stats.ENTITY_KILLED.get(ModEntities.MOUSE.get()));
            int pigeonKilled = player.getStats().getValue(Stats.ENTITY_KILLED.get(ModEntities.PIGEON.get()));
            int badgerKilled = player.getStats().getValue(Stats.ENTITY_KILLED.get(ModEntities.BADGER.get()));

            int mossColected = player.getStats().getValue(Stats.BLOCK_MINED.get(Blocks.MOSS_BLOCK));
            int feathersCollected = player.getStats().getValue(Stats.ITEM_PICKED_UP.get(Items.FEATHER));

            int timePlayed = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
            int timeSurvived = player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));

            SyncMorphStatsPacket packet = new SyncMorphStatsPacket(squirrelKilled, mouseKilled, pigeonKilled,
                    badgerKilled, mossColected, feathersCollected, timePlayed, timeSurvived);

            ModPackets.sendToPlayer(packet, player);
        });

    }

    public static final Type<ReqMorphStatsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "req_morph_stats"));

    public static final StreamCodec<FriendlyByteBuf, ReqMorphStatsPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) ->pkt.toBytes(buf),
                    buf -> new ReqMorphStatsPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
