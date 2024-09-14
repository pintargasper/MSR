package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.characters.Player;
import mister3551.msr.game.controls.zipline.OnZipline;
import mister3551.msr.game.controls.zipline.Zipline;

import java.util.ArrayList;

public class Computer extends Device {

    public Computer(Body body, Player player) {
        super(body, player);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void inputs(boolean ladderCollision, boolean stopOnLadder, boolean watterCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            onZipline(currentZipline.getPoints());
        } else {
            walking(ladderCollision, stopOnLadder, watterCollision);
        }
    }

    @Override
    public void walking(boolean ladderCollision, boolean stopOnLadder, boolean watterCollision) {
        velocityX = 0;
        velocityY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !player.isOnLeftSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() :  player.getCharacterAnimation().getZiplineLeft());
            lastMove = "left";
            velocityX--;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && !player.isOnRightSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() :  player.getCharacterAnimation().getZiplineRight());
            lastMove = "right";
            velocityX++;
        } else if (player.isOnFloor()) {
            if (!watterCollision) {
                player.setCurrentAnimation(lastMove.equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(lastMove.equals("left") ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && ladderCollision) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            velocityY = player.getSpeedOnLadder();
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
            velocityY = -player.getSpeedOnLadder();
        } else if (!stopOnLadder && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
        }

        if (player.isOnFloor()) {
            player.setJumps(0);

            if (velocityX == 0 && stopOnLadder) {
                player.setCurrentAnimation(lastMove.equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }
        }

        if (ladderCollision && velocityY == 0 && !player.isOnFloor()) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getStandingOnLadder() : lastMove.equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && player.getJumps() < 1 && player.isOnFloor() && !ladderCollision) {
            jump();
            body.setGravityScale(1);
        }

        if (ladderCollision) {
            body.setGravityScale(0);
            body.setLinearVelocity(velocityX * player.getSpeed(), velocityY);
        } else {
            body.setGravityScale(1);
            body.setLinearVelocity(velocityX * player.getSpeed(), Math.min(body.getLinearVelocity().y, 25));
        }
    }

    @Override
    public void jump() {
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.applyLinearImpulse(new Vector2(0, body.getMass() * 12), body.getPosition(), true);
        player.setJumps(player.getJumps() + 1);
    }

    @Override
    public void onZipline(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(body, player, zipline, points, player.getSpeedOnZipline());
    }
}
