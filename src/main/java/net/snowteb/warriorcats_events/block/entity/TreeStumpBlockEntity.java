package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.territoryPos != null) {
            CompoundTag chunkPos = new CompoundTag();
            chunkPos.putInt("x", territoryPos.x);
            chunkPos.putInt("z", territoryPos.z);

            tag.put("ChunkPos", chunkPos);
        }

        if (this.ownerClanUUID != null) {
            tag.putUUID("OwnerClanUUID", this.ownerClanUUID);
        }

        tag.putInt("TimeUntilRenewScent", this.timeUntilRenewScent);

        tag.putString("OwnerClanName", this.ownerClanName);
        tag.putInt("OwnerClanColor", this.ownerClanColor);

        tag.putInt("ProgressToReclaim", this.progressToReclaim);

        tag.putString("TerritoryName", this.territoryName);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("ChunkPos")) {
            CompoundTag chunkPos = tag.getCompound("ChunkPos");
            int x = chunkPos.getInt("x");
            int z = chunkPos.getInt("z");

            this.territoryPos = new ChunkPos(x, z);
        }

        if (tag.contains("OwnerClanUUID")) {
            this.ownerClanUUID = tag.getUUID("OwnerClanUUID");
        }

        if (tag.contains("TimeUntilRenewScent")) {
            this.timeUntilRenewScent = tag.getInt("TimeUntilRenewScent");
        }

        if (tag.contains("OwnerClanName")) {
            this.ownerClanName = tag.getString("OwnerClanName");
        }

        if (tag.contains("OwnerClanColor")) {
            this.ownerClanColor = tag.getInt("OwnerClanColor");
        }

        if (tag.contains("TerritoryName")) {
            this.territoryName = tag.getString("TerritoryName");
        }

        if (tag.contains("ProgressToReclaim")) {
            this.progressToReclaim = tag.getInt("ProgressToReclaim");
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(pkt.getTag(), lookupProvider);
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

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level instanceof ServerLevel sLevel) {
            ClanData data = ClanData.get(sLevel.getServer().overworld());
            ClanData.Clan clan = data.getClan(this.getOwnerClanUUID());
            if (clan != null) {
                this.setOwnerClanName(clan.name);
            }
        }
    }

}
