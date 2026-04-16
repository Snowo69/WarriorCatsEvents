package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.clan.ClanData;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MossBedBlockEntity extends BlockEntity {
    private DyeColor color;

    private UUID catUUID = ClanData.EMPTY_UUID;
    private String catName = "";

    public MossBedBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MOSS_BED.get(), pos, blockState);
        this.color = DyeColor.RED;
    }

    public MossBedBlockEntity(BlockPos pos, BlockState blockState, DyeColor color) {
        super(ModBlockEntities.MOSS_BED.get(), pos, blockState);
        this.color = color;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putUUID("Owner", catUUID);
        pTag.putString("Name", catName);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Owner")) this.catUUID =  pTag.getUUID("Owner");
        if (pTag.contains("Name")) this.catName =  pTag.getString("Name");
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
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }


}
