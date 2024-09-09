package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.controls.zipline.OnZipline;
import mister3551.msr.game.controls.zipline.Zipline;

import java.util.ArrayList;

public class Computer extends Device {

    public Computer(Body body, float speed, float speedOnLadder, float speedOnZipline) {
        super(body, speed, speedOnLadder, speedOnZipline);
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
    public void inputs(boolean ladderCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        if (getZipline() != null) {
            onZipLine(getZipline().getPoints());
        } else {
            if (ladderCollision) {
                onLadder();
            } else {
                body.setGravityScale(1);
                jump();
            }
            normal();

            if (body.getLinearVelocity().y == 0) {
                jumps = 0;
            }
        }
    }

    @Override
    public void normal() {
        velocityX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX++;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX--;
        }
        body.setLinearVelocity(velocityX * speed, Math.min(body.getLinearVelocity().y, 25));
    }

    @Override
    public void jump() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && jumps < 1) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, body.getMass() * 12), body.getPosition(), true);
            jumps++;
        }
    }

    @Override
    public void onLadder() {
        velocityY = 0;
        body.setGravityScale(0);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocityY++;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocityY--;
        }
        body.setLinearVelocity(velocityX * speedOnLadder, velocityY * speedOnLadder);
    }

    @Override
    public void onZipLine(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(body, zipline, points, speedOnZipLine);
    }
}
