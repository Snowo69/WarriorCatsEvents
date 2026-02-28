package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import software.bernie.geckolib.model.GeoModel;

public class AccessoryModel extends GeoModel<WCatEntity> {
    private final ResourceLocation model;
    private final ResourceLocation texture;

    public AccessoryModel(ResourceLocation model,
                          ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public ResourceLocation getModelResource(WCatEntity animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(WCatEntity animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(WCatEntity squirrelEntity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/empty.animation.json");
    }

}
