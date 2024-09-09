package mister3551.msr.game.controls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.controls.zipline.Zipline;

import java.util.ArrayList;

public abstract class Device {

    protected final Body body;
    protected Zipline zipline;
    protected float velocityX;
    protected float velocityY;
    protected float speed;
    protected float speedOnLadder;
    protected float speedOnZipLine;
    protected int jumps;

    public Device(Body body, float speed, float speedOnLadder, float speedOnZipLine) {
        this.body = body;
        this.velocityX = 0;
        this.velocityY = 0;
        this.speed = speed;
        this.speedOnLadder = speedOnLadder;
        this.speedOnZipLine = speedOnZipLine;
        this.jumps = 0;
    }

    public abstract void show();
    public abstract void render(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void inputs(boolean ladderCollision, Zipline zipLine);
    public abstract void normal();
    public abstract void jump();
    public abstract void onLadder();
    public abstract void onZipLine(ArrayList<Vector2> points);

    public Zipline getZipline() {
        return zipline;
    }

    public void setZipline(Zipline zipline) {
        this.zipline = zipline;
    }
}
