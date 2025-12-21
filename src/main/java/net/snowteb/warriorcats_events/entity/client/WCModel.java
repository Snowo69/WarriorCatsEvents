package net.snowteb.warriorcats_events.entity.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WCModel extends GeoModel<WCatEntity> {

    ModelPart rightPaw;

    /**
     * An array list of all the textures available.
     */
    public static final ResourceLocation[] TEXTURES = {
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin1.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin2.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin3.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin4.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin5.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin6.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin7.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin8.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin9.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin10.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin11.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin12.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/chestnutpatch.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/ratstar.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/twitchstream.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/blazepit.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/bengalpelt.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/sparrowstar.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/foxeater.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/willowsong.png")
    };

    @Override
    public ResourceLocation getModelResource(WCatEntity object) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.geo.json");
    }

    /**
     * Depending on the variant, grab a texture from the list.
     */
    @Override
    public ResourceLocation getTextureResource(WCatEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(WCatEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "animations/wcat.animation.json");
    }


    @Override
    public void setCustomAnimations(WCatEntity animatable, long instanceId, AnimationState<WCatEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("mainHead");


        getBone("crown").ifPresent(bone -> {
            boolean hasCrown = animatable
                    .getItemBySlot(EquipmentSlot.HEAD)
                    .is(ModItems.FLOWER_CROWN.get());
            bone.setHidden(!hasCrown);
        });

        getBone("mane").ifPresent(bone -> {
            boolean hasMane = animatable
                    .getItemBySlot(EquipmentSlot.HEAD)
                    .is(ModItems.LEAF_MANE.get());
            bone.setHidden(!hasMane);
        });

        {
            boolean hasFlowerArmor = animatable.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.FLOWER_ARMOR.get());

            getBone("flower_upper_armor").ifPresent(bone -> bone.setHidden(!hasFlowerArmor));
            getBone("layer_2").ifPresent(bone -> bone.setHidden(!hasFlowerArmor));
            getBone("layer_3").ifPresent(bone -> bone.setHidden(!hasFlowerArmor));
        }

        {
            boolean hasTeethClaws = animatable.getItemBySlot(EquipmentSlot.FEET).is(ModItems.TEETH_CLAWS.get());

            getBone("teethclaws1").ifPresent(bone -> bone.setHidden(!hasTeethClaws));
            getBone("teethclaws2").ifPresent(bone -> bone.setHidden(!hasTeethClaws));
            getBone("teethclaws3").ifPresent(bone -> bone.setHidden(!hasTeethClaws));
            getBone("teethclaws4").ifPresent(bone -> bone.setHidden(!hasTeethClaws));
        }



        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }


}
