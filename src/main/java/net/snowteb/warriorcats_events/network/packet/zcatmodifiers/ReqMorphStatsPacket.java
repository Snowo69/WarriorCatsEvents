package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.network.ModPackets;
import tocraft.walkers.network.NetworkHandler;

import java.util.function.Supplier;

public class ReqMorphStatsPacket {

    public ReqMorphStatsPacket() {}

    public ReqMorphStatsPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(ReqMorphStatsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

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

        ctx.get().setPacketHandled(true);
    }

}
