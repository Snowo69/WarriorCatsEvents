package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.client.DialogueMessage;
import net.snowteb.warriorcats_events.client.DialogueMessageDistributor;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;

import java.util.function.Supplier;

public class SendDialoguePacket {
    private final Component sender;
    private final int catID;
    private final String message;

    public SendDialoguePacket(Component sender, String dialogue, WCatEntity cat) {
        this.sender = sender;
        this.catID = cat.getId();
        this.message = dialogue;
    }

    public SendDialoguePacket(FriendlyByteBuf buf) {
        this.sender = buf.readComponent();
        this.message = buf.readUtf();
        this.catID = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(sender);
        buf.writeUtf(message);
        buf.writeInt(catID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandles.sendDialogue(sender, message, catID);
        });
        return true;
    }


}
