package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.PigeonEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PigeonModel extends GeoModel<PigeonEntity> {


    @Override
    public ResourceLocation getModelResource(PigeonEntity object) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/pigeon.geo.json");
    }

    /**
     * Depending on the variant chosen, then render a different texture
     */
    @Override
    public ResourceLocation getTextureResource(PigeonEntity entity) {
        return switch (entity.getPigeonVariant()) {
            case 1 -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_2.png");
            case 2 -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_3.png");
            default -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_1.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(PigeonEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/pigeon.animation.json");
    }


    @Override
    public void setCustomAnimations(PigeonEntity animatable, long instanceId, AnimationState<PigeonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("mainhead");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

    }
}
