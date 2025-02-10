package eu.mister3551.msr.screen.gamescreen;

import com.badlogic.gdx.utils.TimeUtils;

public class Timer {

    private long startTime;
    private long pausedAt;
    private long totalPausedTime;
    private boolean paused;
    private boolean running;
    private String time;

    public Timer() {
        this.running = false;
    }

    @Override
    public String toString() {
        return time;
    }

    public void start() {
        if (!running) {
            this.startTime = TimeUtils.nanoTime();
            this.totalPausedTime = 0;
            this.paused = false;
            this.time = "0s";
            this.running = true;
        }
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

    public String string() {
        long currentTime = TimeUtils.nanoTime();
        long elapsedTime = currentTime - startTime - totalPausedTime;

        long seconds = elapsedTime / 1_000_000_000L;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        return (hours < 10 ? "0" + hours : hours) + ":" +
            (minutes < 10 ? "0" + minutes : minutes) + ":" +
            (seconds < 10 ? "0" + seconds : seconds);
    }

    public void dispose() {
        this.totalPausedTime = 0;
        this.paused = false;
        this.time = "0s";
    }

    private String formatTime(long hours, long minutes, long seconds) {
        String format;
        if (hours > 0) {
            format = hours + "h " + minutes + "m " + seconds + "s";
        } else if (minutes > 0) {
            format = minutes + "m " + seconds + "s";
        } else {
            format = seconds + "s";
        }
        return format;
    }
}
