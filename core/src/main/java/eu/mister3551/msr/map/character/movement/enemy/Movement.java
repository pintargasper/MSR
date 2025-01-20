package eu.mister3551.msr.map.character.movement.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.screen.GameScreen;

import java.util.ArrayList;

public class Movement {

    private Vector2 lastPoint;
    private Vector2 targetPoint;
    private int currentTargetIndex = 0;
    private boolean movingForward = true;
    private boolean movingBackward = false;
    private long lastExecutionTime = 0;
    private long interval = 1000;

    public void move(Enemy enemy, MovementCollision movementCollision, ArrayList<Vector2> points, float delta) {
        if (points == null || points.size() < 2) {
            return;
        }

        boolean waterCollision = movementCollision.isWater();
        if (enemy.getLife() < 40) {
            retreat(enemy, waterCollision, delta);
        } else {
            if (enemy.isPlayerDetected()) {
                handlePlayerDetection(enemy, waterCollision, delta);
            } else if (!movingBackward) {
                updateTargetPoint(enemy, points);
                Vector2 direction = calculateDirection(enemy);
                enemy.getBody().setLinearVelocity(direction);

                Animation<TextureRegion> animation = direction.x < 0
                    ? (waterCollision ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getWalkLeft())
                    : (waterCollision ? enemy.getCharacterAnimation().getSwimRight() : enemy.getCharacterAnimation().getWalkRight());
                int offset = direction.x < 0 && !waterCollision ? 45 : 0;
                enemy.setCurrentAnimation(animation);
                enemy.setOffset(offset);
                enemy.setLastMove(direction.x < 0 ? Character.LastMove.LEFT : Character.LastMove.RIGHT);
            } else {
                if (!waterCollision) {
                    movingBackward = false;
                }
            }
        }
    }

    private void updateTargetPoint(Enemy enemy, ArrayList<Vector2> points) {
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
    }

    private Vector2 calculateDirection(Enemy enemy) {
        Vector2 currentPosition = enemy.getBody().getPosition();
        return targetPoint.cpy()
            .sub(currentPosition)
            .nor()
            .scl(enemy.getSpeed());
    }

    private void handlePlayerDetection(Enemy enemy, boolean waterCollision, float delta) {
        Vector2 playerPosition = GameScreen.player.getBody().getPosition();
        Vector2 currentPosition = enemy.getBody().getPosition();

        Vector2 direction = playerPosition.cpy()
            .sub(currentPosition)
            .nor()
            .scl(enemy.getSpeed());

        Animation<TextureRegion> animation = direction.x < 0
            ? (waterCollision ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getWalkLeft())
            : (waterCollision ? enemy.getCharacterAnimation().getSwimRight() : enemy.getCharacterAnimation().getWalkRight());
        int offset = direction.x < 0 && !waterCollision ? 45 : 0;
        enemy.setCurrentAnimation(animation);
        enemy.setOffset(offset);

        if (currentPosition.dst(playerPosition) < enemy.getRange()) {
            if (waterCollision) {
                retreatOutOfWater(enemy, delta);
                if (lastPoint != null) {
                    targetPoint = lastPoint;
                }
                movingBackward = true;
            } else {
                attack(enemy, false, delta);
            }
        }
    }

    private void retreatOutOfWater(Enemy enemy, float delta) {
        Vector2 playerPosition = GameScreen.player.getBody().getPosition();
        Vector2 currentPosition = enemy.getBody().getPosition();

        Vector2 retreatDirection = currentPosition.cpy()
            .sub(playerPosition)
            .nor()
            .scl(enemy.getSpeed());

        enemy.setCurrentAnimation(retreatDirection.x > 0
            ? enemy.getCharacterAnimation().getSwimLeft()
            : enemy.getCharacterAnimation().getSwimRight());

        enemy.setOffset(retreatDirection.x < 0 ? 45 : 0);
        enemy.setLastMove(retreatDirection.x < 0 ? Character.LastMove.LEFT : Character.LastMove.RIGHT);

        enemy.getBody().setLinearVelocity(retreatDirection);

        attackInRetreat(enemy, true, delta);
    }

    private void attack(Enemy enemy, boolean watterCollision, float delta) {
        if (!watterCollision) {
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

    private void attackInRetreat(Enemy enemy, boolean watterCollision, float delta) {
        if (!watterCollision) {
            long currentTime = TimeUtils.millis();
            if (currentTime - lastExecutionTime >= interval) {
                if (enemy.getShoots() < enemy.getWeapon().getMagazineCapacity()) {
                    enemy.setShoots(enemy.getShoots() + enemy.getOnShoot().shoot(enemy, delta));
                }
                lastExecutionTime = currentTime;
                interval = enemy.getShoots() % 2 == 0 ? 100 : 300;
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

    private void retreat(Enemy enemy, boolean watterCollision, float delta) {
        Vector2 playerPosition = GameScreen.player.getBody().getPosition();
        Vector2 currentPosition = enemy.getBody().getPosition();

        if (currentPosition.dst(playerPosition) < enemy.getRange()) {
            if (enemy.isOnRightSide() || enemy.isOnLeftSide()) {
                enemy.setCurrentAnimation(
                    enemy.isOnLeftSide()
                        ? enemy.getCharacterAnimation().getStandRight()
                        : enemy.getCharacterAnimation().getStandLeft()
                );
                enemy.getBody().setLinearVelocity(0, 0);
            } else {
                Vector2 retreatDirection = currentPosition.cpy()
                    .sub(playerPosition)
                    .nor()
                    .scl(enemy.getSpeed());

                if (watterCollision) {
                    enemy.setCurrentAnimation(retreatDirection.x > 0 ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getSwimRight());
                } else {
                    enemy.setCurrentAnimation(retreatDirection.x > 0 ? enemy.getCharacterAnimation().getWalkLeft() : enemy.getCharacterAnimation().getWalkRight());
                }
                enemy.setOffset(retreatDirection.x < 0 ? 45 : 0);
                enemy.setLastMove(retreatDirection.x < 0 ? Character.LastMove.RIGHT : Character.LastMove.LEFT);
                enemy.getBody().setLinearVelocity(retreatDirection);
            }
            attackInRetreat(enemy, watterCollision, delta);
        } else {
            enemy.setLife(41);
        }
    }
}
