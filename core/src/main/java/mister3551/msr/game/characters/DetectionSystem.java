package mister3551.msr.game.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Bullet;
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

        Vector2 directionToPlayer = playerPosition.cpy().sub(enemyPosition).nor();
        float angleToPlayer = directionToPlayer.angleDeg(enemy.getBody().getLinearVelocity().nor());
        boolean isFacingPlayer = Math.abs(angleToPlayer) < 90 || Math.abs(angleToPlayer) > 270;

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getType() == BodyDef.BodyType.StaticBody) {
                visible[0] = true;
                return 0;
            }
            return 1;
        }, enemyPosition, playerPosition);
        return !visible[0] && isFacingPlayer && (radius(player, enemy) && player.isOnFloor() && player.isBodyOnFloor());
    }

    public boolean bulletDetection(Character character) {
        Vector2 characterPosition = character.getBody().getPosition();

        for (Bullet bullet : Static.getBullets()) {
            Vector2 bulletPosition = bullet.getBody().getPosition();

            if (calculateDistance(characterPosition.x, characterPosition.y, bulletPosition.x, bulletPosition.y, 100)) {
                final boolean[] hitStaticObject = {false};

                world.rayCast((fixture, point, normal, fraction) -> {
                    if (fixture.getBody().getType() == BodyDef.BodyType.StaticBody) {
                        hitStaticObject[0] = true;
                        return 0;
                    }
                    return 1;
                }, bulletPosition, characterPosition);

                if (!hitStaticObject[0]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean radius(Player player, Enemy enemy) {
        return calculateDistance(enemy.getX(), enemy.getY(), player.getX(), player.getY(), 300);
    }

    private boolean calculateDistance(float enemyX, float enemyY, float playerX, float playerY, float maxDistance) {
        return Math.sqrt(Math.pow(enemyX - playerX, 2) + Math.pow(enemyY - playerY, 2)) < maxDistance;
    }
}
