package net.snowteb.warriorcats_events.network.packet;

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

    public SyncSkillDataPacket(int speedLevel, int hpLevel) {
        this.speedLevel = speedLevel;
        this.hpLevel = hpLevel;
    }

    public SyncSkillDataPacket(FriendlyByteBuf buf) {
        this.speedLevel = buf.readInt();
        this.hpLevel = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(speedLevel);
        buf.writeInt(hpLevel);
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
            });





        });
        return true;
    }
}
