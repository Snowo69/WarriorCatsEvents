package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;

public class S2CClanPackagePacket implements CustomPacketPayload {

    public List<ClientClans.Entry> clans;
    public int batchIndex;
    public boolean isLast;

    public boolean seeingMyClan;
    public boolean territoryMap;

    public S2CClanPackagePacket(List<ClientClans.Entry> clans, int batchIndex, boolean isLast,
                                boolean seeingMyClan, boolean territoryMap) {
        this.clans = clans;
        this.batchIndex = batchIndex;
        this.isLast = isLast;
        this.seeingMyClan = seeingMyClan;
        this.territoryMap = territoryMap;
    }

    public static void encode(S2CClanPackagePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.batchIndex);
        buf.writeBoolean(msg.isLast);
        buf.writeBoolean(msg.seeingMyClan);
        buf.writeBoolean(msg.territoryMap);

        buf.writeInt(msg.clans.size());
        for (ClientClans.Entry info : msg.clans) {
            info.writeToBuf(buf);
        }
    }

    public static S2CClanPackagePacket decode(FriendlyByteBuf buf) {
        int batchIndex = buf.readInt();
        boolean isLast = buf.readBoolean();
        boolean seeingMyClan = buf.readBoolean();
        boolean territoryMap = buf.readBoolean();

        int size = buf.readInt();
        List<ClientClans.Entry> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ClientClans.Entry entry = ClientClans.Entry.readFromBuf(buf);
            list.add(entry);
        }

        return new S2CClanPackagePacket(list, batchIndex, isLast, seeingMyClan, territoryMap);
    }

    public static void handle(S2CClanPackagePacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            if (msg.batchIndex == 0) {
                ClientClans.startBatch();
            }

            ClientClans.appendClans(msg.clans, msg.isLast);

            if (msg.isLast) {
                WCEClient.playLocalSound(ModSounds.MENU_OPEN.get(), SoundSource.NEUTRAL, 0.8f, 1.3f);
                ClientPacketHandles.openClanListScreen(msg.seeingMyClan, msg.territoryMap);
            }
        });
    }

    public static final Type<S2CClanPackagePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "clan_package_packet"));

    public static final StreamCodec<FriendlyByteBuf, S2CClanPackagePacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
