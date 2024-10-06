package mister3551.msr.game.controls;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.controls.movement.Zipline;

import java.util.ArrayList;

public abstract class Device {

    protected final Body body;
    protected final Player player;
    protected Zipline zipline;
    protected boolean isShooting;

    public Device(Body body, Player player) {
        this.body = body;
        this.player = player;
        this.isShooting = true;
    }

    public abstract void show();
    public abstract void render(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void inputs(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision, Zipline zipline);
    public abstract void walking(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision);
    public abstract void jump();
    public abstract void onZipline(ArrayList<Vector2> points);

    public Zipline getZipline() {
        return zipline;
    }

    public void setZipline(Zipline zipline) {
        this.zipline = zipline;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }
}
