package net.snowteb.warriorcats_events.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.GlowrocksBlock;
import net.snowteb.warriorcats_events.block.custom.WallGlowrocksBlock;

public class GlowrocksFeature extends Feature<NoneFeatureConfiguration> {

    public GlowrocksFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {

        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        for (int i = 0; i < 80; i++) {

            BlockPos pos = origin.offset(
                    random.nextInt(8) - 4,
                    random.nextInt(8) - 4,
                    random.nextInt(8) - 4
            );

            if (!level.isEmptyBlock(pos)) continue;

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos wallPos = pos.relative(dir);

                boolean isRock = level.getBlockState(wallPos).is(BlockTags.BASE_STONE_OVERWORLD);

                if (level.getBlockState(wallPos).isSolidRender(level, wallPos) && isRock) {
                    level.setBlock(pos,
                            ModBlocks.WALL_GLOWROCKS.get()
                                    .defaultBlockState()
                                    .setValue(WallGlowrocksBlock.FACING, dir.getOpposite())
                                    .setValue(WallGlowrocksBlock.AMOUNT_WG, random.nextInt(4) + 1),
                            2
                    );

                    break;
                }
            }

            if (level.isEmptyBlock(pos)) {
                BlockState state =  level.getBlockState(pos.below());
                boolean isRock = state.is(BlockTags.BASE_STONE_OVERWORLD);

                if (state.isSolidRender(level, pos.below()) && isRock
                        && state.isFaceSturdy(level, pos.below(), Direction.UP)) {

                    level.setBlock(pos,
                            ModBlocks.GLOWROCKS.get()
                                    .defaultBlockState()
                                    .setValue(GlowrocksBlock.AMOUNT_G, random.nextInt(6) + 1),
                            2
                    );
                }
            }
        }

        return true;
    }
}