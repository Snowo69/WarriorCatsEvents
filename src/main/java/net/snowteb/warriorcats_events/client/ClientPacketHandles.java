package net.snowteb.warriorcats_events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.screen.menus.SetPoseMenu;
import net.snowteb.warriorcats_events.screen.screens.*;

import java.util.List;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandles {

    public static void openCatScreen(int catId) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        var entity = mc.level.getEntity(catId);

        if (entity instanceof WCatEntity cat) {
            mc.setScreen(new CatDataScreen(cat.getDisplayName(), cat));
        }
    }

    public static void openKitSpawnScreen() {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        mc.setScreen(new KitCreateScreen());
    }

    public static void openAncientStickScreen(List<Integer> entityIds, List<Integer> eagleIds) {
            Minecraft.getInstance().execute(() -> {
                Level level = Minecraft.getInstance().level;
                if (level == null) return;

                List<WCatEntity> cats = entityIds.stream()
                        .map(level::getEntity)
                        .filter(e -> e instanceof WCatEntity)
                        .map(e -> (WCatEntity) e)
                        .toList();

                List<EagleEntity> eagles = eagleIds.stream()
                        .map(level::getEntity)
                        .filter(e -> e instanceof EagleEntity)
                        .map(e -> (EagleEntity) e)
                        .toList();

                Minecraft.getInstance().setScreen(new AncientStickScreen(cats, eagles));
            });

    }

    public static void openClanCreateScreen(String clanName, String morphName) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            mc.setScreen(new CreateClanScreen(clanName, morphName));
        });

    }

    public static void openClanListScreen() {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ClanListScreen()));
    }

    public static void openManageClanScreen(ClanInfo info) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ManageClanScreen(info)));
    }

    public static void openSpecificClan(String clanName, UUID clanUUID) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            mc.setScreen(new SpecificClanScreen(clanName, clanUUID));
        });

    }

    public static void openCreateMorphScreen(boolean isSummoning) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            mc.setScreen(new CreateMorphGeneticsScreen(isSummoning));
        });

    }

    public static void openSaveMorphScreen(String key ,WCGenetics genetics, WCGenetics.GeneticalVariants variants, WCGenetics chimeraGenetics, WCGenetics.GeneticalChimeraVariants chimeraVariants) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;


            mc.setScreen(new SaveChatMorphScreen(key ,genetics, variants, chimeraGenetics, chimeraVariants));
        });

    }

    public static void openPatrolScreen(List<Integer> entityIds, UUID clanUUID, int deputyID) {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            List<WCatEntity> cats = entityIds.stream()
                    .map(mc.level::getEntity)
                    .filter(e -> e instanceof WCatEntity)
                    .map(e -> (WCatEntity) e)
                    .toList();

            mc.setScreen(new PatrolScreen(cats, clanUUID,  deputyID));
        });

    }


    public static void openPlayerCatScreen(WCEPlayerData.PackedData data, UUID targetUUID, int myKitCooldown) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        mc.setScreen(new PlayerCatDataScreen(data, targetUUID, myKitCooldown));

    }

    public static void openPoseMenu() {
        Minecraft.getInstance().execute(() -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new SetPoseMenu());
        });
    }
}

