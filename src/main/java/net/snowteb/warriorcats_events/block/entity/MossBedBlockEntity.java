package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MossBedBlockEntity extends BlockEntity {
    private DyeColor color;

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
}
