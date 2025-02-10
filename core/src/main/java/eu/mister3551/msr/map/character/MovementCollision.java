package eu.mister3551.msr.map.character;

import com.badlogic.gdx.maps.MapObject;
import eu.mister3551.msr.map.character.movement.Zipline;
import lombok.Getter;

@Getter
public class MovementCollision {

    private final MapObject door;
    private final Zipline zipline;
    private final boolean ladder;
    private final boolean stopOnTopOfLadder;
    private final boolean stopOnBottomOfLadder;
    private final boolean water;

    public MovementCollision(MapObject door, Zipline zipline, boolean ladder, boolean stopOnTopOfLadder, boolean stopOnBottomOfLadder, boolean water) {
        this.door = door;
        this.zipline = zipline;
        this.ladder = ladder;
        this.stopOnTopOfLadder = stopOnTopOfLadder;
        this.stopOnBottomOfLadder = stopOnBottomOfLadder;
        this.water = water;
    }
}
