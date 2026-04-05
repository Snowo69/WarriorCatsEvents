package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TreeStumpBlockEntity extends BlockEntity {

    private ChunkPos territoryPos;

    private UUID ownerClanUUID = ClanData.EMPTY_UUID;
    private String ownerClanName = "";
    private String territoryName = "";
    int ownerClanColor = 0;

    private int timeUntilRenewScent = 0;
    private int progressToReclaim = 0;

    public TreeStumpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TREE_STUMP.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        if (this.territoryPos != null) {
            CompoundTag chunkPos = new CompoundTag();
            chunkPos.putInt("x", territoryPos.x);
            chunkPos.putInt("z", territoryPos.z);

            pTag.put("ChunkPos", chunkPos);
        }

        if (this.ownerClanUUID != null) {
            pTag.putUUID("OwnerClanUUID", this.ownerClanUUID);
        }

        pTag.putInt("TimeUntilRenewScent", this.timeUntilRenewScent);

        pTag.putString("OwnerClanName", this.ownerClanName);
        pTag.putInt("OwnerClanColor", this.ownerClanColor);

        pTag.putInt("ProgressToReclaim", this.progressToReclaim);

        pTag.putString("TerritoryName", this.territoryName);

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("ChunkPos")) {
            CompoundTag chunkPos = pTag.getCompound("ChunkPos");
            int x = chunkPos.getInt("x");
            int z = chunkPos.getInt("z");

            this.territoryPos = new ChunkPos(x, z);
        }

        if (pTag.contains("OwnerClanUUID")) {
            this.ownerClanUUID = pTag.getUUID("OwnerClanUUID");
        }

        if (pTag.contains("TimeUntilRenewScent")) {
            this.timeUntilRenewScent = pTag.getInt("TimeUntilRenewScent");
        }

        if (pTag.contains("OwnerClanName")) {
            this.ownerClanName = pTag.getString("OwnerClanName");
        }

        if (pTag.contains("OwnerClanColor")) {
            this.ownerClanColor = pTag.getInt("OwnerClanColor");
        }

        if (pTag.contains("TerritoryName")) {
            this.territoryName = pTag.getString("TerritoryName");
        }

        if (pTag.contains("ProgressToReclaim")) {
            this.progressToReclaim = pTag.getInt("ProgressToReclaim");
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

    public void setOwnerClanUUID(UUID ownerClanUUID) {
        this.ownerClanUUID = ownerClanUUID;
    }

    public void setTerritoryPos(ChunkPos territoryPos) {
        this.territoryPos = territoryPos;
    }

    public ChunkPos getTerritoryPos() {
        return territoryPos;
    }

    public UUID getOwnerClanUUID() {
        return ownerClanUUID;
    }

    public void setTimeUntilRenewScent(int timeUntilRenewScent) {
        this.timeUntilRenewScent = timeUntilRenewScent;
        setChanged();
    }

    public int getTimeUntilRenewScent() {
        return timeUntilRenewScent;
    }

    public void resetTimer() {
        this.timeUntilRenewScent = ((WCEServerConfig.SERVER.MAX_TERRITORY_TIME.get()*20*60)/8);
    }

    public int getProgressToReclaim() {
        return progressToReclaim;
    }

    public void setProgressToReclaim(int progressToReclaim) {
        this.progressToReclaim = progressToReclaim;
    }

    public void setOwnerClanColor(int ownerClanColor) {
        this.ownerClanColor = ownerClanColor;
    }

    public void setOwnerClanName(String ownerClanName) {
        this.ownerClanName = ownerClanName;
    }

    public int getOwnerClanColor() {
        return ownerClanColor;
    }

    public String getOwnerClanName() {
        return ownerClanName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public String getTerritoryName() {
        return territoryName;
    }
}
