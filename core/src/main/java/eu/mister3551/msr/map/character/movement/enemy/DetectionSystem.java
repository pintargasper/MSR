package eu.mister3551.msr.map.character.movement.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.*;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.weapon.bullet.Bullet;
import eu.mister3551.msr.screen.GameScreen;

public class DetectionSystem {

    private final World world;

    public DetectionSystem(World world) {
        this.world = world;
    }

    public boolean isDetected(Player player, Enemy enemy) {
        Vector2 playerPosition = player.getBody().getPosition();
        Vector2 enemyPosition = enemy.getBody().getPosition();

        final boolean[] visible = {false};

        boolean isFacingPlayer = false;
        Vector2 targetPoint = enemy.getMovement().getTargetPoint();
        Vector2 lastPoint = enemy.getMovement().getLastPoint() == null ? targetPoint : enemy.getMovement().getLastPoint();

        if (targetPoint != null) {
            float distanceToPlayer = enemyPosition.dst(playerPosition);
            if (distanceToPlayer > enemy.getRange()) {
                return false;
            }
            Vector2 enemyDirection = new Vector2(targetPoint).sub(lastPoint).nor();
            Vector2 toPlayer = new Vector2(playerPosition).sub(enemyPosition).nor();
            float angle = enemyDirection.angleDeg(toPlayer);
            float viewAngleThreshold = Math.max(15.0f, 90.0f - distanceToPlayer / 10.0f);

            if (angle < viewAngleThreshold || angle > (360 - viewAngleThreshold)) {
                isFacingPlayer = true;
            }
        }

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getType() == BodyDef.BodyType.StaticBody) {
                visible[0] = true;
                return 0;
            }
            return 1;
        }, enemyPosition, playerPosition);
        return !visible[0] && !player.isJumping() && radius(player, enemy) && isFacingPlayer;
    }

    public boolean bulletDetection(Character character) {
        Vector2 characterPosition = character.getBody().getPosition();

        for (Bullet bullet : Constants.gameScreen.getBullets()) {
            Vector2 bulletPosition = bullet.getBody().getPosition();
            Vector2 bulletVelocity = bullet.getBody().getLinearVelocity();
            Vector2 directionToCharacter = new Vector2(characterPosition).sub(bulletPosition).nor();

            float dotProduct = bulletVelocity.dot(directionToCharacter);

            if (dotProduct > 0) {
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
