// Paquete que envía el cliente al servidor para pedir datos
package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;

import java.util.function.Supplier;

public class ReqSkillDataPacket {

    public ReqSkillDataPacket() {}

    public ReqSkillDataPacket(FriendlyByteBuf buf) {} // Leer del buffer, si tuviera datos

    public void toBytes(FriendlyByteBuf buf) {} // No hay datos

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            player.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(data -> {
                ModPackets.sendToPlayer(new SyncSkillDataPacket(data.getSpeedLevel(),
                        data.getHPLevel()), player);
            });

        });
        return true;
    }
}
