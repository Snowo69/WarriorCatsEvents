package net.snowteb.warriorcats_events.managers;

public interface ClimbDataAccessor {

    boolean wce$isClimbing();
    void wce$setClimbing(boolean value);

    int wce$getClimbTick();
    void wce$setClimbTick(int value);

    int wce$getGraceTimer();
    void wce$setGraceTimer(int value);

    void wce$startClimb();
    void wce$stopClimb();
}
