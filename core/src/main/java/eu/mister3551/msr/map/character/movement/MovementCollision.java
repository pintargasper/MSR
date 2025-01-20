package eu.mister3551.msr.map.character.movement;

import com.badlogic.gdx.maps.MapObject;
import lombok.Getter;

@Getter
public class MovementCollision {

    private final boolean ladder;
    private final boolean stopOnLadder;
    private final boolean water;
    private final Zipline zipline;
    private final MapObject door;

    public MovementCollision(boolean ladder, boolean stopOnLadder, boolean water, Zipline zipline, MapObject door) {
        this.ladder = ladder;
        this.stopOnLadder = stopOnLadder;
        this.water = water;
        this.zipline = zipline;
        this.door = door;
    }
}
