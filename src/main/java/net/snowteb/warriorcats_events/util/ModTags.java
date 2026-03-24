package net.snowteb.warriorcats_events.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> HIT_THROUGH_BLOCKS = tag("hit_through_blocks");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(WarriorCatsEvents.MODID, name));
        }
    }
    public static class Items {
        public static final TagKey<Item> PREY = tag("prey");
        public static final TagKey<Item> HERBS = tag("herbs");
        public static final TagKey<Item> FEATHERS = tag("feathers");
        public static final TagKey<Item> HERB_CRAFTING = tag("herb_crafting");

        private static TagKey<Item> tag(String name) {
               return ItemTags.create(new ResourceLocation(WarriorCatsEvents.MODID, name));
        }
    }
}
