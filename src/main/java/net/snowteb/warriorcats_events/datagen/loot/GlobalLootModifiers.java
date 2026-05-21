package net.snowteb.warriorcats_events.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.loot.AddItemModifier;

public class GlobalLootModifiers extends GlobalLootModifierProvider {
    public GlobalLootModifiers(PackOutput output) {
        super(output, WarriorCatsEvents.MODID);
    }

    @Override
    protected void start() {
        this.add("collar_from_villages", new AddItemModifier(new LootItemCondition[]{
            new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village_toolsmith")).build()
        }, ModItems.RED_CAT_COLLAR.get()));
    }
}
