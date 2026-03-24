package net.snowteb.warriorcats_events.util;

import net.snowteb.warriorcats_events.entity.custom.WCGenetics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerPlayerMorphsCache {
    private static Map<String, ServerMorphData> MORPHS = new HashMap<>();

    public static Map<String, ServerMorphData> getMorphsCache() {
        return MORPHS;
    }

    public static class ServerMorphData {
        public WCGenetics genetics;
        public WCGenetics chimeraGenetics;
        public WCGenetics.GeneticalVariants variants;
        public WCGenetics.GeneticalChimeraVariants chimeraVariants;
        public int time;

        public ServerMorphData(WCGenetics genetics, WCGenetics chimeraGenetics, WCGenetics.GeneticalVariants variants, WCGenetics.GeneticalChimeraVariants chimeraVariants, int time) {

            this.genetics = genetics;
            this.chimeraGenetics = chimeraGenetics;
            this.variants = variants;
            this.chimeraVariants = chimeraVariants;
            this.time = time;
        }
    }

    public static void add(String key, ServerMorphData morphData) {
        MORPHS.put(key, morphData);
    }

    public static void tick() {
        Iterator<Map.Entry<String, ServerMorphData>> iterator = MORPHS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, ServerMorphData> entry = iterator.next();

            if (entry.getValue().time > 0) {
                entry.getValue().time--;
            } else {
                iterator.remove();
            }
        }
    }

}
