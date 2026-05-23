package net.snowteb.warriorcats_events.diseases;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiseaseRegistry {

    private static final Map<String, DiseaseType<? extends Disease<?>>> DISEASES = new HashMap<>();

    public static <T extends Disease<T>> DiseaseType<T> register(DiseaseType<T> type) {
        if (DISEASES.containsKey(type.getID())) {
            throw new RuntimeException(type.getID() + " is a duplicate.");
        }
        DISEASES.put(type.getID(), type);
        return type;
    }

    public static DiseaseType<? extends Disease<?>> getByID(String id) {
        return DISEASES.get(id);
    }

    public static Map<String, DiseaseType<? extends Disease<?>>> getList() {
        return DISEASES;
    }

    public static void init() {
        List<ModFileScanData> scanDataList = ModList.get().getAllScanData();
        for (ModFileScanData scanData : scanDataList) {
            for (ModFileScanData.AnnotationData annotation : scanData.getAnnotations()) {
                if (annotation.annotationType().getClassName().equals(WCEDiseaseTypes.class.getName())) {
                    String className = annotation.clazz().getClassName();
                    try {
                        Class.forName(className);
                        WarriorCatsEvents.LOGGER.info("Loaded disease registry class {}", className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
