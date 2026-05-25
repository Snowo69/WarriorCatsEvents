package net.snowteb.warriorcats_events.network.packet.s2c.skilltree;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class SyncSkillDataPacket implements CustomPacketPayload {
    private final int speedLevel;
    private final int hpLevel;
    private final int dmgLevel;
    private final int jumpLevel;
    private final int armorLevel;
    private final boolean climbUnlocked;

    public SyncSkillDataPacket(int speedLevel, int hpLevel, int dmgLevel, int jumpLevel, int armorLevel, boolean climbUnlocked) {
        this.speedLevel = speedLevel;
        this.hpLevel = hpLevel;
        this.dmgLevel = dmgLevel;
        this.jumpLevel = jumpLevel;
        this.armorLevel = armorLevel;
        this.climbUnlocked = climbUnlocked;
    }

    public SyncSkillDataPacket(FriendlyByteBuf buf) {
        this.speedLevel = buf.readInt();
        this.hpLevel = buf.readInt();
        this.dmgLevel = buf.readInt();
        this.jumpLevel = buf.readInt();
        this.armorLevel = buf.readInt();
        this.climbUnlocked = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(speedLevel);
        buf.writeInt(hpLevel);
        buf.writeInt(dmgLevel);
        buf.writeInt(jumpLevel);
        buf.writeInt(armorLevel);
        buf.writeBoolean(climbUnlocked);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandles.syncSkillData(speedLevel, hpLevel, dmgLevel, jumpLevel, armorLevel, climbUnlocked);
        });
        return true;
    }



    public static final Type<SyncSkillDataPacket> TYPE
            = new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "sync_skill_data"));

    public static final StreamCodec<FriendlyByteBuf, SyncSkillDataPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new SyncSkillDataPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
