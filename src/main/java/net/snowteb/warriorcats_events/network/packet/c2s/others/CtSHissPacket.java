package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import tocraft.walkers.api.PlayerShape;

import java.util.function.Supplier;

public class CtSHissPacket {

    public CtSHissPacket() {

    }

    public CtSHissPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel().getLevel();

            if (!(PlayerShape.getCurrentShape(player) instanceof WCatEntity)) return;

            level.playSound(null, player.blockPosition(), SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.7f ,1.0f);

//            if (level.getRandom().nextInt(5) == 0) {
//                AABB aabb = player.getBoundingBox().inflate(10.0);
//
//                level.getEntities(player, aabb).forEach(entity -> {
//                   if (entity instanceof PathfinderMob livEnt && livEnt.getTarget() == player) {
//                       if (level.getRandom().nextInt(2) == 0) {
//                           Vec3 entityPos = livEnt.position();
//                           Vec3 playerPos = player.position();
//
//                           Vec3 direction = entityPos.subtract(playerPos).normalize();
//                           Vec3 targetPos = entityPos.add(direction.scale(12.0));
//                           livEnt.setTarget(null);
//                           livEnt.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.4);
//                           livEnt.setAggressive(false);
//                           level.sendParticles(
//                                   ParticleTypes.CAMPFIRE_COSY_SMOKE,
//                                   livEnt.getX(), livEnt.getY(), livEnt.getZ(),
//                                   10, 0.2, 0.5, 0.2, 0.2
//                           );
//                       }
//                   }
//                });
//            }

        });
        return true;
    }
}
