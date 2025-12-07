package net.snowteb.warriorcats_events.client;

public class AnimationClientData {

    public static int anim1 = 0;
    public static int anim2 = 0;
    public static int anim3 = 0;
    public static int anim4 = 0;
    public static int anim5 = 0;
    public static int anim6 = 0;
    public static boolean isPlayerShape = false;


    public static int getAnim1() {
        return anim1;
    }

    public static int getAnim2() {
        return anim2;
    }

    public static int getAnim3() {
        return anim3;
    }

    public static int getAnim4() {
        return anim4;
    }

    public static int getAnim5() {
        return anim5;
    }

    public static int getAnim6() {
        return anim6;
    }

    public static void reset() {
        anim1 = anim2 = anim3 = anim4 = anim5 = anim6 = 0;
    }
}
