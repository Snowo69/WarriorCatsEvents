package net.snowteb.warriorcats_events.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class AddItemPoolModifier extends LootModifier {
    public static final Supplier<Codec<AddItemPoolModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec()
            .listOf().fieldOf("items").forGetter(m -> m.items))
            .and(Codec.FLOAT.optionalFieldOf("chance", 0.5f)
                    .forGetter(m -> m.chance))
            .apply(inst, AddItemPoolModifier::new)));
    private final List<Item> items;
    private final float chance;

    public AddItemPoolModifier(LootItemCondition[] conditionsIn, List<Item> items, float chance) {
        super(conditionsIn);
        this.items = items;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() <= chance) {
            Item item = items.get(context.getRandom().nextInt(items.size()));
            generatedLoot.add(new ItemStack(item));
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
