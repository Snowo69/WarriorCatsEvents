package net.snowteb.warriorcats_events.entity.custom.wcat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;

import static net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity.ACCESSORY_FLAGS;

public class EmbeddedAccessoriesModule {

    private final WCatEntity cat;

    public EmbeddedAccessoriesModule(WCatEntity cat) {
        this.cat = cat;
    }

    // 0-7
    public static final int FLAG_SUNGLASSES = 1 << 0;
    public static final int FLAG_MOSS_COAT = 1 << 1;

    private boolean getAccessoryFlag(int bit) {
        return (cat.getEntityData().get(ACCESSORY_FLAGS) & bit) != 0;
    }

    private void setAccessoryFlag(int bit, boolean value) {
        byte current = cat.getEntityData().get(ACCESSORY_FLAGS);
        if (value) {
            current = (byte) (current | bit);
        } else {
            current = (byte) (current & ~bit);
        }
        cat.getEntityData().set(ACCESSORY_FLAGS, current);
    }


    public boolean hasSunGlasses() {
        return getAccessoryFlag(FLAG_SUNGLASSES);
    }
    public void setSunGlasses(boolean value) {
        setAccessoryFlag(FLAG_SUNGLASSES, value);
    }

    public boolean hasMossCoat() {
        return getAccessoryFlag(FLAG_MOSS_COAT);
    }
    public void setMossCoat(boolean value) {
        setAccessoryFlag(FLAG_MOSS_COAT, value);
    }










    public void defineSyncedData(SynchedEntityData.Builder builder) {
        builder.define(ACCESSORY_FLAGS, (byte) 0);
    }

    public void save(CompoundTag tag) {
        tag.putByte("AccessoryFlags", cat.getEntityData().get(ACCESSORY_FLAGS));
    }

    public void load(CompoundTag tag) {
        if (tag.contains("AccessoryFlags")) {
            cat.getEntityData().set(ACCESSORY_FLAGS, tag.getByte("AccessoryFlags"));
        }
    }

}
