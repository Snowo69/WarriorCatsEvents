package net.snowteb.warriorcats_events.managers;

import net.minecraft.server.level.ServerLevel;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.LinkedList;
import java.util.Queue;

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
 * Remember to include the SequenceManager.tick() in the Server or Client tick.
 */

public class Sequence {

    private final Queue<SequenceStep> steps = new LinkedList<>();
    private SequenceStep current;

    public Sequence(ServerLevel level) {
    }

    public Sequence then(Runnable r) {
        steps.add(level -> {
            run(r);

            return true;
        });
        return this;
    }

    public Sequence wait(int ticks) {
        steps.add(new SequenceStep() {
            int time = ticks;

            @Override
            public boolean tick(ServerLevel level) {
                time--;
                return time <= 0;
            }
        });
        return this;
    }

    public Sequence then(int ticks, Runnable r) {
        steps.add(new SequenceStep() {
            int time = ticks;
            @Override
            public boolean tick(ServerLevel level) {
                run(r);

                time--;
                return time <= 0;
            }
        });

        return this;
    }

    public Sequence thenEveryFor(int everyTicks, int forTicks, Runnable r) {
        steps.add(new SequenceStep() {
            int time = forTicks;
            int counter = 0;

            @Override
            public boolean tick(ServerLevel level) {
                counter++;

                if (counter % everyTicks == 0) {
                    run(r);
                }

                time--;
                return time <= 0;
            }
        });
        return this;
    }

    public Sequence thenEveryForTimes(int everyTicks, int times, Runnable r) {
        steps.add(new SequenceStep() {
            int remaining = times;
            int counter = 0;
            @Override
            public boolean tick(ServerLevel level) {
                counter++;

                if (counter % everyTicks == 0) {
                    run(r);
                    remaining--;
                }
                return remaining <= 0;
            }
        });
        return this;
    }

    public Sequence build() {
        SequenceManager.add(this);
        return this;
    }

    public void tick(ServerLevel level) {
        if (current == null) {
            current = steps.poll();
            if (current == null) return;
        }

        if (current.tick(level)) {
            current = null;
        }
    }

    public boolean isFinished() {
        return current == null && steps.isEmpty();
    }

    public void run(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            WarriorCatsEvents.LOGGER.warn("A Sequence step has thrown an exception: ", e);
        }
    }

    // Example of use
//    new Sequence(level)
//    .then(() -> {
//        level.sendParticles(ParticleTypes.SPLASH, player.getX(),player.getY() + 0.4, player.getZ(),
//                10, 0.3, 0.2, 0.3, 0.01);
//    })
//    .wait(40)
//    .thenEveryForTimes(20, 10, () -> {
//        level.sendParticles(ParticleTypes.SMALL_FLAME, player.getX(),player.getY() + 0.4,
//                player.getZ(), 10, 0.3, 0.2, 0.3,
//                0.01);
//    })
//    .wait(10)
//    .then(() -> {
//        level.playSound(null, player.blockPosition(), SoundEvents.CAT_HISS, SoundSource.PLAYERS, 0.2f ,1.5f);
//    })
//    .build();

}
