package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.StCKitCreateScreenPacket;
import net.snowteb.warriorcats_events.screen.clandata.KitCreateScreen;

public class KitItem extends Item {
    public KitItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();

        if (pContext.getPlayer() != null) {
            if (!pContext.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.KIT_ITEM.get())) {
                return InteractionResult.FAIL;
            }
        }

        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {

            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            BlockPos blockpos1;

            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            pContext.getPlayer().getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                cap.setTempClickedPosData(blockpos1);
            });

            if (pContext.getPlayer() instanceof ServerPlayer sPlayer) {
                    ModPackets.sendToPlayer(new StCKitCreateScreenPacket(), sPlayer);
            }

            return InteractionResult.CONSUME;
        }
    }
}
