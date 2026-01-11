package net.snowteb.warriorcats_events.clan;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.awt.*;
import java.util.UUID;

public class PlayerClanData {

    private String morphName = "";
    private String clanName = "";
    private String sufix = "";
    private String prefix = "";
    private int variantData = 0;
    private int genderData;
    private boolean firstLoginHandled = false;
    private boolean useSufixes = true;
    private UUID mateUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private BlockPos tempClickedPosData;
    private Component mateName = Component.literal("Undefined");

    public enum Age {
        KIT,
        APPRENTICE,
        ADULT
    }

    private Age morphAge = Age.ADULT;

    public BlockPos getTempClickedPosData() {
        return tempClickedPosData;
    }
    public void setTempClickedPosData(BlockPos tempClickedPosData) {
        this.tempClickedPosData = tempClickedPosData;
    }

    public Component getMateName() {
        return mateName;
    }
    public void setMateName(Component mateName) {
        this.mateName = mateName;
    }

    public UUID getMateUUID() {
        return mateUUID;
    }
    public void setMateUUID(UUID mateUUID) {
        this.mateUUID = mateUUID;
    }

    public String getMorphName() {
        return morphName;
    }
    public void setMorphName(String morphName) {
        this.morphName = morphName;
    }

    public String getClanName() {
        return clanName;
    }
    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public String getSufix() {
        return sufix;
    }
    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int  getVariantData() {
        return variantData;
    }
    public void setVariantData(int variantData) {
        this.variantData = variantData;
    }

    public int getGenderData() {
        return genderData;
    }
    public void setGenderData(int genderData) {
        this.genderData = genderData;
    }

    public Age getMorphAge() {
        return morphAge;
    }
    public void setMorphAge(Age age) {
        this.morphAge = age != null ? age : Age.ADULT;
    }

    public boolean isFirstLoginHandled() {
        return firstLoginHandled;
    }
    public void setFirstLoginHandled(boolean firstLoginHandled) {
        this.firstLoginHandled = firstLoginHandled;
    }

    public boolean isUseSufixes() {
        return useSufixes;
    }
    public void setUseSufixes(boolean useSufixes) {
        this.useSufixes = useSufixes;
    }



    public void copyFrom(PlayerClanData source) {
        this.clanName = source.clanName;
        this.variantData = source.variantData;
        this.genderData = source.genderData;
        this.morphName = source.morphName;
        this.morphAge = source.morphAge;
        this.firstLoginHandled = source.firstLoginHandled;
        this.prefix = source.prefix;
        this.useSufixes = source.useSufixes;
        this.sufix = source.sufix;
        this.mateUUID = source.mateUUID;
        this.mateName = source.mateName;
        this.tempClickedPosData = source.tempClickedPosData;
    }

    public void reset() {
        this.morphName = "<None>";
        this.clanName = "None";
        this.variantData = 0;
        this.genderData = 0;
        this.morphAge = Age.ADULT;
        this.firstLoginHandled = false;
        this.sufix = "None";
        this.prefix = "None";
        this.useSufixes = true;
        this.mateUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.mateName = Component.literal("Undefined");
        this.tempClickedPosData = null;
    }

//    public void tick() {
//        thirstDamageTimer++;
//    }
//
//    public int getThirstDamageTimer() {
//        return thirstDamageTimer;
//    }
//
//    public void resetThirstDamageTimer() {
//        thirstDamageTimer = 0;
//    }



    public void saveNBT(CompoundTag nbt) {
        nbt.putString("morphName", this.morphName);
        nbt.putString("clanName", this.clanName);
        nbt.putInt("variantData", this.variantData);
        nbt.putInt("genderData", this.genderData);
        nbt.putString("morphAge", this.morphAge.name());
        nbt.putBoolean("firstLoginHandled", this.firstLoginHandled);
        nbt.putString("sufix", this.sufix);
        nbt.putString("prefix", this.prefix);
        nbt.putBoolean("useSufixes", this.useSufixes);
        if (mateUUID != null) {
            nbt.putUUID("mateUUID", mateUUID);
        }

        nbt.putString("mateName", this.mateName != null ? this.mateName.getString() : "Undefined");

    }

    public void loadNBT(CompoundTag nbt) {
        morphName = nbt.getString("morphName");
        clanName = nbt.getString("clanName");
        variantData = nbt.getInt("variantData");
        genderData = nbt.getInt("genderData");
        if (nbt.contains("morphAge")) {
            try {
                this.morphAge = Age.valueOf(nbt.getString("morphAge"));
            } catch (IllegalArgumentException e) {
                this.morphAge = Age.ADULT;
            }
        } else {
            this.morphAge = Age.ADULT;
        }
        firstLoginHandled = nbt.getBoolean("firstLoginHandled");
        sufix = nbt.getString("sufix");
        prefix = nbt.getString("prefix");
        useSufixes = nbt.getBoolean("useSufixes");
        if (nbt.contains("mateUUID")) {
            try {
                mateUUID = nbt.getUUID("mateUUID");
            } catch (Exception e) {
                mateUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
            }
        } else {
            mateUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        }

        mateName = Component.literal(nbt.getString("mateName"));

    }
}
