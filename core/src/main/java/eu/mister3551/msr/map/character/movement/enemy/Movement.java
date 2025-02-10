package eu.mister3551.msr.map.character.movement.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Enemy;
import lombok.Getter;

import java.util.ArrayList;

//TODO enemy improve movement
@Getter
public class Movement {

    private Vector2 lastPoint;
    private Vector2 targetPoint;
    private int currentTargetIndex;
    private boolean movingForward;

    public Movement() {
        this.currentTargetIndex = 0;
        this.movingForward = true;
    }

    public void move(Enemy enemy, ArrayList<Vector2> points, float delta) {
        if (points == null || points.size() < 2) {
            return;
        }

        Vector2 direction;
        if (enemy.isBulletDetected() && enemy.getLife() < 40) {
            direction = handleBulletDetection(enemy);
        } else if (enemy.isPlayerDetected()) {
            direction = handlePlayerDetection(enemy, delta);
        } else {
            direction = updateTargetPoint(enemy, points);
        }
        enemy.getBody().setLinearVelocity(direction);
        enemy.getBody().setGravityScale(10);
    }

    private Vector2 updateTargetPoint(Enemy enemy, ArrayList<Vector2> points) {
        if (targetPoint == null || enemy.getBody().getPosition().dst(targetPoint) < 1.5f) {
            if (movingForward) {
                currentTargetIndex++;
                if (currentTargetIndex >= points.size()) {
                    movingForward = false;
                    currentTargetIndex = points.size() - 2;
                }
            } else {
                currentTargetIndex--;
                if (currentTargetIndex < 0) {
                    movingForward = true;
                    currentTargetIndex = 1;
                }
            }
            lastPoint = targetPoint;
            targetPoint = points.get(currentTargetIndex).cpy();
        }

        Vector2 direction = calculateDirection(enemy);

        Animation<TextureRegion> animation = direction.x < 0
            ? (enemy.getMovementCollision().isWater() ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getWalkLeft())
            : (enemy.getMovementCollision().isWater() ? enemy.getCharacterAnimation().getSwimRight() : enemy.getCharacterAnimation().getWalkRight());
        int offset = direction.x < 0 && !enemy.getMovementCollision().isWater() ? 45 : 0;
        enemy.setCurrentAnimation(animation);
        enemy.setOffset(offset);
        enemy.setLastMove(direction.x < 0 ? Character.LastMove.LEFT : Character.LastMove.RIGHT);
        return direction;
    }

    private Vector2 handlePlayerDetection(Enemy enemy, float delta) {
        Vector2 playerPosition = Constants.gameScreen.getPlayer().getBody().getPosition();
        Vector2 currentPosition = enemy.getBody().getPosition();

        Vector2 direction = currentPosition.cpy()
            .sub(playerPosition)
            .nor()
            .scl(enemy.getSpeed());

        if (enemy.getMovementCollision().isWater()) {
            retreatOutOfWater();
        } else {
            enemy.setCurrentAnimation(direction.x < 0
                ? enemy.getCharacterAnimation().getStandLeft()
                : enemy.getCharacterAnimation().getStandRight());
            enemy.setOffset(direction.x < 0 ? 0 : 45);
            attack(enemy, delta);
            return new Vector2(0, 0);
        }
        return direction.cpy();
    }

    private Vector2 handleBulletDetection(Enemy enemy) {
        targetPoint = lastPoint;

        Vector2 playerPosition = Constants.gameScreen.getPlayer().getBody().getPosition();
        Vector2 currentPosition = enemy.getBody().getPosition();

        Vector2 direction = currentPosition.cpy()
            .sub(playerPosition)
            .nor()
            .scl(enemy.getSpeed());

        if (enemy.isLeftSide() || enemy.isRightSide() || enemy.isLeftOffset() || enemy.isRightOffset()) {
            boolean isSwimming = enemy.getMovementCollision().isWater();
            if (direction.x > 0) {
                enemy.setCurrentAnimation(isSwimming ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getStandLeft());
            } else {
                enemy.setCurrentAnimation(isSwimming ? enemy.getCharacterAnimation().getSwimRight() : enemy.getCharacterAnimation().getStandRight());
            }
            return new Vector2(0, 0);
        }

        if (enemy.getMovementCollision().isWater()) {
            enemy.setCurrentAnimation(direction.x < 0 ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getSwimRight());
            enemy.setOffset(direction.x > 0 ? 0 : 45);
        } else {
            enemy.setCurrentAnimation(direction.x > 0 ? enemy.getCharacterAnimation().getWalkLeft() : enemy.getCharacterAnimation().getWalkRight());
            enemy.setOffset(direction.x < 0 ? 0 : 45);
        }
        return direction.cpy();
    }

    private void attack(Enemy enemy, float delta) {
        if (!enemy.getMovementCollision().isWater()) {
            enemy.getBody().setLinearVelocity(0, 0);
            enemy.setCurrentAnimation(enemy.getLastMove().equals(Character.LastMove.LEFT) ? enemy.getCharacterAnimation().getStandLeft() : enemy.getCharacterAnimation().getStandRight());

            if (enemy.getShoots() < enemy.getWeapon().getMagazineCapacity()) {
                enemy.setShoots(enemy.getShoots() + enemy.getOnShoot().shoot(enemy, delta));
            }

            if (enemy.getShoots() >= enemy.getWeapon().getMagazineCapacity()) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        enemy.setShoots(0);
                    }
                }, 3);
            }
        }
    }

    private void retreatOutOfWater() {
        targetPoint = lastPoint;
    }

    private Vector2 calculateDirection(Enemy enemy) {
        Vector2 currentPosition = enemy.getBody().getPosition();
        return targetPoint.cpy()
            .sub(currentPosition)
            .nor()
            .scl(enemy.getSpeed());
    }
}
