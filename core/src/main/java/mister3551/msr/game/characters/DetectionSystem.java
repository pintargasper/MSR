package mister3551.msr.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import mister3551.msr.game.characters.object.Enemy;
import mister3551.msr.game.characters.object.Player;

public class DetectionSystem {

    private final World world;

    public DetectionSystem(World world) {
        this.world = world;
    }

    public boolean isDetected(Player player, Enemy enemy) {

        Vector2 playerPosition = player.getBody().getPosition();
        Vector2 enemyPosition = enemy.getBody().getPosition();

        final boolean[] visible = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getType() == BodyDef.BodyType.StaticBody) {
                visible[0] = true;
                return 0;
            }
            return 1;
        }, enemyPosition, playerPosition);
        return !visible[0];
    }
}
