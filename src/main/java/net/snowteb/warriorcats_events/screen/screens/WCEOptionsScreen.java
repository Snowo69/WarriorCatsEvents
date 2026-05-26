package net.snowteb.warriorcats_events.screen.screens;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.skilltree.ReqSkillDataPacket;
import net.snowteb.warriorcats_events.screen.widgets.ButtonScrollList;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

public class WCEOptionsScreen extends Screen {
    private ButtonScrollList list;
    private Button close;


    private float menuX;
    private final float targetX = 0;


    public WCEOptionsScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        list = new ButtonScrollList(minecraft, 120, height - 60, 20, this.height - 20, 25);

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        list.addButton("Profile", () -> {
            player.connection.sendCommand("wce info profile");
        }, "Open your character profile, change your gender, your bio, and see your current health status.");
        list.addButton("Skill Tree", () -> {
            Minecraft.getInstance().setScreen(new SkillScreen());
            ModPackets.sendToServer(new ReqSkillDataPacket());
        }, "Access the Skill Tree. Improve your skills and abilities.");
        list.addButton("Edit Morph", () -> {
            Minecraft.getInstance().setScreen(new CreateMorphGeneticsScreen(false));
        }, "Create a new morph, edit your current morph, and manage your saved morphs.");
        list.addButton("Edit Character", () -> {
            player.connection.sendCommand("wce info setup");
        }, "Edit your character name, gender, and age.");
        list.addButton("Morph Pose", () -> {
            player.connection.sendCommand("wce info morphPose");
        }, "Edit your character current pose.");
        list.addButton("Manage Clan", () -> {
            player.connection.sendCommand("wce clan manage");
        }, "Manage your clan, change the name of your clan, and claim and unclaim territory.");
        list.addButton("Territory Map", () -> {
            player.connection.sendCommand("wce clan map");
        }, "See the territory map in the area you are.");
        list.addButton("My Clan", () -> {
            player.connection.sendCommand("wce clan");
        }, "Open your clan screen. See the clan logs, see the members, and the clan profile.");
        list.addButton("Clan List", () -> {
            player.connection.sendCommand("wce clan list");
        }, "See the clans that are currently in the server.");
        list.addButton("Register Clan", () -> {
            player.connection.sendCommand("wce clan register");
        }, "Register a clan of your own.");
        list.addButton("Changelog", () -> {
            Minecraft.getInstance().setScreen(new WCEChangelogScreen(this));
        }, "See the latest Warrior Cats Events changelog.");
        list.addButton("Config", () -> {
            Minecraft.getInstance().setScreen(new WCEConfigScreen(this));
        }, "Edit your client configuration.");


        list.setRenderSelection(false);
        list.setRenderTopAndBottom(true);
        list.setLeftPos(0);


        close = Button.builder(Component.literal("Close"), button -> {
            this.onClose();
        }).bounds(this.width - 65, this.height - 25, 60, 20)
                .build();

        this.addRenderableWidget(close);
        this.addRenderableWidget(list);
        menuX = -500;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (menuX < targetX) {
            menuX += -(menuX) * 0.05f;
            if (menuX > targetX - 0.3) menuX = targetX;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(menuX, 0, 0);

        fillGradientHorizontal(pGuiGraphics,119,0, 125, this.height, 0, 0xbb000000, 0x00000000);

        pGuiGraphics.pose().pushPose();
        float scale = 1.1f;
        pGuiGraphics.pose().translate(10, 5, 0);
        pGuiGraphics.pose().scale(scale, scale, scale);
        pGuiGraphics.drawString(this.font, "Options", 0, 0, 0xFFaaaaaa);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.enableScissor(0,0, (int) (120 + menuX),this.height);
        this.renderDirtBackground(pGuiGraphics);
        pGuiGraphics.fill(0,0,120,this.height, 0x33000000);
        pGuiGraphics.disableScissor();


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        if (pMouseY > 20 && pMouseX < this.height - 20) {
            this.list.renderButtonTooltip(pGuiGraphics, pMouseX, pMouseY + 5);
        }

        pGuiGraphics.pose().popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static void fillGradientHorizontal(GuiGraphics guiGraphics, int x1, int y1,
                                              int x2, int y2, int z,
                                              int colorLeft, int colorRight) {
        VertexConsumer consumer = guiGraphics.bufferSource()
                .getBuffer(RenderType.gui());

        Matrix4f matrix = guiGraphics.pose().last().pose();

        float a1 = (float) FastColor.ARGB32.alpha(colorLeft) / 255.0F;
        float r1 = (float) FastColor.ARGB32.red(colorLeft) / 255.0F;
        float g1 = (float) FastColor.ARGB32.green(colorLeft) / 255.0F;
        float b1 = (float) FastColor.ARGB32.blue(colorLeft) / 255.0F;

        float a2 = (float) FastColor.ARGB32.alpha(colorRight) / 255.0F;
        float r2 = (float) FastColor.ARGB32.red(colorRight) / 255.0F;
        float g2 = (float) FastColor.ARGB32.green(colorRight) / 255.0F;
        float b2 = (float) FastColor.ARGB32.blue(colorRight) / 255.0F;

        consumer.vertex(matrix, x1, y1, z).color(r1, g1, b1, a1).endVertex();
        consumer.vertex(matrix, x1, y2, z).color(r1, g1, b1, a1).endVertex();

        consumer.vertex(matrix, x2, y2, z).color(r2, g2, b2, a2).endVertex();
        consumer.vertex(matrix, x2, y1, z).color(r2, g2, b2, a2).endVertex();

        guiGraphics.flush();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E
                ||  pKeyCode == GLFW.GLFW_KEY_W
                || pKeyCode == GLFW.GLFW_KEY_A
                || pKeyCode == GLFW.GLFW_KEY_S
                || pKeyCode == GLFW.GLFW_KEY_D) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
