package mister3551.msr.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Character {

    protected Body body;
    protected Rectangle rectangle;
    protected float width;
    protected float height;
    protected float x;
    protected float y;

    public Character(Body body, Rectangle rectangle, float width, float height) {
        this.body = body;
        this.rectangle = rectangle;
        this.width = width;
        this.height = height;
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();

    public Body getBody() {
        return body;
    }
}
