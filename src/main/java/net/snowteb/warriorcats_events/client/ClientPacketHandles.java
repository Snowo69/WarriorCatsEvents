package net.snowteb.warriorcats_events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.screen.AncientStickScreen;
import net.snowteb.warriorcats_events.screen.clandata.CatDataScreen;
import net.snowteb.warriorcats_events.screen.clandata.KitCreateScreen;

import java.util.List;

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

    public static void openAncientStickScreen(List<Integer> entityIds) {
            Minecraft.getInstance().execute(() -> {
                Level level = Minecraft.getInstance().level;
                if (level == null) return;

                List<WCatEntity> cats = entityIds.stream()
                        .map(level::getEntity)
                        .filter(e -> e instanceof WCatEntity)
                        .map(e -> (WCatEntity) e)
                        .toList();

                Minecraft.getInstance().setScreen(new AncientStickScreen(cats));
            });

    }
}

