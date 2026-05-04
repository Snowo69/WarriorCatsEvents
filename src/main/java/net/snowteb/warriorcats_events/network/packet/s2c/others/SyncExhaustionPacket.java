package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.client.ClientThirstData;

import java.util.function.Supplier;

public class SyncExhaustionPacket {
    private final int exhaustionLevel;

    public SyncExhaustionPacket(int exhaustionLevel) {
        this.exhaustionLevel = exhaustionLevel;

    }

    public SyncExhaustionPacket(FriendlyByteBuf buf) {
        this.exhaustionLevel = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(exhaustionLevel);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            WCEClient.setExhaustionLevel(exhaustionLevel);
        });
        return true;
    }


}
