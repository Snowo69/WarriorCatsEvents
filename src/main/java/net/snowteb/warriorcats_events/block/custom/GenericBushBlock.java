package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class GenericBushBlock extends SweetBerryBushBlock {

    private final Supplier<Item> dropItem;
    private final int minDrop;
    private final int maxDrop;
    private final SoundEvent harvestSound;

    public GenericBushBlock(Properties props, Supplier<Item> dropItem, int minDrop, int maxDrop, SoundEvent harvestSound) {
        super(props);
        this.dropItem = dropItem;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.harvestSound = harvestSound;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(dropItem.get());
    }


    /**
     * If the block age is not 3 and the item in hand is Bone Meal, Interaction result is PASS,
     * Another method will handle it, the super in this case.
     *
     * If the age is bigger than stage 1, harvest the items, and replace the block.
     */

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        int age = state.getValue(AGE);
        boolean mature = age == 3;

        if (!mature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        }

        if (age > 1) {
            int amount = minDrop + level.random.nextInt(maxDrop - minDrop + 1);
            popResource(level, pos, new ItemStack(dropItem.get(), amount));

            level.playSound(null, pos, harvestSound,
                    SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);

            BlockState newState = state.setValue(AGE, 1);
            level.setBlock(pos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, hit);
    }

    /**
     * If an entity is inside the bush, make it get stuck (Overrides so it doesn't hurt as the berry bush does)
     */

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.makeStuckInBlock(state, new Vec3(1D, 1D, 1D));
        }
    }

    /**
     * If the age is not at its max, the light level is 9 or greater,
     * and a random generated number is correct, then grow the plant.
     */

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);

        if (age < 3 &&
                level.getRawBrightness(pos.above(), 0) >= 9 &&
                net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(10) == 0)) {

            BlockState newState = state.setValue(AGE, age + 1);
            level.setBlock(pos, newState, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }


}
