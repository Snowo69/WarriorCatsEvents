package net.snowteb.warriorcats_events.managers;

import net.minecraft.server.level.ServerLevel;

public interface SequenceStep {
    boolean tick(ServerLevel level);
}
