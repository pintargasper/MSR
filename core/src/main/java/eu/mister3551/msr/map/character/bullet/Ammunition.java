package eu.mister3551.msr.map.character.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;

@Getter
public abstract class Ammunition {

    protected Body body;
    protected float width;
    protected float height;
    protected float x;
    protected float y;
    protected int speed;
    protected int damage;
    protected String owner;

    public Ammunition(Body body, String owner) {
        this.body = body;
        this.owner = owner;
        this.width = 10;
        this.height = 5;
    }

    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update();
    public abstract void dispose();
}
