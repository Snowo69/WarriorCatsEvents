package net.snowteb.warriorcats_events.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class TreeStumpEntityRenderer implements BlockEntityRenderer<TreeStumpBlockEntity> {

    public TreeStumpEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TreeStumpBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {


        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        if (mc.player == null) return;
//        if (!mc.player.isShiftKeyDown()) return;
        if (mc.player.distanceToSqr(pBlockEntity.getBlockPos().getCenter()) > 3*3) return;
//        HitResult hit = mc.hitResult;
//
//        if (!(hit instanceof BlockHitResult blockHitResult)) return;
//        Vec3 hitPos = blockHitResult.getLocation();
//        AABB box = new AABB(pBlockEntity.getBlockPos())
//                .inflate(0.6);
//
//        if (!box.contains(hitPos)) return;

        String clanName = pBlockEntity.getOwnerClanName();

        if (clanName.isEmpty()) return;

        int clanColor = pBlockEntity.getOwnerClanColor();

        pPoseStack.pushPose();

        pPoseStack.translate(0.5, 1.5, 0.5);

        pPoseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

        pPoseStack.scale(-0.011f, -0.011f, 0.011f);

        Matrix4f matrix = pPoseStack.last().pose();

        Component name = Component.literal(clanName + " territory").withStyle(Style.EMPTY.withColor(clanColor));
        String territoryName = pBlockEntity.getTerritoryName();

        String text = name.getString();

        int timeToReclaim = pBlockEntity.getTimeUntilRenewScent();

        Component toReclaimText;
        boolean isOnCooldown = false;
        if (timeToReclaim > 0) {
            isOnCooldown = true;
            toReclaimText = Component.literal(timeToReclaim/20 + "s").withStyle(ChatFormatting.YELLOW);
        } else {
            toReclaimText = Component.literal("Hold shift to remark scent").withStyle(ChatFormatting.GRAY);
        }

        int progressToClaim = pBlockEntity.getProgressToReclaim();
        ChatFormatting color = progressToClaim == 0 ? ChatFormatting.GRAY : ChatFormatting.GREEN;
        Component progressToClaimText = Component.literal(progressToClaim + "%").withStyle(color);

        float x = -font.width(name) / 2f;

        float x2 = -font.width(toReclaimText.getString()) / 2f;

        int y = territoryName.isEmpty() ? 40 : 30;
        if (!text.isEmpty()) {
            font.drawInBatch(name, x, y, 0xFFFFFFFF,
                    false, matrix, pBuffer, Font.DisplayMode.NORMAL, 0x44000000, pPackedLight);
            y += 10;

            if (!territoryName.isEmpty()) {
                float x3 = -font.width(territoryName) / 2f;
                font.drawInBatch(territoryName, x3, y, 0xFFFFFFFF,
                        false, matrix, pBuffer, Font.DisplayMode.NORMAL, 0x44000000, pPackedLight);
                y += 10;
            }
            font.drawInBatch(toReclaimText, x2, y, 0xFFFFFFFF,
                    false, matrix, pBuffer, Font.DisplayMode.NORMAL, 0x44000000, pPackedLight);
            y += 10;
            if (!isOnCooldown) {
                int x4 = -font.width(progressToClaimText)/2;
                font.drawInBatch(progressToClaimText, x4, y, 0xFFFFFFFF,
                        false, matrix, pBuffer, Font.DisplayMode.NORMAL, 0x44000000, pPackedLight);
            }


        }


        pPoseStack.popPose();
    }
}
