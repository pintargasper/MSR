package mister3551.msr.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Ammunition {

    protected Body body;
    protected float width;
    protected float height;
    protected float x;
    protected float y;
    protected int speed;
    protected int damage;

    public Ammunition(Body body) {
        this.body = body;
        this.width = 20;
        this.height = 10;
    }

    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update();
    public abstract void dispose();

    public Body getBody() {
        return body;
    }

    public int getDamage() {
        return damage;
    }
}
