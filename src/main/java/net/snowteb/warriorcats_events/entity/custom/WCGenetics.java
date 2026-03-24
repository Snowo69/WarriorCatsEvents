package net.snowteb.warriorcats_events.entity.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;

public class WCGenetics {


    public class Values {
        public static final int MAX_ORANGE_VARIANTS = 11;
        public static final int MAX_WHITE_VARIANTS = 21;
        public static final int MAX_ALBINO_VARIANTS = 3;
        public static final int MAX_TABBY_VARIANTS = 5;
        public static final int MAX_EYE_VARIANTS = 11;
        public static final int MAX_NOISE_VARIANTS = 3;
        public static final int MAX_RUFOUSING_VARIANTS = 7;
        public static final int MAX_BLUE_RUFOUSING_VARIANTS = 7;

        public static final int MAX_CHIMERISM_VARIANTS = 8;
    }

    public String chestFur = "s-s";
    public String bellyFur = "s-s";
    public String legsFur = "s-s";
    public String headFur = "s-s";
    public String cheekFur = "s-s";
    public String backFur = "s-s";
    public String tailFur = "s-s";
    public String bobtail = "B-B";

    public String base = "B-b";
    public String orangeBase = "o-o";
    public String whiteRatio = "w-w";
    public String albino = "C-cs";
    public String dilute = "d-d";
    public String agouti = "a-a";
    public String tabbyStripes = "mc-mc";
    public String eyesAnomaly = "H-h";

    public int rufousing = 0;
    public int blueRufousing = 0;
    public int noise = 0;

    public String chimeraGene = "C-C";


    public WCGenetics(WCGenetics copy) {
        this.chestFur = copy.chestFur;
        this.bellyFur = copy.bellyFur;
        this.legsFur = copy.legsFur;
        this.headFur = copy.headFur;
        this.cheekFur = copy.cheekFur;
        this.backFur =  copy.backFur;
        this.tailFur = copy.tailFur;
        this.bobtail = copy.bobtail;

        this.base = copy.base;
        this.orangeBase = copy.orangeBase;
        this.whiteRatio = copy.whiteRatio;
        this.albino = copy.albino;
        this.dilute = copy.dilute;
        this.agouti = copy.agouti;
        this.tabbyStripes = copy.tabbyStripes;
        this.eyesAnomaly = copy.eyesAnomaly;
        this.rufousing = copy.rufousing;
        this.blueRufousing = copy.blueRufousing;
        this.noise = copy.noise;

        this.chimeraGene = copy.chimeraGene;
    }

    public WCGenetics(String bobtail, String chestFur, String bellyFur,
                      String legsFur, String headFur, String cheekFur,
                      String tailFur, String backFur, String base,
                      String orangeBase, String whiteRatio, String albino, String dilute,
                      String agouti, String tabbyStripes, String eyesAnomaly,
                      int rufousing, int blueRufousing, int noise, String chimeraGene) {
        this.chestFur = chestFur;
        this.bellyFur = bellyFur;
        this.legsFur = legsFur;
        this.headFur = headFur;
        this.cheekFur = cheekFur;
        this.backFur =  backFur;
        this.tailFur = tailFur;
        this.bobtail = bobtail;

        this.base = base;
        this.orangeBase = orangeBase;
        this.whiteRatio = whiteRatio;
        this.albino = albino;
        this.dilute = dilute;
        this.agouti = agouti;
        this.tabbyStripes = tabbyStripes;
        this.eyesAnomaly = eyesAnomaly;
        this.rufousing = rufousing;
        this.blueRufousing = blueRufousing;
        this.noise = noise;

        this.chimeraGene = chimeraGene;
    }

    public WCGenetics() {
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.bobtail);
        buf.writeUtf(this.chestFur);
        buf.writeUtf(this.bellyFur);
        buf.writeUtf(this.legsFur);
        buf.writeUtf(this.headFur);
        buf.writeUtf(this.cheekFur);
        buf.writeUtf(this.tailFur);
        buf.writeUtf(this.backFur);

        buf.writeUtf(this.base);
        buf.writeUtf(this.orangeBase);
        buf.writeUtf(this.whiteRatio);
        buf.writeUtf(this.albino);
        buf.writeUtf(this.dilute);
        buf.writeUtf(this.agouti);
        buf.writeUtf(this.tabbyStripes);
        buf.writeUtf(this.eyesAnomaly);

        buf.writeInt(this.rufousing);
        buf.writeInt(this.blueRufousing);
        buf.writeInt(this.noise);

        buf.writeUtf(this.chimeraGene);
    }

    public static WCGenetics decode(FriendlyByteBuf buf) {
        return new WCGenetics(
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),

                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),
                buf.readUtf(),

                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readUtf()
        );
    }





    public static class GeneticalVariants {
        public String eyeColorLeft = "yellow";
        public String eyeColorRight = "yellow";
        public int rufousingVariant = 0;
        public int blueRufousingVariant = 0;
        public int orangeVar = 0;
        public int whiteVar = 0;
        public int tabbyVar = 0;
        public int albinoVar = 0;
        public int leftEyeVar = 0;
        public int rightEyeVar = 0;
        public int noise = 0;
        public float size = 0;

        public GeneticalVariants() {

        }

        public GeneticalVariants(String eyeColorLeft, String eyeColorRight, int rufousingVariant,
                                 int blueRufousingVariant, int orangeVar, int whiteVar,
                                 int tabbyVar, int albinoVar, int leftEyeVar, int rightEyeVar, int noise, float size) {
            this.eyeColorLeft = eyeColorLeft;
            this.eyeColorRight = eyeColorRight;
            this.rufousingVariant = rufousingVariant;
            this.blueRufousingVariant = blueRufousingVariant;
            this.orangeVar = orangeVar;
            this.whiteVar = whiteVar;
            this.tabbyVar = tabbyVar;
            this.albinoVar = albinoVar;
            this.leftEyeVar = leftEyeVar;
            this.rightEyeVar = rightEyeVar;
            this.noise = noise;
            this.size = size;
        }

        public GeneticalVariants(GeneticalVariants copy) {
            this.eyeColorLeft = copy.eyeColorLeft;
            this.eyeColorRight = copy.eyeColorRight;
            this.rufousingVariant = copy.rufousingVariant;
            this.blueRufousingVariant = copy.blueRufousingVariant;
            this.orangeVar = copy.orangeVar;
            this.whiteVar = copy.whiteVar;
            this.tabbyVar = copy.tabbyVar;
            this.albinoVar = copy.albinoVar;
            this.leftEyeVar = copy.leftEyeVar;
            this.rightEyeVar = copy.rightEyeVar;
            this.noise = copy.noise;
            this.size = copy.size;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(this.eyeColorLeft);
            buf.writeUtf(this.eyeColorRight);

            buf.writeInt(this.rufousingVariant);
            buf.writeInt(this.blueRufousingVariant);
            buf.writeInt(this.orangeVar);
            buf.writeInt(this.whiteVar);
            buf.writeInt(this.tabbyVar);
            buf.writeInt(this.albinoVar);
            buf.writeInt(this.leftEyeVar);
            buf.writeInt(this.rightEyeVar);
            buf.writeInt(this.noise);
            buf.writeFloat(this.size);
        }
        public static GeneticalVariants decode(FriendlyByteBuf buf) {
            return new GeneticalVariants(
                    buf.readUtf(),
                    buf.readUtf(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readFloat()
            );
        }
    }

    public static class GeneticalChimeraVariants {
        public int rufousingVariant = 0;
        public int blueRufousingVariant = 0;
        public int orangeVar = 0;
        public int whiteVar = 0;
        public int tabbyVar = 0;
        public int albinoVar = 0;
        public int noise = 0;

        public int chimeraVariant = 0;

        public String chimeraGene = "C-C";

        public GeneticalChimeraVariants() {

        }

        public GeneticalChimeraVariants(int chimeraVariant ,int rufousingVariant,
                                        int blueRufousingVariant, int orangeVar, int whiteVar,
                                        int tabbyVar, int albinoVar, int noise, String chimeraGene) {
            this.rufousingVariant = rufousingVariant;
            this.blueRufousingVariant = blueRufousingVariant;
            this.orangeVar = orangeVar;
            this.whiteVar = whiteVar;
            this.chimeraVariant = chimeraVariant;
            this.tabbyVar = tabbyVar;
            this.albinoVar = albinoVar;
            this.noise = noise;
            this.chimeraGene = chimeraGene;
        }

        public GeneticalChimeraVariants(GeneticalChimeraVariants copy) {
            this.rufousingVariant = copy.rufousingVariant;
            this.blueRufousingVariant = copy.blueRufousingVariant;
            this.orangeVar = copy.orangeVar;
            this.whiteVar = copy.whiteVar;
            this.chimeraVariant = copy.chimeraVariant;
            this.tabbyVar = copy.tabbyVar;
            this.albinoVar = copy.albinoVar;
            this.noise = copy.noise;
            this.chimeraGene = copy.chimeraGene;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(this.chimeraVariant);
            buf.writeInt(this.rufousingVariant);
            buf.writeInt(this.blueRufousingVariant);
            buf.writeInt(this.orangeVar);
            buf.writeInt(this.whiteVar);
            buf.writeInt(this.tabbyVar);
            buf.writeInt(this.albinoVar);
            buf.writeInt(this.noise);
            buf.writeUtf(this.chimeraGene);

        }
        public static GeneticalChimeraVariants decode(FriendlyByteBuf buf) {
            return new GeneticalChimeraVariants(
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readUtf()
            );
        }
    }


    public enum Bobtail {
        FULL("B"),
        BOBTAIL("b");

        private final String allele;

        Bobtail(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static String init(RandomSource random) {
            String a1 = random.nextFloat() < 0.70F ? FULL.allele : BOBTAIL.allele;
            String a2 = random.nextFloat() < 0.70F ? FULL.allele : BOBTAIL.allele;
            return a1 + "-" + a2;
        }

        public static boolean isBobtail(String alleles) {
            return alleles.equals("b-b");
        }
    }

    public enum FurGene {

        DOMINANT("L"),
        RECESSIVE("s");

        private final String allele;

        FurGene(String allele) {
            this.allele = allele;
        }

        public String getAllele() {
            return allele;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.2) {
                return DOMINANT.getAllele();
            } else {
                return RECESSIVE.getAllele();
            }
        }

        public static boolean isLongFur(String genotype) {
            return genotype.contains("L");
        }
    }


    public enum Base {
        BLACK("B"),
        CHOCOLATE("b"),
        CINNAMON("b1"),

        ;
        private String alelo;

        Base(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.33f) {
                return BLACK.getAlelo();
            } else if (chance < 0.67f){
                return CHOCOLATE.getAlelo();
            } else {
                return CINNAMON.getAlelo();
            }
        }

        public static boolean isBlack(String genotype) {
            return genotype.contains("B");
        }
        public static boolean isChocolate(String genotype) {
            if (genotype.isEmpty()) return false;
            String[] value = genotype.split("-");
            if (value[0].equals("b") || value[1].equals("b")) return true;
            return false;
        }
        public static boolean isCinnamon(String genotype) {
            return genotype.equals("b1-b1");
        }

    }


    public enum OrangeBase {
        ORANGE("O"),
        NOT_ORANGE("o"),

        ;
        private String alelo;

        OrangeBase(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.42f) {
                return ORANGE.getAlelo();
            } else {
                return NOT_ORANGE.getAlelo();
            }
        }

        public static boolean isOrange(String genotype, int gender) {
            return (gender == 0 && genotype.contains("O"))
                    || (gender == 1 && genotype.equals("O-O"));
        }

        public static boolean isTortoiseshell(String genotype) {
            return genotype.contains("O") && genotype.contains("o");
        }

    }


    public enum WhiteRatio {
        WHITE("Wd"),
        SPOTTING("S"),
        NO_WHITE("w"),

        ;
        private String alelo;

        WhiteRatio(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.15f) {
                return WHITE.getAlelo();
            } else if (chance < 0.40f) {
                return SPOTTING.getAlelo();
            } else {
                return  NO_WHITE.getAlelo();
            }
        }

        public static boolean isWhite(String genotype) {
            return genotype.contains("Wd");
        }

        public static boolean isHighSpotted(String genotype) {
            return genotype.equals("S-S");
        }

        public static boolean isLowSpotted(String genotype) {
            return genotype.contains("S") && genotype.contains("w");
        }

    }

    public enum Albino {
        NOT_ALBINO("C"),
        SIAMESE("cs"),
        SEPIA("cb"),
        TRUE_ALBINO("c"),

        ;
        private String alelo;

        Albino(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.05f) {
                return TRUE_ALBINO.getAlelo();
            } else if (chance < 0.15f) {
                return SEPIA.getAlelo();
            } else if (chance < 0.25f) {
                return SIAMESE.getAlelo();
            } else {
                return NOT_ALBINO.getAlelo();
            }
        }

        public static boolean isNotAlbino(String genotype) {
            return genotype.contains("C");
        }

        public static boolean isSiamese(String genotype) {
            return (genotype.contains("cs") && genotype.contains("c")) || genotype.equals("cs-cs");
        }

        public static boolean isMink(String genotype) {
            return genotype.contains("cs") && genotype.contains("cb");
        }

        public static boolean isSepia(String genotype) {
            return (genotype.contains("cb") && genotype.contains("c")) || genotype.equals("cb-cb");
        }

        public static boolean isTrueAlbino(String genotype) {
            return genotype.equals("c-c");
        }

    }

    public enum Dilute {
        DILUTE("D"),
        NON_DILUTE("d"),

        ;
        private String alelo;

        Dilute(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.3f) {
                return DILUTE.getAlelo();
            } else {
                return NON_DILUTE.getAlelo();
            }
        }

        public static boolean isDilute(String genotype) {
            return genotype.contains("D");
        }

    }


    public enum Agouti {
        TABBY("A"),
        NON_TABBY("a"),

        ;
        private String alelo;

        Agouti(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.3f) {
                return TABBY.getAlelo();
            } else {
                return NON_TABBY.getAlelo();
            }
        }

        public static boolean isTabby(String genotype) {
            return genotype.contains("A");
        }

    }

    public enum TabbyStripeTypes {
        MACKEREL("Mc"),
        CLASSIC("mc"),

        ;
        private String alelo;

        TabbyStripeTypes(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();
            if (chance < 0.3f) {
                return MACKEREL.getAlelo();
            } else {
                return CLASSIC.getAlelo();
            }
        }

        public static boolean isMackerel(String genotype) {
            return genotype.contains("Mc");
        }

        public static boolean isClassic(String genotype) {
            return genotype.equals("mc-mc");
        }
    }

    public enum EyeColor {
        YELLOW("yellow"),
        BLUE("blue"),
        GREEN("green"),
        RED("red"),

        ;
        private String alelo;

        EyeColor(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random, String whiteGenotype, String albinoGenotype) {

            if (Albino.isTrueAlbino(albinoGenotype)) {
                return RED.getAlelo();
            }

            if (albinoGenotype.equals("cs-cs") || (albinoGenotype.contains("cs") && albinoGenotype.contains("c"))) {
                return BLUE.getAlelo();
            }

            if ((albinoGenotype.contains("cs") && albinoGenotype.contains("cb"))) {
                if (whiteGenotype.equals("w-w")) {
                    if (random.nextInt(2) == 0) {
                        return GREEN.getAlelo();
                    } else {
                        return BLUE.getAlelo();
                    }
                } else {
                    return GREEN.getAlelo();
                }
            }


            if (whiteGenotype.equals("w-w")) {
                if (random.nextInt(2) == 0) {
                    return GREEN.getAlelo();
                } else {
                    return YELLOW.getAlelo();
                }
            } else {
                int chance2 = random.nextInt(3);
                if (chance2 == 0) {
                    return GREEN.getAlelo();
                } else if (chance2 == 1) {
                    return BLUE.getAlelo();
                } else {
                    return YELLOW.getAlelo();
                }
            }
        }

        public static EyeColor getEyeColor(String genotype) {
            switch (genotype) {
                case "yellow" -> {
                    return  YELLOW;
                }
                case "blue" -> {
                    return  BLUE;
                }
                case "green" -> {
                    return  GREEN;
                }

                case "red" -> {
                    return RED;
                }

                default -> {
                    return YELLOW;
                }
            }
        }

    }


    public enum EyesAnomaly {
        HETEROCHROMIA("h"),
        NORMAL("H")

        ;
        private String alelo;

        EyesAnomaly(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();

            if (chance < 0.1f) {
                return HETEROCHROMIA.getAlelo();
            } else {
                return  NORMAL.getAlelo();
            }
        }


        public static boolean isHeteroChromic(String genotype) {
            return genotype.equals("h-h");
        }

    }

    public enum Chimerism {
        CHIMERA("c"),
        NORMAL("C")

        ;
        private String alelo;

        Chimerism(String alelo) {
            this.alelo = alelo;
        }

        public String getAlelo() {
            return alelo;
        }

        public static String generateAlelo(RandomSource random) {
            float chance = random.nextFloat();

            if (chance < 0.1f) {
                return CHIMERA.getAlelo();
            } else {
                return  NORMAL.getAlelo();
            }
        }


        public static boolean isChimera(String genotype) {
            return genotype.equals("c-c");
        }

    }


    public static String encodeGene(String gene) {
        StringBuilder sb = new StringBuilder();

        for (char c : gene.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append("u").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }


}
