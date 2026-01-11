package net.snowteb.warriorcats_events.network.packet.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientThirstData;

import java.util.function.Supplier;

public class ThirstDataSyncStCPacket {
    private final int thirst;

    public ThirstDataSyncStCPacket(int thirst) {
        this.thirst = thirst;

    }

    public ThirstDataSyncStCPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ClientThirstData.set(thirst);

        });
        return true;
    }


}
