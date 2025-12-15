package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModBookItem extends Item {
    public ModBookItem(Properties properties) {
        super(properties.durability(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ServerLevel serverLevel = (ServerLevel) level;

            if (player.getTags().contains("leader")) {
            serverLevel.getServer().getCommands().performPrefixedCommand(
                    serverLevel.getServer().createCommandSourceStack()
                            .withEntity(player).withSuppressedOutput(),"function warriorcats_events:leaderslives_1");

                itemStack.hurtAndBreak(1, player, (p) ->
                        p.broadcastBreakEvent(hand));

            } else {

            player.displayClientMessage(Component.translatable("warriorcats_events.ModBookItem.notleader"), true);
        }

        }

        return InteractionResultHolder.success(itemStack);
    }
}
