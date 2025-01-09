package eu.mister3551.msr.map.character.movement;

import com.badlogic.gdx.maps.MapObject;

public class Collision {

    private final boolean ladder;
    private final boolean stopOnLadder;
    private final boolean water;
    private final Zipline zipline;
    private final MapObject door;

    public Collision(boolean ladder, boolean stopOnLadder, boolean water, Zipline zipline, MapObject door) {
        this.ladder = ladder;
        this.stopOnLadder = stopOnLadder;
        this.water = water;
        this.zipline = zipline;
        this.door = door;
    }

    public boolean isLadder() {
        return ladder;
    }

    public boolean isStopOnLadder() {
        return stopOnLadder;
    }

    public boolean isWater() {
        return water;
    }

    public Zipline getZipline() {
        return zipline;
    }

    public MapObject getDoor() {
        return door;
    }
}
