package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class CtSFishFailed implements CustomPacketPayload {

    public CtSFishFailed() {}

    public static void encode(CtSFishFailed msg, FriendlyByteBuf buf) {}

    public static CtSFishFailed decode(FriendlyByteBuf buf) {
        return new CtSFishFailed();
    }

    public static void handle(CtSFishFailed msg, IPayloadContext ctx) {

        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            InteractionHand pHand = InteractionHand.MAIN_HAND;
            ItemStack itemstack = player.getItemInHand(pHand);
            itemstack.hurtAndBreak(3, player.serverLevel(), player, (p) -> {});

            ServerLevel level = player.serverLevel();

            RandomSource random = level.getRandom();

            int itemNumber = random.nextInt(8) + 1;
            ItemStack itemEarned;

            float airOrTrash = random.nextFloat();
            if (airOrTrash < 0.5f) {
                itemEarned = new ItemStack(Items.AIR);
            } else {
                switch (itemNumber) {
                    case 1 -> itemEarned = new ItemStack(Items.KELP);
                    case 2 -> itemEarned = new ItemStack(Items.BONE);
                    case 3 -> itemEarned = new ItemStack(Items.DIRT);
                    case 4 -> itemEarned = new ItemStack(Items.LEATHER_BOOTS);
                    case 5 -> itemEarned = new ItemStack(Items.ROTTEN_FLESH);
                    case 6 -> itemEarned = new ItemStack(Items.STICK);
                    case 7 -> {
                        itemEarned = new ItemStack(Items.BROWN_DYE);
                        itemEarned.set(DataComponents.CUSTOM_NAME, Component.literal("Fox dung"));
                    }
                    case 8 -> itemEarned = new ItemStack(Items.SEAGRASS);
                    default -> itemEarned = ItemStack.EMPTY;
                }
            }

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
                    SoundEvents.PLAYER_ATTACK_NODAMAGE,
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
    }

    public static final Type<CtSFishFailed> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "fish_failed"));

    public static final StreamCodec<FriendlyByteBuf, CtSFishFailed> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
