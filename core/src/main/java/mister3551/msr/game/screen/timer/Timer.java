package mister3551.msr.game.screen.timer;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {

    private final long startTime;
    private String time;

    public Timer() {
        this.startTime = TimeUtils.nanoTime();
        this.time = "0s";
    }

    public void update() {
        long elapsedTime = TimeUtils.timeSinceNanos(startTime);

        long seconds = elapsedTime / 1_000_000_000L;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        time = (hours == 0 && minutes == 0)
            ? seconds + "s"
            : (hours == 0)
            ? minutes + "m " + seconds + "s"
            : hours + "h " + minutes + "m " + seconds + "s";
    }

    @Override
    public String toString() {
        return time;
    }
}
