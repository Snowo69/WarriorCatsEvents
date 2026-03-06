package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.FlowerArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class AccessoryModel extends GeoModel<WCatEntity> {
    private final ResourceLocation model;
    public ResourceLocation texture;

    public static final ResourceLocation[] FEATHER_TEXTURES = {
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/red_parrot_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/gray_parrot_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/green_parrot_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/blue_parrot_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/light_blue_parrot_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png"),

            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/pigeon_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/crow_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/american_goldfinch_feathers.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/red_cardinal_feathers.png"),
    };

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
