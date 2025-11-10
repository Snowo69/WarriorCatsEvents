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
        public static final TagKey<Block> CUSTOMHERBBLOCKS = tag("custom_herb_blocks"); {}



        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(WarriorCatsEvents.MODID, name));
        }
    }
    public static class Items {



        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(WarriorCatsEvents.MODID, name));
        }
    }
}
