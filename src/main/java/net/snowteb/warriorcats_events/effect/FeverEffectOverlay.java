package net.snowteb.warriorcats_events.effect;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class FeverEffectOverlay {

    public static final LayeredDraw.Layer HUD_FEVER = ((pGuiGraphics, deltaTracker) -> {

        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if (player == null) return;
        if (mc.level == null) return;
        if (!player.hasEffect(ModEffects.FEVER)) return;

        ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                "textures/hud/fever.png");

        int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        float pulseAlpha = Mth.clamp(getAlpha(mc, deltaTracker.getGameTimeDeltaTicks()) * 2, 0.5f, 1f);
        float fadeOut = getRemainingAlpha(player);
        float finalAlpha = pulseAlpha * fadeOut;
        pGuiGraphics.setColor(1f, 1f, 1f, finalAlpha);

        pGuiGraphics.blit(
                overlay,
                0, 0,
                0, 0,
                width, height,
                width, height
        );
        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    });


    private static float getAlpha(Minecraft mc, float pPartialTicks) {
        float f2 = mc.options.darknessEffectScale().get().floatValue();
        float f3 = getDarknessGamma(pPartialTicks, mc) * f2;
        float f4 = calculateScale(mc.player, f3, pPartialTicks) * f2;

        return f4;
    }


    private static float getDarknessGamma(float pPartialTick, Minecraft mc) {
        if (mc.player.hasEffect(ModEffects.FEVER)) {
            MobEffectInstance mobeffectinstance = mc.player.getEffect(ModEffects.FEVER);
            if (mobeffectinstance != null) {
                return mobeffectinstance.getBlendFactor(mc.player, pPartialTick);
            }
        }
        return 0.0F;
    }

    private static float calculateScale(LivingEntity pEntity, float pGamma, float pPartialTick) {
        float f = 0.45F * pGamma;
        return Math.max(0.0F, Mth.cos(((float) pEntity.tickCount - pPartialTick) * (float) Math.PI * 0.025F) * f);
    }

    private static float getRemainingAlpha(LocalPlayer player) {
        MobEffectInstance effect = player.getEffect(ModEffects.FEVER);
        if (effect == null) return 0F;
        int fadeTicks = 40;
        int duration = effect.getDuration();
        if (duration >= fadeTicks) {
            return 1F;
        }
        return duration / (float) fadeTicks;
    }
}
