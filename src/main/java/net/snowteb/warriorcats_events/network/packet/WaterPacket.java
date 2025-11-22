package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;

import java.util.function.Supplier;

public class WaterPacket {

    public WaterPacket() {

    }

    public WaterPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel().getLevel();

            if(isLookingAtWater(player, level)) {

                level.playSound(null, player.blockPosition(), SoundEvents.DROWNED_SWIM, SoundSource.PLAYERS, 0.2f ,1.5f);

                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    thirst.addThirst(1);
                    ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), player);
                    player.displayClientMessage(Component.literal("Thirst level: " + thirst.getThirst()).withStyle(ChatFormatting.GRAY), true);
                    level.sendParticles(ParticleTypes.SPLASH, player.getX(),player.getY() + 0.4, player.getZ(), 10, 0.3, 0.2, 0.3, 0.01);
                });



            } else {

                player.displayClientMessage(Component.literal("That's not water!").withStyle(ChatFormatting.RED), true);
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirst.getThirst()), player);
                });


            }

        });

        return true;
    }

    private boolean isLookingAtWater(ServerPlayer player, ServerLevel level) {
        double reach = 2.0D;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPos = eyePos.add(lookVec.scale(reach));

        ClipContext context = new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.ANY,
                player
        );

        BlockHitResult hitResult = level.clip(context);
        if (hitResult.getType() != HitResult.Type.BLOCK) return false;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        return state.getFluidState().is(FluidTags.WATER);
    }

}
