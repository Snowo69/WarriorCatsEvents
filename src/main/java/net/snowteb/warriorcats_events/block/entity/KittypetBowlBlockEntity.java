package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.snowteb.warriorcats_events.block.custom.KittyPetBowl;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.others.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.thirst.PlayerThirstProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KittypetBowlBlockEntity extends BlockEntity {

    private float waterLevel = 0;
    private float foodLevel = 0;

    public KittypetBowlBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.KITTYPET_BOWL.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putFloat("waterLevel", waterLevel);
        pTag.putFloat("foodLevel", foodLevel);

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("waterLevel")) {
            waterLevel = pTag.getFloat("waterLevel");
        }

        if (pTag.contains("foodLevel")) {
            foodLevel = pTag.getFloat("foodLevel");
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }


    public float getFoodLevel() {
        return foodLevel;
    }

    public float getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(float waterLevel) {
        this.waterLevel = Mth.clamp(waterLevel, 0, 3);
    }

    public void setFoodLevel(float foodLevel) {
        this.foodLevel = Mth.clamp(foodLevel, 0, 3);
    }

    public boolean canRefillWater() {
        return waterLevel < 3 && foodLevel <= 0;
    }

    public boolean canRefillFood() {
        return foodLevel < 3 && waterLevel <= 0;
    }

    public boolean isBowlEmpty() {
        return foodLevel <= 0 && waterLevel <= 0;
    }

    public boolean hasFood() {
        return foodLevel > 0;
    }

    public boolean hasWater() {
        return waterLevel > 0;
    }

    public boolean isBowlFull() {
        return waterLevel >= 3 || foodLevel >= 3;
    }

    public void performFillWater(ServerLevel pLevel, Vec3 pPos, boolean partiallyFill) {

        if (partiallyFill) setWaterLevel(Mth.clamp(getWaterLevel() + 1, 0, 3));
        else setWaterLevel(3);


        pLevel.playSound(null, BlockPos.containing(pPos),
                SoundEvents.DROWNED_SWIM, SoundSource.PLAYERS, 0.3f ,1.5f);
        pLevel.sendParticles(ParticleTypes.SPLASH, pPos.x,pPos.y + 0.4,
                pPos.z, 10, 0.1, 0.0, 0.1, 0.01);

    }

    public void performFillFood(ServerLevel pLevel, Vec3 pPos) {
        setFoodLevel(3);


        pLevel.playSound(null, BlockPos.containing(pPos),
                SoundEvents.CAT_EAT, SoundSource.PLAYERS, 0.5f ,1.0f);

        pLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ModItems.SHREDDED_MEAT.get())), pPos.x,pPos.y + 0.4,
                pPos.z, 10, 0.1, 0.0, 0.1, 0.01);

    }

    public void drinkFromBowl(ServerLevel pLevel, Vec3 pPos, ServerPlayer pPlayer) {
        setWaterLevel(getWaterLevel() - 0.2f);
        pPlayer.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirstCap -> {
            thirstCap.addThirst(1);
            ModPackets.sendToPlayer(new ThirstDataSyncStCPacket(thirstCap.getThirst()), pPlayer);
        });

        pLevel.playSound(null, BlockPos.containing(pPos),
                SoundEvents.CAT_EAT, SoundSource.PLAYERS, 0.5f ,1.2f);

        pLevel.sendParticles(ParticleTypes.SPLASH, pPos.x,pPos.y + 0.3,
                pPos.z, 10, 0.0, 0.0, 0.0, 0.00);

    }

    public void eatFromBowl(ServerLevel pLevel, Vec3 pPos, ServerPlayer pPlayer) {
        setFoodLevel(getFoodLevel() - 0.4f);
        pPlayer.getFoodData().eat(1, 1);

        pLevel.playSound(null, BlockPos.containing(pPos),
                SoundEvents.CAT_EAT, SoundSource.PLAYERS, 0.5f ,1.0f);

        pLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ModItems.SHREDDED_MEAT.get())), pPos.x,pPos.y + 0.3,
                pPos.z, 10, 0.0, 0.0, 0.0, 0.01);

    }

}
