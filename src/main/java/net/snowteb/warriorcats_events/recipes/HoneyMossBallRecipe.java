package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.CollarArmorItem;
import net.snowteb.warriorcats_events.item.custom.MossBallItem;

public class HoneyMossBallRecipe extends CustomRecipe {

    public HoneyMossBallRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {

        boolean hasBall = false;

        boolean hasHoney = false;

        boolean alreadyHasHoney = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof MossBallItem ball) {
                if (hasBall) return false;
                if (MossBallItem.getHoneyLevel(stack) > 0) alreadyHasHoney = true;

                hasBall = true;
            } else if ((stack.is(Items.HONEY_BOTTLE) || stack.is(Items.HONEY_BLOCK)) && !alreadyHasHoney) {
                if (hasHoney) return false;
                hasHoney = true;
            } else {
                return false;
            }
        }

        return hasBall && (hasHoney);
    }

    @Override
    public ItemStack assemble(CraftingContainer table, RegistryAccess access) {
        ItemStack ball = ItemStack.EMPTY;

        boolean hasHoney = false;

        boolean alreadyHasHoney = false;

        for (int i = 0; i < table.getContainerSize(); i++) {
            ItemStack stack = table.getItem(i);

            if (stack.getItem() instanceof MossBallItem ballItem) {
                ball = stack.copy();

                if (MossBallItem.getHoneyLevel(ball) > 0) alreadyHasHoney = true;

            }

            if ((stack.is(Items.HONEY_BOTTLE) || stack.is(Items.HONEY_BLOCK)) && !alreadyHasHoney) hasHoney = true;

        }

        if (!ball.isEmpty()) {

            if (hasHoney) {
                MossBallItem.setHoneyLevel(ball, 5);
            }

        }

        return ball;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WCERecipes.HONEY_MOSSBALL_SERIALIZER.get();
    }

}

