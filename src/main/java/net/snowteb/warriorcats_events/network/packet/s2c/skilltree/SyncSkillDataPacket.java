package net.snowteb.warriorcats_events.network.packet.s2c.skilltree;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.PlayerSkill;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class SyncSkillDataPacket implements CustomPacketPayload {
    private final PlayerSkill playerSkill;

    public SyncSkillDataPacket(PlayerSkill playerSkill) {
        this.playerSkill = playerSkill;
    }

    public SyncSkillDataPacket(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        PlayerSkill playerSkill = new PlayerSkill();
        if (tag != null){
            playerSkill.deserializeNBT(((RegistryFriendlyByteBuf) buf).registryAccess(), tag);
        }
        this.playerSkill = playerSkill;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(playerSkill.serializeNBT(((RegistryFriendlyByteBuf) buf).registryAccess()));
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandles.syncSkillData(playerSkill);
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
