package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenPlayerCatDataScreenPacket {

    public final WCEPlayerData.PackedData playerData;
    public final UUID targetUUID;
    public final int myKitCooldown;

    public OpenPlayerCatDataScreenPacket(WCEPlayerData.PackedData data, UUID targetUUID, int myKitCooldown) {
        this.playerData = data;
        this.targetUUID = targetUUID;
        this.myKitCooldown = myKitCooldown;
    }

    public OpenPlayerCatDataScreenPacket(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        String clanName = buf.readUtf();
        int gender = buf.readInt();
        String mateName = buf.readUtf();
        WCEPlayerData.Age age = buf.readEnum(WCEPlayerData.Age.class);
        int kitCooldown = buf.readInt();
        UUID targetUUID = buf.readUUID();
        int myKitCooldown = buf.readInt();

        this.playerData = new WCEPlayerData.PackedData(name, clanName, gender, mateName, age, kitCooldown);
        this.targetUUID = targetUUID;
        this.myKitCooldown = myKitCooldown;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(playerData.name);
        buf.writeUtf(playerData.clanName);
        buf.writeInt(playerData.gender);
        buf.writeUtf(playerData.mateName);
        buf.writeEnum(playerData.age);
        buf.writeInt(playerData.kitCooldown);
        buf.writeUUID(targetUUID);
        buf.writeInt(myKitCooldown);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {

            ClientPacketHandles.openPlayerCatScreen(playerData, targetUUID, myKitCooldown);
        });

        ctx.setPacketHandled(true);
    }
}
