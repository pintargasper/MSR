package mister3551.msr.game.controls.zipline;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class OnZipline {

    public static Zipline movement(Body body, Zipline zipline, ArrayList<Vector2> points, float speedOnZipline) {
        if (points.size() < 2) {
            return null;
        }

        if (zipline.getTargetPoint() == null) {
            zipline.setTargetPoint(points.get(0));
            body.setTransform(zipline.getTargetPoint(), body.getAngle());
        }

        Vector2 currentPosition = body.getPosition();

        if (Math.round(currentPosition.dst(points.get(points.size() - 1))) <= 1f) {
            return null;
        }

        if (currentPosition.dst(zipline.getTargetPoint()) < 1f) {
            zipline.setCurrentTargetIndex(zipline.getCurrentTargetIndex() + 1);
            zipline.setTargetPoint(points.get(zipline.getCurrentTargetIndex()));
        }

        Vector2 direction = zipline.getTargetPoint().cpy().sub(currentPosition);
        direction.nor().scl(speedOnZipline);

        body.setGravityScale(0);
        body.setLinearVelocity(direction);
        return zipline;
    }
}