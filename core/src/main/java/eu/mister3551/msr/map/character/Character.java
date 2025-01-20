package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import eu.mister3551.msr.map.ObjectData;
import eu.mister3551.msr.map.character.collision.Collision;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.map.character.weapon.OnShoot;
import eu.mister3551.msr.map.character.weapon.Weapon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Character {

    protected TextureAtlas textureAtlas;
    protected CharacterAnimation characterAnimation;
    protected Animation<TextureRegion> currentAnimation;
    protected Collision collision;
    protected MovementCollision movementCollision;
    protected Body body;
    protected final Rectangle bounds;
    protected final String name;
    protected final String type;
    protected final String group;
    protected float award;
    protected int life;
    protected int maxLife;
    protected int jumps;
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
    protected boolean onJump;
    protected boolean processed;
    protected boolean animationPaused;
    protected float pausedTime;
    protected LastMove lastMove;
    protected Weapon weapon;
    protected final OnShoot onShoot;

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
        this.award = objectData.getAward();
        this.width = objectData.getWidth();
        this.height = objectData.getHeight();
        this.life = objectData.getLife();
        this.maxLife = objectData.getLife();
        this.jumps = objectData.getJumps();
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.speed = objectData.getSpeed();
        this.speedOnLadder = objectData.getSpeedOnLadder();
        this.speedOnZipline = objectData.getSpeedOnZipline();
        this.lastMove = LastMove.RIGHT;
        this.weapon = objectData.getWeapon();
        this.onShoot = new OnShoot();
        this.animationPaused = false;
        this.pausedTime = 0;
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void resume();
    public abstract void pause();
}
