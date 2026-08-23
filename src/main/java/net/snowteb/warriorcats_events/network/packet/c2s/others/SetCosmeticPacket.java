package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.diseases.DiseaseTypes;
import net.snowteb.warriorcats_events.diseases.Diseaseable;
import net.snowteb.warriorcats_events.diseases.kinds.BrokenPaw;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class SetCosmeticPacket implements CustomPacketPayload {

    private final int index;

    public SetCosmeticPacket(int index) {

        this.index = index;
    }

    public static SetCosmeticPacket decode(FriendlyByteBuf buf) {
        int index = buf.readInt();

        return new SetCosmeticPacket(index);
    }

    public static void encode(SetCosmeticPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.index);
    }

    public static void handle(SetCosmeticPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            if (!WarriorCatsEvents.Collaborators.isContributor(player.getUUID())) return;

            LivingEntity shape = PlayerShape.getCurrentShape(player);
            if (shape instanceof WCatEntity catShape) {

                PlayerShape.updateShapes(player, null);

                if (packet.index == 0) {
                    catShape.embeddedAccessories().setSunGlasses(!catShape.embeddedAccessories().hasSunGlasses());
                } else if (packet.index == 1) {
                    catShape.embeddedAccessories().setMossCoat(!catShape.embeddedAccessories().hasMossCoat());
                }

                PlayerShape.updateShapes(player, catShape);

                player.teleportTo(player.getX(), player.getY() + 0.2, player.getZ());

            }

        });
    }

    public static final Type<SetCosmeticPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "set_cosmetic"));

    public static final StreamCodec<FriendlyByteBuf, SetCosmeticPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
