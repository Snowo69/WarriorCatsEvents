package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CtSFishSuccesful {

    public CtSFishSuccesful() {}

    public static void encode(CtSFishSuccesful msg, FriendlyByteBuf buf) {}

    public static CtSFishSuccesful decode(FriendlyByteBuf buf) {
        return new CtSFishSuccesful();
    }

    public static void handle(CtSFishSuccesful msg, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            InteractionHand pHand = InteractionHand.MAIN_HAND;
            ItemStack itemstack = player.getItemInHand(pHand);
            itemstack.hurtAndBreak(3, player, (p) -> p.broadcastBreakEvent(pHand));

            ServerLevel level = player.serverLevel();
            RandomSource random = level.getRandom();

            int itemNumber = random.nextInt(8) + 1;
            ItemStack itemEarned;


            switch (itemNumber) {
                case 1 -> itemEarned = new ItemStack(Items.COD);
                case 2 -> itemEarned = new ItemStack(Items.SALMON);
                case 3 -> itemEarned = new ItemStack(Items.COD);
                case 4 -> itemEarned = new ItemStack(Items.SALMON);
                case 5 -> itemEarned = new ItemStack(Items.COD);
                case 6 -> itemEarned = new ItemStack(Items.SALMON);
                case 7 -> itemEarned = new ItemStack(Items.PUFFERFISH);
                case 8 -> itemEarned = new ItemStack(Items.TROPICAL_FISH);
                default -> itemEarned = ItemStack.EMPTY;
            }

            if (itemEarned.isEmpty()) return;

            ItemEntity drop = new ItemEntity(
                    level,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    itemEarned
            );

            drop.setPickUpDelay(10);
            drop.setDeltaMovement(
                    random.nextGaussian() * 0.05,
                    0.2,
                    random.nextGaussian() * 0.05
            );

            level.addFreshEntity(drop);

            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.AMBIENT_UNDERWATER_EXIT,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.0F
            );
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.COD_DEATH,
                    SoundSource.PLAYERS,
                    0.8F,
                    1.0F
            );

            level.sendParticles(
                    ParticleTypes.SPLASH,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    30,
                    0.4, 0.2, 0.4,
                    0.02
            );

        });
        ctx.get().setPacketHandled(true);
    }

}
