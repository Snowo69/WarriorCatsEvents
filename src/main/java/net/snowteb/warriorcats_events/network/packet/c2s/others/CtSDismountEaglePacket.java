package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.effect.ModEffects;

public class CtSDismountEaglePacket implements CustomPacketPayload {

    public CtSDismountEaglePacket() {
    }

    public CtSDismountEaglePacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {

            ServerPlayer player = (ServerPlayer) context.player();
            ServerLevel level = player.serverLevel().getLevel();

            if (player.getVehicle() != null) {
                level.playSound(null, player.blockPosition(), SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.7f ,1.0f);

                player.stopRiding();

                level.sendParticles(ParticleTypes.CLOUD,
                        player.getX(), player.getY(), player.getZ(),
                15,0,0,0,0.5);

                player.addEffect(new MobEffectInstance(ModEffects.EAGLE_ESCAPIST, 100));
            }

        });
        return true;
    }

    public static final Type<CtSDismountEaglePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "dismount_eagle_packet"));

    public static final StreamCodec<FriendlyByteBuf, CtSDismountEaglePacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new CtSDismountEaglePacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
