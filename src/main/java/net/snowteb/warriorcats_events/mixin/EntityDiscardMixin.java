//package net.snowteb.warriorcats_events.mixin;
//
//import net.minecraft.world.entity.Entity;
//import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(Entity.class)
//public class EntityDiscardMixin {
//
//    @Inject(method = "discard", at = @At("HEAD"), cancellable = true)
//    private void blockDiscard(CallbackInfo ci){
//        Entity target = (Entity)(Object)this;
//
//        if (target instanceof WCatEntity cat) {
//            if (cat.isTame()) {
//                ci.cancel();
//            }
//        }
//    }
//
//}
