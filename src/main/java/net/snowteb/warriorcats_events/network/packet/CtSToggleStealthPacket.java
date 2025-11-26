package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

import java.util.function.Supplier;

public class CtSToggleStealthPacket {

    private boolean state;

    public CtSToggleStealthPacket(boolean state) {
        this.state = state;
    }

    public CtSToggleStealthPacket(FriendlyByteBuf buf) {
        this.state = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(state);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

                if (!cap.isUnlocked()) {
                    cap.setStealthOn(false);
                    player.setInvisible(false);
                    return;
                }
                cap.setStealthOn(state);

                if (state) {
                    player.level().playSound(
                            null,
                            player.blockPosition(),
                            SoundEvents.AZALEA_HIT,
                            SoundSource.PLAYERS,
                            0.6f,
                            1f
                    );
                } else {
                    player.level().playSound(
                            null,
                            player.blockPosition(),
                            SoundEvents.AZALEA_BREAK,
                            SoundSource.PLAYERS,
                            0.6f,
                            0.8f
                    );
                }
                player.setInvisible(state);
            });

        });

        return true;
    }
}
