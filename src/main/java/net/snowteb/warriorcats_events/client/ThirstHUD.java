package net.snowteb.warriorcats_events.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ThirstHUD {

    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(WarriorCatsEvents.MODID,
            "textures/hud/filled.png");
    private static final ResourceLocation HALF_THIRST = new ResourceLocation(WarriorCatsEvents.MODID,
            "textures/hud/half.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(WarriorCatsEvents.MODID,
            "textures/hud/empty.png");

    public static final IGuiOverlay HUD_THIRST = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        var player = net.minecraft.client.Minecraft.getInstance().player;
        if (player == null || player.isCreative()) return;

        int x = screenWidth / 2 + 7;
        int y = screenHeight - 53;
        if (player.getAirSupply() < player.getMaxAirSupply()) {y -= 9;}

        int thirst = ClientThirstData.getPlayerThirst();
        int iconCount = 10;

        boolean lowThirst = thirst <= 6;
        boolean extraLowThirst = thirst <= 3;
        int tickCount = net.minecraft.client.Minecraft.getInstance().player.tickCount;

        for (int i = 0; i < iconCount; i++) {
            int index = iconCount - 1 - i;
            int thirstRemaining = thirst - index * 2;

            ResourceLocation texture;
            if (thirstRemaining >= 2) texture = FILLED_THIRST;
            else if (thirstRemaining == 1) texture = HALF_THIRST;
            else texture = EMPTY_THIRST;

            int yOffset = 0;
            if (lowThirst) {
                yOffset = (int)(Math.sin((tickCount + i) * 1.0) * 2);
            }
            if (extraLowThirst) {
                yOffset = (int)(Math.sin((tickCount + i) * 3.0) * 2);
            }

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
            guiGraphics.blit(texture, x + i * 8, y + yOffset, 0,0,14,14,14,14);
        }

    }
    );

}
