package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class CtSSwitchShape {

    public CtSSwitchShape() {

    }

    public CtSSwitchShape(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

                EntityType<?> WCAT = ModEntities.WCAT.get();

                LivingEntity current = PlayerShape.getCurrentShape(player);
                ServerLevel level = (ServerLevel) player.level();

                if (!(current instanceof WCatEntity)) {

                    LivingEntity wcat = (LivingEntity) WCAT.create(level);

                    PlayerShape.updateShapes(player, wcat);

                } else {
                    PlayerShape.updateShapes(player, null);
                }


        });

        ctx.get().setPacketHandled(true);

        return true;
    }
}
