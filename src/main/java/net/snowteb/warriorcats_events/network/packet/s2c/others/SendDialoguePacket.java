package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;

public class SendDialoguePacket implements CustomPacketPayload {
    private final Component sender;
    private final int catID;
    private final String message;

    public SendDialoguePacket(Component sender, String dialogue, WCatEntity cat) {
        this.sender = sender;
        this.catID = cat.getId();
        this.message = dialogue;
    }

    public SendDialoguePacket(FriendlyByteBuf buf) {
        this.sender = buf.readJsonWithCodec(ComponentSerialization.CODEC);
        this.message = buf.readUtf();
        this.catID = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(ComponentSerialization.CODEC, sender);
        buf.writeUtf(message);
        buf.writeInt(catID);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandles.sendDialogue(sender, message, catID);
        });
        return true;
    }

    public static final Type<SendDialoguePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "send_dialogue"));

    public static final StreamCodec<FriendlyByteBuf, SendDialoguePacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new SendDialoguePacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
