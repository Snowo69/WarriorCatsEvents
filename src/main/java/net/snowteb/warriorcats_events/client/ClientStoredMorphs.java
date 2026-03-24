package net.snowteb.warriorcats_events.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClientStoredMorphs {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path FILE_PATH =
            Minecraft.getInstance().gameDirectory.toPath().resolve("config")
                    .resolve("warriorcats_events").resolve("morphs.json");

    public static MorphsFile DATA = new MorphsFile();

    public static class MorphsFile {
        public Map<String,MorphData> morphs = new HashMap<>();

        public record MorphData(WCGenetics genetics, WCGenetics chimeraGenetics, WCGenetics.GeneticalVariants variants, WCGenetics.GeneticalChimeraVariants chimeraVariants){}
    }

    public static void load() {
        try {

            if (!Files.exists(FILE_PATH)) {
                Files.createDirectories(FILE_PATH.getParent());
                save();
                return;
            }

            Reader reader = Files.newBufferedReader(FILE_PATH);
            DATA = GSON.fromJson(reader, MorphsFile.class);
            if (DATA == null) {
                DATA = new MorphsFile();
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {

            Files.createDirectories(FILE_PATH.getParent());

            Writer writer = Files.newBufferedWriter(FILE_PATH);
            GSON.toJson(DATA, writer);
            writer.close();

        } catch (Exception e) {
            WarriorCatsEvents.LOGGER.debug("There was an error while saving the morphs. -" + e.getMessage());
        }
    }

    public static boolean add(String name, MorphsFile.MorphData data, boolean isOverWriting) {
        if (DATA.morphs.containsKey(name) && !isOverWriting) {
            return false;
        }
        DATA.morphs.put(name, data);
        save();
        return true;
    }

    public static boolean remove(String name) {
        if (!DATA.morphs.containsKey(name)) {
            return false;
        }
        DATA.morphs.remove(name);
        save();
        return true;
    }

}
