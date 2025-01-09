package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import eu.mister3551.msr.map.ObjectData;
import eu.mister3551.msr.map.character.collision.Collision;
import lombok.Data;

@Data
public abstract class Character {

    protected TextureAtlas textureAtlas;
    protected CharacterAnimation characterAnimation;
    protected Animation<TextureRegion> currentAnimation;
    protected Collision collision;
    protected final Body body;
    protected final Rectangle bounds;
    protected final String name;
    protected final String type;
    protected final String group;
    protected float x;
    protected float y;
    protected float velocityX;
    protected float velocityY;
    protected float width;
    protected float height;
    protected float offset;
    protected float elapsedTime;
    protected final float speed;
    protected final float speedOnLadder;
    protected final float speedOnZipline;
    protected boolean onLeftSide;
    protected boolean onRightSide;
    protected boolean onFloor;
    protected boolean bodyOnFloor;
    protected int life;
    protected int jumps;
    protected LastMove lastMove;

    public enum LastMove {
        LEFT, RIGHT, UP, DOWN
    }

    public Character(ObjectData objectData) {
        this.collision = new Collision();
        this.body = objectData.getBody();
        this.bounds = objectData.getBounds();
        this.name = objectData.getName();
        this.type = objectData.getType();
        this.group = objectData.getGroup();
        this.width = objectData.getWidth();
        this.height = objectData.getHeight();
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.life = objectData.getLife();
        this.speed = objectData.getSpeed();
        this.speedOnLadder = objectData.getSpeedOnLadder();
        this.speedOnZipline = objectData.getSpeedOnZipline();
        this.lastMove = LastMove.RIGHT;
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void resume();
    public abstract void pause();
}
