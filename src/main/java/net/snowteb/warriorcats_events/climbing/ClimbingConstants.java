package net.snowteb.warriorcats_events.climbing;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ClimbingConstants {

    public static final List<Block> VALID_BLOCKS = new ArrayList<>();
    public static final List<TagKey<Block>> VALID_TAGS = new ArrayList<>();

    public static final List<Block> HARD_BLOCKS = new ArrayList<>();
    public static final List<TagKey<Block>> HARD_BLOCKTAGS = new ArrayList<>();


    static {
        VALID_TAGS.add(BlockTags.LOGS);
        VALID_TAGS.add(BlockTags.LOGS_THAT_BURN);
        VALID_TAGS.add(BlockTags.OVERWORLD_NATURAL_LOGS);

        VALID_TAGS.add(BlockTags.STONE_BRICKS);
        HARD_BLOCKTAGS.add(BlockTags.STONE_BRICKS);

        VALID_TAGS.add(BlockTags.PLANKS);
        VALID_TAGS.add(BlockTags.WOOL);

        VALID_TAGS.add(BlockTags.BASE_STONE_OVERWORLD);
        HARD_BLOCKTAGS.add(BlockTags.BASE_STONE_OVERWORLD);

        VALID_TAGS.add(BlockTags.STONE_ORE_REPLACEABLES);
        HARD_BLOCKTAGS.add(BlockTags.STONE_ORE_REPLACEABLES);
        VALID_TAGS.add(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        HARD_BLOCKTAGS.add(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        VALID_TAGS.add(BlockTags.TERRACOTTA);
        HARD_BLOCKTAGS.add(BlockTags.TERRACOTTA);

        VALID_BLOCKS.add(Blocks.COBBLESTONE);
        HARD_BLOCKS.add(Blocks.COBBLESTONE);
        VALID_BLOCKS.add(Blocks.MOSSY_COBBLESTONE);
        HARD_BLOCKS.add(Blocks.MOSSY_COBBLESTONE);
        VALID_BLOCKS.add(Blocks.MOSSY_STONE_BRICKS);
        HARD_BLOCKS.add(Blocks.MOSSY_STONE_BRICKS);
        VALID_BLOCKS.add(Blocks.INFESTED_MOSSY_STONE_BRICKS);
        HARD_BLOCKS.add(Blocks.INFESTED_MOSSY_STONE_BRICKS);
        VALID_BLOCKS.add(Blocks.DIRT);
        VALID_BLOCKS.add(Blocks.DIRT_PATH);
        VALID_BLOCKS.add(Blocks.GRASS_BLOCK);
        VALID_BLOCKS.add(Blocks.COARSE_DIRT);
        VALID_BLOCKS.add(Blocks.FARMLAND);
        VALID_BLOCKS.add(Blocks.ROOTED_DIRT);
        VALID_BLOCKS.add(Blocks.MYCELIUM);
        VALID_BLOCKS.add(Blocks.BAMBOO_MOSAIC);
        VALID_BLOCKS.add(Blocks.BOOKSHELF);
        VALID_BLOCKS.add(Blocks.CHISELED_BOOKSHELF);
        VALID_BLOCKS.add(Blocks.CHEST);
        VALID_BLOCKS.add(Blocks.CRAFTING_TABLE);
        VALID_BLOCKS.add(Blocks.PUMPKIN);
        VALID_BLOCKS.add(Blocks.CARVED_PUMPKIN);
        VALID_BLOCKS.add(Blocks.SOUL_SOIL);
        HARD_BLOCKS.add(Blocks.SOUL_SOIL);
        VALID_BLOCKS.add(Blocks.HAY_BLOCK);
        VALID_BLOCKS.add(Blocks.NETHER_WART_BLOCK);
        VALID_BLOCKS.add(Blocks.RED_NETHER_BRICKS);
        HARD_BLOCKS.add(Blocks.RED_NETHER_BRICKS);
        VALID_BLOCKS.add(Blocks.BONE_BLOCK);
        VALID_BLOCKS.add(Blocks.WHITE_CONCRETE);
        HARD_BLOCKS.add(Blocks.WHITE_CONCRETE);
        VALID_BLOCKS.add(Blocks.ORANGE_CONCRETE);
        HARD_BLOCKS.add(Blocks.ORANGE_CONCRETE);
        VALID_BLOCKS.add(Blocks.MAGENTA_CONCRETE);
        HARD_BLOCKS.add(Blocks.MAGENTA_CONCRETE);
        VALID_BLOCKS.add(Blocks.LIGHT_BLUE_CONCRETE);
        HARD_BLOCKS.add(Blocks.LIGHT_BLUE_CONCRETE);
        VALID_BLOCKS.add(Blocks.YELLOW_CONCRETE);
        HARD_BLOCKS.add(Blocks.YELLOW_CONCRETE);
        VALID_BLOCKS.add(Blocks.LIME_CONCRETE);
        HARD_BLOCKS.add(Blocks.LIME_CONCRETE);
        VALID_BLOCKS.add(Blocks.PINK_CONCRETE);
        HARD_BLOCKS.add(Blocks.PINK_CONCRETE);
        VALID_BLOCKS.add(Blocks.GRAY_CONCRETE);
        HARD_BLOCKS.add(Blocks.GRAY_CONCRETE);
        VALID_BLOCKS.add(Blocks.LIGHT_GRAY_CONCRETE);
        HARD_BLOCKS.add(Blocks.LIGHT_GRAY_CONCRETE);
        VALID_BLOCKS.add(Blocks.CYAN_CONCRETE);
        HARD_BLOCKS.add(Blocks.CYAN_CONCRETE);
        VALID_BLOCKS.add(Blocks.PURPLE_CONCRETE);
        HARD_BLOCKS.add(Blocks.PURPLE_CONCRETE);
        VALID_BLOCKS.add(Blocks.BLUE_CONCRETE);
        HARD_BLOCKS.add(Blocks.BLUE_CONCRETE);
        VALID_BLOCKS.add(Blocks.BROWN_CONCRETE);
        HARD_BLOCKS.add(Blocks.BROWN_CONCRETE);
        VALID_BLOCKS.add(Blocks.GREEN_CONCRETE);
        HARD_BLOCKS.add(Blocks.GREEN_CONCRETE);
        VALID_BLOCKS.add(Blocks.RED_CONCRETE);
        HARD_BLOCKS.add(Blocks.RED_CONCRETE);
        VALID_BLOCKS.add(Blocks.BLACK_CONCRETE);
        HARD_BLOCKS.add(Blocks.BLACK_CONCRETE);
        VALID_BLOCKS.add(Blocks.RED_MUSHROOM_BLOCK);
        VALID_BLOCKS.add(Blocks.BROWN_MUSHROOM_BLOCK);
        VALID_BLOCKS.add(Blocks.MUSHROOM_STEM);




    }

    public static boolean isValidClimbable(BlockState state) {

        Block block = state.getBlock();
        if (VALID_BLOCKS.contains(block)) {
            return true;
        }
        for (TagKey<Block> tag : VALID_TAGS) {
            if (state.is(tag)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHardSurface(BlockState state) {
        Block block = state.getBlock();
        if (HARD_BLOCKS.contains(block)) {
            return true;
        }
        for (TagKey<Block> tag : HARD_BLOCKTAGS) {
            if (state.is(tag)) {
                return true;
            }
        }

        return false;
    }

}
