package mister3551.msr.game.screen.timer;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {

    private final long startTime;
    private long pausedAt;
    private long totalPausedTime;
    private boolean paused;
    private String time;

    public Timer() {
        this.startTime = TimeUtils.nanoTime();
        this.totalPausedTime = 0;
        this.paused = false;
        this.time = "0s";
    }

    public void update() {
        if (!paused) {
            long currentTime = TimeUtils.nanoTime();
            long elapsedTime = currentTime - startTime - totalPausedTime;

            long seconds = elapsedTime / 1_000_000_000L;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            seconds %= 60;
            minutes %= 60;

            time = formatTime(hours, minutes, seconds);
        }
    }

    public void pause() {
        if (!paused) {
            pausedAt = TimeUtils.nanoTime();
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            totalPausedTime += TimeUtils.nanoTime() - pausedAt;
            paused = false;
        }
    }

    private String formatTime(long hours, long minutes, long seconds) {
        if (hours > 0) {
            return hours + "h " + minutes + "m " + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    @Override
    public String toString() {
        return time;
    }
}
