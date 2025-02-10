package eu.mister3551.msr.map.character.movement;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Zipline {

    private final ArrayList<Vector2> points;
    private final boolean ziplineCollision;
    private Vector2 targetPoint;
    private int currentTargetIndex = 0;

    public Zipline(ArrayList<Vector2> points, boolean ziplineCollision) {
        this.points = points;
        this.ziplineCollision = ziplineCollision;
    }
}
