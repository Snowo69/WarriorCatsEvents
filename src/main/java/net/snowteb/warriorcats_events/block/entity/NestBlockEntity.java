package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.clan.ClanData;

import java.util.UUID;

public class NestBlockEntity extends BlockEntity {
    private UUID catUUID = ClanData.EMPTY_UUID;
    private String catName = "";

    public NestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MOSS_BED.get(), pos, blockState);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putUUID("Owner", catUUID);
        tag.putString("Name", catName);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Owner")) this.catUUID =  tag.getUUID("Owner");
        if (tag.contains("Name")) this.catName =  tag.getString("Name");
    }

    public void assignNest(UUID uuid) {
        this.catUUID = uuid;
    }

    public UUID getAssignedUUID() {
        return this.catUUID;
    }

    public boolean isOwnedBy(UUID uuid) {
        return this.catUUID.equals(uuid);
    }

    public String getCatName() {
        return catName;
    }
    public void setCatName(String catName) {
        this.catName = catName;
    }

    public LivingEntity getAssignedEntity(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(catUUID);
            if (entity instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return null;
    }

    public void resetAssigned() {
        this.catUUID = ClanData.EMPTY_UUID;
        this.catName = "";
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
