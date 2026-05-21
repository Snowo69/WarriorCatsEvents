package net.snowteb.warriorcats_events.managers;

import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/** Sequences by Klyonstar
 * This class allows to program sequences to run. Includes wait ticks,
 * <p>
 * running code every X ticks, or running code every Y ticks for X times, etc.
 * <p>
 * This class is under CC By 4.0 license
 * <a href="https://creativecommons.org/licenses/by/4.0/deed.es">...</a>
 * <p>
 * Might not be the greatest thing, but might as well be useful to someone one day.
 * <p>
 * Remember to include the SequenceManager.tick() in the level tick.
 */


public class SequenceManager {

    private static final List<Sequence> active = new ArrayList<>();

    public static void add(Sequence seq) {
        active.add(seq);
    }

    public static void tick(ServerLevel level) {
        Iterator<Sequence> it = active.iterator();

        while (it.hasNext()) {
            Sequence seq = it.next();

            seq.tick(level);

            if (seq.isFinished()) {
                it.remove();
            }
        }
    }
}
