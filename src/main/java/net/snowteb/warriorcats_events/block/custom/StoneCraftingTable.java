package net.snowteb.warriorcats_events.block.custom;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.snowteb.warriorcats_events.block.entity.StoneCraftingTableBlockEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.particles.WCEParticles;
import net.snowteb.warriorcats_events.recipes.HerbsRecipe;
import net.snowteb.warriorcats_events.recipes.WCERecipes;
import net.snowteb.warriorcats_events.screen.StoneCraftingTableMenu;
import net.snowteb.warriorcats_events.util.ModTags;
import org.jetbrains.annotations.Nullable;
import tocraft.walkers.api.PlayerShape;

import java.util.Optional;

public class StoneCraftingTable extends HorizontalDirectionalBlock implements EntityBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("block.warriorcats_events.stone_crafting_table");

    protected static final VoxelShape NORTH_TABLE = Block.box(2.0D, 0.0D, 6.0D, 13.0D, 6.0D, 14.0D);
    protected static final VoxelShape NORTH_ROCK1 = Block.box(0.0D, 0.0D, 0.0D, 4.0D, 2.0D, 4.0D);
    protected static final VoxelShape NORTH_ROCK2 = Block.box(12.0D, 0.0D, 1.0D, 15.0D, 2.0D, 5.0D);

    protected static final VoxelShape SOUTH_TABLE = Block.box(3.0D, 0.0D, 2.0D, 14.0D, 6.0D, 10.0D);
    protected static final VoxelShape SOUTH_ROCK1 = Block.box(12.0D, 0.0D, 12D, 16D, 2D, 16D);
    protected static final VoxelShape SOUTH_ROCK2 = Block.box(1D, 0D, 11D, 4D, 2D, 15D);

    protected static final VoxelShape WEST_TABLE = Block.box(6.0D, 0.0D, 3.0D, 14.0D, 6.0D, 14.0D);
    protected static final VoxelShape WEST_ROCK1 = Block.box(0, 0, 12, 4, 2, 16);
    protected static final VoxelShape WEST_ROCK2 = Block.box(1, 0, 1, 5, 2, 4);

    protected static final VoxelShape EAST_TABLE = Block.box(2.0D, 0.0D, 2.0D, 10.0D, 6.0D, 13.0D);
    protected static final VoxelShape EAST_ROCK1 = Block.box(12, 0, 0, 16, 2, 4);
    protected static final VoxelShape EAST_ROCK2 = Block.box(11, 0, 12, 15, 2, 15);

    protected static final VoxelShape SHAPE_NORTH = Shapes.or(NORTH_TABLE, NORTH_ROCK1, NORTH_ROCK2);
    protected static final VoxelShape SHAPE_SOUTH = Shapes.or(SOUTH_TABLE, SOUTH_ROCK1, SOUTH_ROCK2);
    protected static final VoxelShape SHAPE_WEST = Shapes.or(WEST_TABLE, WEST_ROCK1, WEST_ROCK2);
    protected static final VoxelShape SHAPE_EAST = Shapes.or(EAST_TABLE, EAST_ROCK1, EAST_ROCK2);

    public StoneCraftingTable(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (PlayerShape.getCurrentShape(pPlayer) instanceof Animal) {

                if (pPlayer.isShiftKeyDown()) {
                    return InteractionResult.PASS;
                }

                pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
                pPlayer.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return false;
    }

    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((id, inv, player) -> {
            return new StoneCraftingTableMenu(id, inv, ContainerLevelAccess.create(pLevel, pPos));
        }, CONTAINER_TITLE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        switch (direction) {
            case EAST:
                return SHAPE_EAST;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case NORTH:
                return SHAPE_NORTH;
        }
        return SHAPE_NORTH;
    }

    public boolean handleHerbsRecipeCraftingBlockState(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand) {
        if (!pPlayer.isShiftKeyDown()) return false;
        if (!(pLevel.getBlockEntity(pPos) instanceof StoneCraftingTableBlockEntity blockEntity)) return false;

        if (pPlayer.getItemInHand(pHand).is(ModTags.Items.HERB_CRAFTING)) {

            boolean handled = blockEntity.handleInteractValidItem(pPlayer, pPlayer.getItemInHand(InteractionHand.MAIN_HAND));;
            if (handled) {
                if (pLevel instanceof ServerLevel sLevel) {
                    Vec3 position = pPos.getCenter();
                    sLevel.playSound(
                            null, position.x, position.y, position.z,
                            SoundEvents.MOSS_STEP, SoundSource.BLOCKS,
                            0.6F, (float) (0.7F + 0.3*sLevel.getRandom().nextFloat())
                    );
                    sLevel.playSound(
                            null, position.x, position.y, position.z,
                            SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                            0.2F, 1.5F
                    );
                }
            }
            return handled;

        } else if (pPlayer.getItemInHand(pHand).is(ModItems.CLAWS.get())) {

            boolean recipeSuccess = handleMakeRecipe(pState, pLevel, pPos, pPlayer, pHand, blockEntity);

            if (recipeSuccess) {
                if (pLevel instanceof ServerLevel sLevel) {
                    Vec3 position = pPos.getCenter();
                    sLevel.playSound(
                            null, position.x, position.y, position.z,
                            SoundEvents.MOSS_STEP, SoundSource.BLOCKS,
                            0.6F, (float) (0.7F + 0.3*sLevel.getRandom().nextFloat())
                    );
                    sLevel.playSound(
                            null, position.x, position.y, position.z,
                            SoundEvents.SLIME_JUMP_SMALL, SoundSource.BLOCKS,
                            0.6F, (float) (1.2F + 0.3*sLevel.getRandom().nextFloat())
                    );

                    Direction facing = pState.getValue(StoneCraftingTable.FACING);

                    position = switch (facing) {
                        case NORTH -> position.add(0,0,0.1);
                        case SOUTH -> position.add(0,0,-0.1);
                        case WEST -> position.add(0.1,0,0);
                        case EAST -> position.add(-0.1,0,0);
                        default -> position;
                    };

                    sLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            position.x, position.y + 0.4, position.z,
                            10, 0.1, 0.3, 0.1, 0.005);

                }
                return true;
            } else {
                pPlayer.displayClientMessage(Component.literal("Not a recipe").withStyle(ChatFormatting.GRAY), true);
                return false;
            }

        } else if (pPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            boolean handled = blockEntity.handleTakeItem(pPlayer);
            if (handled) {
                if (pLevel instanceof ServerLevel sLevel) {
                    Vec3 position = pPos.getCenter();
                    sLevel.playSound(
                            null, position.x, position.y, position.z,
                            SoundEvents.MOSS_STEP, SoundSource.BLOCKS,
                            0.6F, (float) (0.7F + 0.3*sLevel.getRandom().nextFloat())
                    );
                }
            }
            return handled;
        }


        return false;
    }

    public boolean handleMakeRecipe(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, StoneCraftingTableBlockEntity pBlockEntity) {

        RecipeWrapper wrapper = new RecipeWrapper(pBlockEntity.getItemStackHandler());

        Optional<HerbsRecipe> recipe = pLevel.getRecipeManager()
                .getRecipeFor(WCERecipes.HERBS.get(), wrapper, pLevel);

        if (recipe.isPresent()) {

            ItemStack result = recipe.get().assemble(wrapper, pLevel.registryAccess());

            for (int i = 0; i < pBlockEntity.getItemStackHandler().getSlots(); i++) {
                pBlockEntity.getItemStackHandler().extractItem(i, 1, false);
            }

            Vec3 pos = pPos.getCenter();

            ItemEntity itemEntity = new ItemEntity(pLevel, pos.x, pos.y, pos.z, result);
            itemEntity.setPickUpDelay(10);

            pLevel.addFreshEntity(itemEntity);

            return true;
        }
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StoneCraftingTableBlockEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {

        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof StoneCraftingTableBlockEntity) {
                ((StoneCraftingTableBlockEntity) blockEntity).dropInventory();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {

        if (pLevel.getBlockEntity(pPos) instanceof StoneCraftingTableBlockEntity blockEntity) {
            if (blockEntity.isNotEmpty()) {
                Vec3 position = pPos.getCenter();

                Direction facing = pState.getValue(StoneCraftingTable.FACING);

                position = switch (facing) {
                    case NORTH -> position.add(0,0,0.1);
                    case SOUTH -> position.add(0,0,-0.1);
                    case WEST -> position.add(0.1,0,0);
                    case EAST -> position.add(-0.1,0,0);
                    default -> position;
                };

                pLevel.sendParticles(
                        WCEParticles.HERBS.get(),
                        position.x, position.y - 0.05, position.z,
                        1, 0.0, 0.0, 0.0, 0.005);
            }
        }

        pLevel.scheduleTick(pPos, this, 15);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (!pLevel.isClientSide()) {
            pLevel.scheduleTick(pPos, this, 15);
        }
    }
}
