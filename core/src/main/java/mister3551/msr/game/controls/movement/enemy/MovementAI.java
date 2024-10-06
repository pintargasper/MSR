package mister3551.msr.game.controls.movement.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Enemy;

import java.util.ArrayList;

public class MovementAI {

    private Vector2 targetPoint;
    private int currentTargetIndex = 0;
    private boolean movingForward = true;
    private boolean retreat = false;

    //TODO "v redu, ampak treba izboljšati še izmikanje...
    public void movement(Enemy enemy, ArrayList<Vector2> points, boolean ladderCollision, boolean swimming, boolean playerDetected, float delta, float speed) {
        if (points.size() < 2) {
            return;
        }

        if (retreat) {
            retreat(enemy, points, speed);
        } else if (playerDetected) {
            detection(enemy, delta);
        } else {
            walking(enemy, points, swimming, speed);
        }
    }

    private void walking(Enemy enemy, ArrayList<Vector2> points, boolean swimming, float speed) {
        Vector2 currentPosition = enemy.getBody().getPosition();

        if (targetPoint == null) {
            targetPoint = points.get(currentTargetIndex).cpy();
        }

        if (currentPosition.dst(targetPoint) < 1.5f) {
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
            targetPoint = points.get(currentTargetIndex).cpy();
        }

        Vector2 direction = targetPoint.cpy().sub(currentPosition).nor().scl(speed);

        if (swimming) {
            enemy.setCurrentAnimation(direction.x < 0 ? enemy.getCharacterAnimation().getSwimLeft() : enemy.getCharacterAnimation().getSwimRight());
            enemy.setOffset(0);
        } else {
            enemy.setCurrentAnimation(direction.x < 0 ? enemy.getCharacterAnimation().getWalkLeft() : enemy.getCharacterAnimation().getWalkRight());
            enemy.setOffset(direction.x < 0 ? 45 : 0);
        }

        enemy.setLastMove(direction.x < 0 ? "left" : "right");
        enemy.getBody().setLinearVelocity(direction);
    }

    private void detection(Enemy enemy, float delta) {
        if (enemy.isBulletComing() && enemy.getLive() < 40) {
            retreat = true;
        } else {
            attack(enemy, delta);
        }
    }

    private void attack(Enemy enemy, float delta) {
        enemy.getBody().setLinearVelocity(0, 0);
        enemy.setCurrentAnimation(enemy.getLastMove().equals("left") ? enemy.getCharacterAnimation().getStandLeft() : enemy.getCharacterAnimation().getStandRight());
        if (enemy.getShots() < enemy.getWeapon().getMagazineCapacity()) {
            enemy.setShots(enemy.getShots() + enemy.getOnShoot().shoot(enemy, delta));
        }

        if (enemy.getShots() >= enemy.getWeapon().getMagazineCapacity()) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    enemy.setShots(0);
                }
            }, 3);
        }
    }

    private void retreat(Enemy enemy, ArrayList<Vector2> points, float speed) {
        Vector2 currentPosition = enemy.getBody().getPosition();
        Vector2 playerPosition = Static.getPlayer().getBody().getPosition();

        Vector2 directionAwayFromPlayer = currentPosition.cpy().sub(playerPosition).nor();

        Vector2 closestPoint = null;
        float minDistance = Float.MAX_VALUE;

        for (Vector2 point : points) {
            float distance = currentPosition.dst(point);
            Vector2 directionToPoint = point.cpy().sub(currentPosition).nor();

            if (directionToPoint.dot(directionAwayFromPlayer) > 0 && distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        if (closestPoint == null) {
            retreat = false;
            return;
        }

        if (currentPosition.dst(closestPoint) < 1.5f) {
            retreat = false;
            enemy.getBody().setLinearVelocity(0, 0);
            return;
        }

        Vector2 directionToClosestPoint = closestPoint.cpy().sub(currentPosition).nor().scl(speed);

        enemy.setCurrentAnimation(directionToClosestPoint.x < 0 ? enemy.getCharacterAnimation().getWalkLeft() : enemy.getCharacterAnimation().getWalkRight());
        enemy.setOffset(directionToClosestPoint.x < 0 ? 45 : 0);
        enemy.setLastMove(directionToClosestPoint.x < 0 ? "left" : "right");
        enemy.getBody().setLinearVelocity(directionToClosestPoint);
    }
}
