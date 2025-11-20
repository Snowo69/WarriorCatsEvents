package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.MouseEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MouseModel extends GeoModel<MouseEntity> {
    @Override
    public ResourceLocation getModelResource(MouseEntity mouseEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/mouse.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MouseEntity mouseEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/mouse.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MouseEntity mouseEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/mouse.animation.json");
    }

    @Override
    public void setCustomAnimations(MouseEntity animatable, long instanceId, AnimationState<MouseEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
