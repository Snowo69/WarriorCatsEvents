package net.snowteb.warriorcats_events.network.packet.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class SyncSkillDataPacket {
    private final int speedLevel;
    private final int hpLevel;
    private final int dmgLevel;
    private final int jumpLevel;
    private final int armorLevel;

    public SyncSkillDataPacket(int speedLevel, int hpLevel, int dmgLevel, int jumpLevel, int armorLevel) {
        this.speedLevel = speedLevel;
        this.hpLevel = hpLevel;
        this.dmgLevel = dmgLevel;
        this.jumpLevel = jumpLevel;
        this.armorLevel = armorLevel;
    }

    public SyncSkillDataPacket(FriendlyByteBuf buf) {
        this.speedLevel = buf.readInt();
        this.hpLevel = buf.readInt();
        this.dmgLevel = buf.readInt();
        this.jumpLevel = buf.readInt();
        this.armorLevel = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(speedLevel);
        buf.writeInt(hpLevel);
        buf.writeInt(dmgLevel);
        buf.writeInt(jumpLevel);
        buf.writeInt(armorLevel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            LazyOptional<ISkillData> cap = player.getCapability(PlayerSkillProvider.SKILL_DATA);
            cap.ifPresent(data -> {
                data.setSpeedLevel(speedLevel);
                data.setHPLevel(hpLevel);
                data.setDMGLevel(dmgLevel);
                data.setJumpLevel(jumpLevel);
                data.setArmorLevel(armorLevel);
            });





        });
        return true;
    }
}
