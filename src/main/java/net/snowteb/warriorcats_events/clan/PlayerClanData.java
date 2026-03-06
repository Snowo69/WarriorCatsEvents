package net.snowteb.warriorcats_events.clan;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;

import java.util.UUID;

public class PlayerClanData {

    private String morphName = "None";
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
    private int sleepingCooldown = 0;

    private UUID currentClanUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");


    public enum Age {
        KIT,
        APPRENTICE,
        ADULT
    }

    private Age morphAge = Age.ADULT;


    // GENETICS

    private boolean isOnGeneticalSkin = false;

    private String chestFur = "s-s";
    private String bellyFur = "s-s";
    private String legsFur = "s-s";
    private String headFur = "s-s";
    private String cheekFur = "s-s";
    private String backFur = "s-s";
    private String tailFur = "s-s";
    private String bobtail = "B-b";

    private String base = "B-b";
    private String orangeBase = "o-o";
    private String whiteRatio = "w-w";
    private String albino = "C-cs";
    private String dilute = "d-d";
    private String agouti = "a-a";
    private String tabbyStripes = "mc-mc";
    private String eyesAnomaly = "H-h";
    private int rufousing = 0;
    private int blueRufousing = 0;
    private int noise = 0;
    private float size = 0;

    private String eyeColorLeft = "green";
    private String eyeColorRight = "green";
    private int rufousingVariant = 0;
    private int blueRufousingVariant = 0;
    private int orangeVar = 0;
    private int whiteVar = 0;
    private int tabbyVar = 0;
    private int albinoVar = 0;
    private int leftEyeVar = 0;
    private int rightEyeVar = 0;

    private WCGenetics mateGenetics = new WCGenetics();

    private WCGenetics chimeraPlayerGenetics = new WCGenetics();
    private WCGenetics.GeneticalChimeraVariants chimeraPlayerVariants = new WCGenetics.GeneticalChimeraVariants();

    public void setMateGenetics(WCGenetics mateGenetics) {
        this.mateGenetics = mateGenetics;
    }
    public WCGenetics getMateGenetics() {
        return mateGenetics;
    }

    public void setPlayerGenetics(WCGenetics genetics) {

        if (!genetics.chestFur.isEmpty()) this.chestFur = genetics.chestFur;
        if (!genetics.bellyFur.isEmpty()) this.bellyFur = genetics.bellyFur;
        if (!genetics.legsFur.isEmpty()) this.legsFur = genetics.legsFur;
        if (!genetics.headFur.isEmpty()) this.headFur = genetics.headFur;
        if (!genetics.cheekFur.isEmpty()) this.cheekFur = genetics.cheekFur;
        if (!genetics.backFur.isEmpty()) this.backFur = genetics.backFur;
        if (!genetics.tailFur.isEmpty()) this.tailFur = genetics.tailFur;
        if (!genetics.bobtail.isEmpty()) this.bobtail = genetics.bobtail;

        if (!genetics.base.isEmpty()) this.base = genetics.base;
        if (!genetics.orangeBase.isEmpty()) this.orangeBase = genetics.orangeBase;
        if (!genetics.whiteRatio.isEmpty()) this.whiteRatio = genetics.whiteRatio;
        if (!genetics.albino.isEmpty()) this.albino = genetics.albino;
        if (!genetics.dilute.isEmpty()) this.dilute = genetics.dilute;
        if (!genetics.agouti.isEmpty()) this.agouti = genetics.agouti;
        if (!genetics.tabbyStripes.isEmpty()) this.tabbyStripes = genetics.tabbyStripes;
        if (!genetics.eyesAnomaly.isEmpty()) this.eyesAnomaly = genetics.eyesAnomaly;

        this.rufousing = genetics.rufousing;
        this.blueRufousing = genetics.blueRufousing;
        this.noise = genetics.noise;

    }


    public void setPlayerChimeraVariants(WCGenetics.GeneticalChimeraVariants variants) {
        this.chimeraPlayerVariants = variants;
    }
    public WCGenetics.GeneticalChimeraVariants getPlayerChimeraVariants() {
        return chimeraPlayerVariants;
    }

    public void setPlayerChimeraGenetics(WCGenetics gens) {
        this.chimeraPlayerGenetics = gens;
    }
    public WCGenetics getPlayerChimeraGenetics() {
        return chimeraPlayerGenetics;
    }

    public void setPlayerGeneticalVariants(String eyesVariantLeft, String eyesVariantRight,
                                           int rufVar, int blueRufVar,
                                           int orangeVar, int whiteVar, int tabbyVar,
                                           int albinoVar, int leftEyeVar, int rightEyeVar, int noise, float size) {

        if (!eyesVariantLeft.isEmpty()) this.eyeColorLeft = eyesVariantLeft;
        if (!eyesVariantRight.isEmpty()) this.eyeColorRight = eyesVariantRight;
        this.rufousingVariant = rufVar;
        this.blueRufousingVariant = blueRufVar;
        this.orangeVar = orangeVar;
        this.whiteVar = whiteVar;
        this.tabbyVar = tabbyVar;
        this.albinoVar = albinoVar;
        this.leftEyeVar = leftEyeVar;
        this.rightEyeVar = rightEyeVar;
        this.noise = noise;
        this.size = size;
    }

    public void setPlayerGeneticalVariants(WCGenetics.GeneticalVariants genetics) {

        if (!genetics.eyeColorLeft.isEmpty()) this.eyeColorLeft = genetics.eyeColorLeft;
        if (!genetics.eyeColorRight.isEmpty()) this.eyeColorRight = genetics.eyeColorRight;
        this.rufousingVariant = genetics.rufousingVariant;
        this.blueRufousingVariant = genetics.blueRufousingVariant;
        this.orangeVar = genetics.orangeVar;
        this.whiteVar = genetics.whiteVar;
        this.tabbyVar = genetics.tabbyVar;
        this.albinoVar = genetics.albinoVar;
        this.leftEyeVar = genetics.leftEyeVar;
        this.rightEyeVar = genetics.rightEyeVar;
        this.noise = genetics.noise;
        this.size = genetics.size;
    }

    public WCGenetics getPlayerGenetics() {
        return new WCGenetics(bobtail, chestFur, bellyFur,
                legsFur, headFur, cheekFur, tailFur, backFur, base, orangeBase, whiteRatio, albino,
                dilute, agouti, tabbyStripes, eyesAnomaly, rufousing, blueRufousing, noise, chimeraPlayerVariants.chimeraGene);
    }

    public WCGenetics.GeneticalVariants getPlayerGeneticalVariants() {
        return new WCGenetics.GeneticalVariants(eyeColorLeft, eyeColorRight, rufousingVariant,
                blueRufousingVariant, orangeVar, whiteVar, tabbyVar, albinoVar, leftEyeVar, rightEyeVar, noise, size);
    }

    public boolean isOnGeneticalSkin() {
        return isOnGeneticalSkin;
    }

    public void setOnGeneticalSkin(boolean onGeneticalSkin) {
        isOnGeneticalSkin = onGeneticalSkin;
    }


    // GENETICS

    public UUID getCurrentClanUUID() {
        return currentClanUUID;
    }

    public void setCurrentClanUUID(UUID currentClanUUID) {
        this.currentClanUUID = currentClanUUID;
    }

    public int getSleepingCooldown() {
        return sleepingCooldown;
    }

    public void setSleepingCooldown(int sleepingCooldown) {
        this.sleepingCooldown = sleepingCooldown;
    }

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

    public int getVariantData() {
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
        this.currentClanUUID = source.currentClanUUID;

        this.isOnGeneticalSkin = source.isOnGeneticalSkin;

        this.chestFur = source.chestFur;
        this.bellyFur = source.bellyFur;
        this.legsFur = source.legsFur;
        this.headFur = source.headFur;
        this.cheekFur = source.cheekFur;
        this.backFur = source.backFur;
        this.tailFur = source.tailFur;
        this.bobtail = source.bobtail;

        this.base = source.base;
        this.orangeBase = source.orangeBase;
        this.whiteRatio = source.whiteRatio;
        this.albino = source.albino;
        this.dilute = source.dilute;
        this.agouti = source.agouti;
        this.tabbyStripes = source.tabbyStripes;
        this.eyesAnomaly = source.eyesAnomaly;

        this.rufousing = source.rufousing;
        this.blueRufousing = source.blueRufousing;
        this.noise = source.noise;
        this.size = source.size;

        this.eyeColorLeft = source.eyeColorLeft;
        this.eyeColorRight = source.eyeColorRight;

        this.rufousingVariant = source.rufousingVariant;
        this.blueRufousingVariant = source.blueRufousingVariant;
        this.orangeVar = source.orangeVar;
        this.whiteVar = source.whiteVar;
        this.tabbyVar = source.tabbyVar;
        this.albinoVar = source.albinoVar;
        this.leftEyeVar = source.leftEyeVar;
        this.rightEyeVar = source.rightEyeVar;
        this.mateGenetics = source.mateGenetics;

        this.chimeraPlayerVariants = source.chimeraPlayerVariants;
        this.chimeraPlayerGenetics = source.chimeraPlayerGenetics;

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
        this.sleepingCooldown = 0;
        this.mateGenetics = new WCGenetics();

        setDefaultGenetics();
    }

    public void setDefaultGenetics() {

        this.isOnGeneticalSkin = false;

        this.chestFur = "s-s";
        this.bellyFur = "s-s";
        this.legsFur = "s-s";
        this.headFur = "s-s";
        this.cheekFur = "s-s";
        this.backFur = "s-s";
        this.tailFur = "s-s";
        this.bobtail = "B-b";

        this.base = "B-b";
        this.orangeBase = "o-o";
        this.whiteRatio = "w-w";
        this.albino = "C-cs";
        this.dilute = "d-d";
        this.agouti = "a-a";
        this.tabbyStripes = "mc-mc";
        this.eyesAnomaly = "H-h";

        this.rufousing = 0;
        this.blueRufousing = 0;
        this.noise = 0;
        this.size = 0;

        this.eyeColorLeft = "green";
        this.eyeColorRight = "green";

        this.rufousingVariant = 0;
        this.blueRufousingVariant = 0;
        this.orangeVar = 0;
        this.whiteVar = 0;
        this.tabbyVar = 0;
        this.albinoVar = 0;
        this.leftEyeVar = 0;
        this.rightEyeVar = 0;

        this.chimeraPlayerGenetics = new  WCGenetics();
        this.chimeraPlayerVariants = new WCGenetics.GeneticalChimeraVariants();
    }

    public void tick() {
        if (sleepingCooldown > 0) {
            sleepingCooldown--;
        }
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
        if (currentClanUUID != null) {
            nbt.putUUID("clanUUID", currentClanUUID);
        }

        nbt.putString("mateName", this.mateName != null ? this.mateName.getString() : "Undefined");


        CompoundTag geneticsTag = new CompoundTag();

        geneticsTag.putBoolean("Genetical", isOnGeneticalSkin);

        geneticsTag.putString("ChestFur", chestFur);
        geneticsTag.putString("BellyFur", bellyFur);
        geneticsTag.putString("LegsFur", legsFur);
        geneticsTag.putString("HeadFur", headFur);
        geneticsTag.putString("CheekFur", cheekFur);
        geneticsTag.putString("BackFur", backFur);
        geneticsTag.putString("TailFur", tailFur);
        geneticsTag.putString("Bobtail", bobtail);

        geneticsTag.putString("Base", base);
        geneticsTag.putString("OrangeBase", orangeBase);
        geneticsTag.putString("WhiteRatio", whiteRatio);
        geneticsTag.putString("Albino", albino);
        geneticsTag.putString("Dilute", dilute);
        geneticsTag.putString("Agouti", agouti);
        geneticsTag.putString("TabbyStripes", tabbyStripes);
        geneticsTag.putString("EyeColorLeft", eyeColorLeft);
        geneticsTag.putString("EyeColorRight", eyeColorRight);
        geneticsTag.putString("EyesAnomaly", eyesAnomaly);

        geneticsTag.putInt("Rufousing", rufousingVariant);
        geneticsTag.putInt("BlueRufousing", blueRufousingVariant);
        geneticsTag.putInt("OrangeBaseVariant", orangeVar);
        geneticsTag.putInt("WhiteRatioVariant", whiteVar);
        geneticsTag.putInt("AlbinoVariant", albinoVar);
        geneticsTag.putInt("TabbyStripesVariant", tabbyVar);
        geneticsTag.putInt("EyeColorVariantLeft", leftEyeVar);
        geneticsTag.putInt("EyeColorVariantRight", rightEyeVar);
        geneticsTag.putInt("Noise", noise);
        geneticsTag.putFloat("Size", size);

        geneticsTag.putString("BaseChimera", chimeraPlayerGenetics.base);
        geneticsTag.putString("OrangeBaseChimera", chimeraPlayerGenetics.orangeBase);
        geneticsTag.putString("WhiteRatioChimera", chimeraPlayerGenetics.whiteRatio);
        geneticsTag.putString("AlbinoChimera", chimeraPlayerGenetics.albino);
        geneticsTag.putString("DiluteChimera", chimeraPlayerGenetics.dilute);
        geneticsTag.putString("AgoutiChimera", chimeraPlayerGenetics.agouti);
        geneticsTag.putString("TabbyStripesChimera", chimeraPlayerGenetics.tabbyStripes);

        geneticsTag.putInt("RufousingChimera", chimeraPlayerGenetics.rufousing);
        geneticsTag.putInt("BlueRufousingChimera", chimeraPlayerGenetics.blueRufousing);
        geneticsTag.putInt("OrangeBaseVariantChimera", chimeraPlayerVariants.orangeVar);
        geneticsTag.putInt("WhiteRatioVariantChimera", chimeraPlayerVariants.whiteVar);
        geneticsTag.putInt("AlbinoVariantChimera", chimeraPlayerVariants.albinoVar);
        geneticsTag.putInt("TabbyStripesVariantChimera", chimeraPlayerVariants.tabbyVar);
        geneticsTag.putInt("NoiseChimera", chimeraPlayerVariants.noise);
        geneticsTag.putString("ChimeraGene", chimeraPlayerGenetics.chimeraGene);

        nbt.put("Genetics", geneticsTag);


        if (this.mateGenetics != null) {
            CompoundTag mateTag = new CompoundTag();
            saveMateGenetics(mateTag, this.mateGenetics);
            nbt.put("MateGenetics", mateTag);
        }

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

        if (nbt.contains("clanUUID")) {
            try {
                currentClanUUID = nbt.getUUID("clanUUID");
            } catch (Exception e) {
                currentClanUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
            }
        } else {
            currentClanUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
        }


        mateName = Component.literal(nbt.getString("mateName"));

        if (nbt.contains("Genetics")) {
            CompoundTag geneticsTag = nbt.getCompound("Genetics");

            this.setOnGeneticalSkin(geneticsTag.getBoolean("Genetical"));

            WCGenetics genetics = new WCGenetics(
                    geneticsTag.getString("Bobtail"),
                    geneticsTag.getString("ChestFur"),
                    geneticsTag.getString("BellyFur"),
                    geneticsTag.getString("LegsFur"),
                    geneticsTag.getString("HeadFur"),
                    geneticsTag.getString("CheekFur"),
                    geneticsTag.getString("TailFur"),
                    geneticsTag.getString("BackFur"),

                    geneticsTag.getString("Base"),
                    geneticsTag.getString("OrangeBase"),
                    geneticsTag.getString("WhiteRatio"),
                    geneticsTag.getString("Albino"),
                    geneticsTag.getString("Dilute"),
                    geneticsTag.getString("Agouti"),
                    geneticsTag.getString("TabbyStripes"),
                    geneticsTag.getString("EyesAnomaly"),

                    geneticsTag.getInt("Rufousing"),
                    geneticsTag.getInt("BlueRufousing"),
                    geneticsTag.getInt("Noise"),
                    geneticsTag.getString("ChimeraGene")
            );

            this.setPlayerGenetics(genetics);




            WCGenetics chimeraGens = new  WCGenetics();

            chimeraGens.base = geneticsTag.getString("BaseChimera");
            chimeraGens.orangeBase = geneticsTag.getString("OrangeBaseChimera");
            chimeraGens.whiteRatio = geneticsTag.getString("WhiteRatioChimera");
            chimeraGens.albino = geneticsTag.getString("AlbinoChimera");
            chimeraGens.dilute = geneticsTag.getString("DiluteChimera");
            chimeraGens.agouti = geneticsTag.getString("AgoutiChimera");
            chimeraGens.tabbyStripes = geneticsTag.getString("TabbyStripesChimera");
            chimeraGens.chimeraGene = geneticsTag.getString("ChimeraGene");

            this.setPlayerChimeraGenetics(chimeraGens);

            WCGenetics.GeneticalChimeraVariants chimeraVariants = new WCGenetics.GeneticalChimeraVariants();

            chimeraVariants.rufousingVariant = geneticsTag.getInt("RufousingChimera");
            chimeraVariants.blueRufousingVariant = geneticsTag.getInt("BlueRufousingChimera");
            chimeraVariants.orangeVar = geneticsTag.getInt("OrangeBaseVariantChimera");
            chimeraVariants.whiteVar = geneticsTag.getInt("WhiteRatioVariantChimera");
            chimeraVariants.albinoVar = geneticsTag.getInt("AlbinoVariantChimera");
            chimeraVariants.tabbyVar = geneticsTag.getInt("TabbyStripesVariantChimera");
            chimeraVariants.noise = geneticsTag.getInt("NoiseChimera");

            this.setPlayerChimeraVariants(chimeraVariants);


            eyeColorLeft = geneticsTag.getString("EyeColorLeft");
            eyeColorRight = geneticsTag.getString("EyeColorRight");

            orangeVar = geneticsTag.getInt("OrangeBaseVariant");
            whiteVar = geneticsTag.getInt("WhiteRatioVariant");
            albinoVar = geneticsTag.getInt("AlbinoVariant");
            tabbyVar = geneticsTag.getInt("TabbyStripesVariant");
            leftEyeVar = geneticsTag.getInt("EyeColorVariantLeft");
            rightEyeVar = geneticsTag.getInt("EyeColorVariantRight");
            size = geneticsTag.getFloat("Size");
        }

        if (nbt.contains("MateGenetics")) {

            CompoundTag mateTag = nbt.getCompound("MateGenetics");

            this.mateGenetics = new WCGenetics(
                    mateTag.getString("Bobtail"),
                    mateTag.getString("ChestFur"),
                    mateTag.getString("BellyFur"),
                    mateTag.getString("LegsFur"),
                    mateTag.getString("HeadFur"),
                    mateTag.getString("CheekFur"),
                    mateTag.getString("TailFur"),
                    mateTag.getString("BackFur"),

                    mateTag.getString("Base"),
                    mateTag.getString("Orange"),
                    mateTag.getString("WhiteRatio"),
                    mateTag.getString("Albino"),
                    mateTag.getString("Dilute"),
                    mateTag.getString("Agouti"),
                    mateTag.getString("TabbyStripes"),
                    mateTag.getString("EyesAnomaly"),
                    mateTag.getInt("Rufousing"),
                    mateTag.getInt("BlueRufousing"),
                    mateTag.getInt("Noise"),
                    mateTag.getString("ChimeraGene")
            );
        }

    }

    private void saveMateGenetics(CompoundTag tag, WCGenetics genetics) {

        tag.putString("ChestFur", genetics.chestFur);
        tag.putString("BellyFur", genetics.bellyFur);
        tag.putString("LegsFur", genetics.legsFur);
        tag.putString("HeadFur", genetics.headFur);
        tag.putString("CheekFur", genetics.cheekFur);
        tag.putString("BackFur", genetics.backFur);
        tag.putString("TailFur", genetics.tailFur);
        tag.putString("Bobtail", genetics.bobtail);

        tag.putString("Base", genetics.base);
        tag.putString("Orange", genetics.orangeBase);
        tag.putString("WhiteRatio", genetics.whiteRatio);
        tag.putString("Albino", genetics.albino);
        tag.putString("Dilute", genetics.dilute);
        tag.putString("Agouti", genetics.agouti);
        tag.putString("TabbyStripes", genetics.tabbyStripes);
        tag.putString("EyesAnomaly", genetics.eyesAnomaly);
        tag.putInt("Rufousing", genetics.rufousing);
        tag.putInt("BlueRufousing", genetics.blueRufousing);
        tag.putInt("Noise", genetics.noise);
        tag.putString("ChimeraGene", genetics.chimeraGene);

    }
}
