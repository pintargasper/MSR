package eu.mister3551.msr.map.character.movement;

import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.map.character.Player;

import java.util.ArrayList;

public class OnZipline {

    public static Zipline movement(Player player, Zipline zipline) {
        ArrayList<Vector2> points = zipline.getPoints();

        if (points.size() < 2) {
            return null;
        }

        if (zipline.getTargetPoint() == null) {
            zipline.setTargetPoint(points.get(0));
            player.getBody().setTransform(zipline.getTargetPoint(), player.getBody().getAngle());
        }

        Vector2 currentPosition = player.getBody().getPosition();

        if (Math.round(currentPosition.dst(points.get(points.size() - 1))) <= 1f) {
            return null;
        }

        if (currentPosition.dst(zipline.getTargetPoint()) < 1f) {
            zipline.setCurrentTargetIndex(zipline.getCurrentTargetIndex() + 1);
            zipline.setTargetPoint(points.get(zipline.getCurrentTargetIndex()));
        }

        Vector2 direction = zipline.getTargetPoint().cpy().sub(currentPosition);
        direction.nor().scl(player.getSpeedOnZipline());

        player.setCurrentAnimation(direction.x < 0 ? player.getCharacterAnimation().getZiplineLeft() : player.getCharacterAnimation().getZiplineRight());
        player.getBody().setGravityScale(0);
        player.getBody().setLinearVelocity(direction);
        return zipline;
    }
}
