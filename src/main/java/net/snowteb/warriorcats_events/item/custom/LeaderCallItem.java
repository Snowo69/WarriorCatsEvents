//package net.snowteb.warriorcats_events.item.custom;
//
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
//
//import java.util.List;
//
//public class LeaderCallItem extends Item {
//    public LeaderCallItem(Properties pProperties) {
//        super(pProperties);
//    }
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//
//        if (!pLevel.isClientSide) {
//            pPlayer.getCooldowns().addCooldown(this, 20 * 15);
//
//            AABB box = pPlayer.getBoundingBox().inflate(32);
//            List<WCatEntity> cats = pLevel.getEntitiesOfClass(
//                    WCatEntity.class,
//                    box,
//                    cat -> cat.isAlive()
//            );
//
//            pLevel.playSound(null,pPlayer.blockPosition(), SoundEvents.CAT_AMBIENT,  SoundSource.PLAYERS, 1.2F, 0.9F);
//
//            for (WCatEntity cat : cats) {
//                if (cat.isTame() && cat.isOwnedBy(pPlayer)) {
//
//                    Vec3 target = pPlayer.position().add(
//                            pLevel.random.nextInt(5) - 2,
//                            0,
//                            pLevel.random.nextInt(5) - 2
//                    );
//
//
//                    cat.setLeaderCallTarget(target);
//
//                    pPlayer.getItemInHand(pUsedHand).hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pUsedHand));
//
//                }
//            }
//
//
//        }
//        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
//    }
//}
