package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.MorphStatsClientData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.screen.clandata.MorphGrowthScreen;

import java.util.function.Supplier;

public class SyncMorphStatsPacket {

    private final int squirrelKilled;
    private final int mouseKilled;
    private final int pigeonKilled;
    private final int badgerKilled;

    private final int mossColected;
    private final int feathersCollected;

    private final int timePlayed;
    private final int timeSurvived;

    public SyncMorphStatsPacket(int squirrelKilled, int mouseKilled, int pigeonKilled,
                                int badgerKilled, int mossColected, int feathersCollected,
                                int timePlayed, int timeSurvived) {
        this.squirrelKilled = squirrelKilled;
        this.mouseKilled = mouseKilled;
        this.pigeonKilled = pigeonKilled;
        this.badgerKilled = badgerKilled;
        this.mossColected = mossColected;
        this.feathersCollected = feathersCollected;
        this.timePlayed = timePlayed;
        this.timeSurvived = timeSurvived;
    }

    public SyncMorphStatsPacket(FriendlyByteBuf buf) {
        this.squirrelKilled = buf.readInt();
        this.mouseKilled = buf.readInt();
        this.pigeonKilled = buf.readInt();
        this.badgerKilled = buf.readInt();
        this.mossColected = buf.readInt();
        this.feathersCollected = buf.readInt();
        this.timePlayed = buf.readInt();
        this.timeSurvived = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(squirrelKilled);
        buf.writeInt(mouseKilled);
        buf.writeInt(pigeonKilled);
        buf.writeInt(badgerKilled);
        buf.writeInt(mossColected);
        buf.writeInt(feathersCollected);
        buf.writeInt(timePlayed);
        buf.writeInt(timeSurvived);
    }

    public static void handle(SyncMorphStatsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            MorphStatsClientData.squirrelKilled = msg.squirrelKilled;
            MorphStatsClientData.mouseKilled = msg.mouseKilled;
            MorphStatsClientData.pigeonKilled = msg.pigeonKilled;
            MorphStatsClientData.badgerKilled = msg.badgerKilled;

            MorphStatsClientData.mossColected = msg.mossColected;
            MorphStatsClientData.feathersCollected = msg.feathersCollected;

            MorphStatsClientData.timePlayed = msg.timePlayed;
            MorphStatsClientData.timeSurvived = msg.timeSurvived;
        });

        ctx.get().setPacketHandled(true);
    }


}

