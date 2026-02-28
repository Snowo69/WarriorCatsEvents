package net.snowteb.warriorcats_events.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.FreshkillPileBlock;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class FreshkillPileRenderer implements BlockEntityRenderer<FreshkillPileBlockEntity> {

    private static final ResourceLocation BOTTOM =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/block/fresh_kill_pile2.png");


    public FreshkillPileRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(FreshkillPileBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        List<ItemStack> items = pBlockEntity.getRenderStacks();

        ItemStack item1 = !items.isEmpty() ? items.get(0) : ItemStack.EMPTY;
        ItemStack item2 = items.size() > 1 ? items.get(1) : ItemStack.EMPTY;
        ItemStack item3 = items.size() > 2 ? items.get(2) : ItemStack.EMPTY;
        ItemStack item4 = items.size() > 3 ? items.get(3) : ItemStack.EMPTY;
        ItemStack item5 = items.size() > 4 ? items.get(4) : ItemStack.EMPTY;
        ItemStack item6 = items.size() > 5 ? items.get(5) : ItemStack.EMPTY;
        ItemStack item7 = items.size() > 6 ? items.get(6) : ItemStack.EMPTY;
        ItemStack item8 = items.size() > 7 ? items.get(7) : ItemStack.EMPTY;


        BlockState state = pBlockEntity.getBlockState();
        Direction facing = state.getValue(FreshkillPileBlock.FACING);


        pPoseStack.pushPose();

        pPoseStack.translate(0.0D, 0.001D, 0.0D);


        PoseStack.Pose pose = pPoseStack.last();
        VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityCutout(BOTTOM));

        Matrix4f matrix = pose.pose();
        Matrix3f normal = pose.normal();

        float min = 0f;
        float max = 1f;
        float y = 0f;

        int light = getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos());

        vertexConsumer.vertex(matrix, min, y, min)
                .color(255, 255, 255, 255)
                .uv(0f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(matrix, min, y, max)
                .color(255, 255, 255, 255)
                .uv(0f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(matrix, max, y, max)
                .color(255, 255, 255, 255)
                .uv(1f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(matrix, max, y, min)
                .color(255, 255, 255, 255)
                .uv(1f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();





        pPoseStack.popPose();


        pPoseStack.pushPose();

        pPoseStack.translate(0.5D, 0.0D, 0.5D);
        switch (facing) {
            case NORTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
            case SOUTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST  -> pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST  -> pPoseStack.mulPose(Axis.YP.rotationDegrees(-90));
        }

        pPoseStack.translate(-0.5D, 0.0D, -0.5D);

        pPoseStack.translate(0.5D, 0.02D, 0.5D);
        pPoseStack.scale(0.27F, 0.6F, 0.27F);

        pPoseStack.mulPose(Axis.XP.rotationDegrees(90f));

        itemRenderer.renderStatic(item1, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);

        pPoseStack.translate(0.0D, 0.5D, -0.05D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(5f));

        itemRenderer.renderStatic(item2, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);

        pPoseStack.translate(0.0D, -0.5D, 0.05D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-5f));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(65f));

        pPoseStack.translate(-0.5D, -0.8D, 0.05D);


        itemRenderer.renderStatic(item3, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);

        pPoseStack.translate(-0.4D, 1.1D, 0.00D);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(115f));


        itemRenderer.renderStatic(item4, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);

        pPoseStack.translate(-1.2D, -1.1D, -0.10D);
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(65f));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(15f));



        itemRenderer.renderStatic(item5, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);


        pPoseStack.mulPose(Axis.XP.rotationDegrees(-15f));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-95f));
        pPoseStack.translate(1.2D, 1.1D, 0.10D);



        pPoseStack.translate(0.3D, +0.2D, -0.15D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(22f));


        itemRenderer.renderStatic(item6, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);

        pPoseStack.mulPose(Axis.XP.rotationDegrees(-22f));
        pPoseStack.translate(-0.3D, -0.2D, 0.15D);

        pPoseStack.translate(-1.1D, -0.1D, -0.05D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(15f));


        itemRenderer.renderStatic(item7, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);


        pPoseStack.translate(1.1D, 0.1D, 0.05D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-15f));

        pPoseStack.translate(-2.1D, -1.2D, -0.12D);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(15f));

        itemRenderer.renderStatic(item8, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(),
                        pBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, pPoseStack,
                pBuffer, pBlockEntity.getLevel(), 1);


        pPoseStack.popPose();

    }

    private int getLightLevel(Level world, BlockPos pos) {
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
