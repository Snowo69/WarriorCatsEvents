package net.snowteb.warriorcats_events.entity.client;

/*
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.entity.custom.ModModelLayers;
import net.snowteb.warriorcats_events.entity.custom.VanillaWCatEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import tocraft.walkers.api.PlayerShape;

public class VanillaWCatRenderer extends MobRenderer<VanillaWCatEntity, VanillaWCatModel<VanillaWCatEntity>> {
    public VanillaWCatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new VanillaWCatModel<>(pContext.bakeLayer(ModModelLayers.VANILLAWCAT_LAYER)), 1.0F);
    }

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
    public ResourceLocation getTextureLocation(VanillaWCatEntity pEntity) {
        return TEXTURES[pEntity.getVariant()];
    }

    @Override
    public void render(VanillaWCatEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        if (pEntity == PlayerShape.getCurrentShape(Minecraft.getInstance().player)) {
            AnimationClientData.isPlayerShape = true;
        } else {
            AnimationClientData.isPlayerShape = false;
        }


        if (pEntity.isBaby()) {
            pPoseStack.scale(0.4f, 0.4f, 0.4f);
        }
        if (pEntity.isAppScale() && pEntity.isBaby()){
            pPoseStack.scale(1.75f, 1.75f, 1.75f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
*/