package mister3551.msr.game.controls.zipline;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Zipline {

    private final ArrayList<Vector2> points;
    private final boolean ziplineCollision;
    private Vector2 targetPoint;
    private int currentTargetIndex = 0;

    public Zipline(ArrayList<Vector2> points, boolean ziplineCollision) {
        this.points = points;
        this.ziplineCollision = ziplineCollision;
    }

    public ArrayList<Vector2> getPoints() {
        return points;
    }

    public boolean isZiplineCollision() {
        return ziplineCollision;
    }

    public Vector2 getTargetPoint() {
        return targetPoint;
    }

    public void setTargetPoint(Vector2 targetPoint) {
        this.targetPoint = targetPoint;
    }

    public int getCurrentTargetIndex() {
        return currentTargetIndex;
    }

    public void setCurrentTargetIndex(int currentTargetIndex) {
        this.currentTargetIndex = currentTargetIndex;
    }
}
