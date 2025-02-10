package eu.mister3551.msr.map.character;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.object.ObjectData;
import eu.mister3551.msr.map.character.weapon.OnShoot;
import eu.mister3551.msr.map.character.weapon.Weapon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Character {

    protected Body body;
    protected final Rectangle bounds;
    protected Weapon weapon;
    protected int width;
    protected int height;
    protected float x;
    protected float y;
    protected float velocityX;
    protected float velocityY;
    protected int life;
    protected int speed;
    protected int speedOnLadder;
    protected int speedOnZipline;
    protected float elapsedTime;
    protected float offset;
    protected boolean jumping;
    protected boolean leftSide;
    protected boolean rightSide;
    protected boolean leftOffset;
    protected boolean rightOffset;
    protected boolean processed;
    protected boolean bulletDetected;

    protected LastMove lastMove;

    public enum LastMove {
        LEFT, RIGHT, UP, DOWN
    }

    protected Collision collision;
    protected MovementCollision movementCollision;

    protected TextureAtlas textureAtlas;
    protected CharacterAnimation characterAnimation;
    protected Animation<TextureRegion> currentAnimation;

    protected String name;
    protected String type;
    protected String group;
    protected float award;

    protected final OnShoot onShoot;

    protected PointLight pointLight;

    protected Character(ObjectData objectData) {
        this.body = objectData.getBody();
        this.bounds = objectData.getBounds();
        this.weapon = objectData.getWeapon();
        this.width = objectData.getWidth();
        this.height = objectData.getHeight();
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.life = objectData.getLife();
        this.speed = objectData.getSpeed();
        this.speedOnLadder = objectData.getSpeedOnLadder();
        this.speedOnZipline = objectData.getSpeedOnZipLine();
        this.lastMove = objectData.getLastMove() != null
            ? objectData.getLastMove().equals("left")
            ? LastMove.LEFT : LastMove.RIGHT : null;
        this.name = objectData.getName();
        this.type = objectData.getType();
        this.group = objectData.getGroup();
        this.award = objectData.getAward();
        this.velocityX = 0;
        this.velocityY = 0;
        this.collision = new Collision();
        this.onShoot = new OnShoot();
    }

    public void setLight(RayHandler rayHandler, float light) {
        if (light < 0.5f) {
            pointLight = new PointLight(rayHandler, 150, Color.CORAL, 5, x / Constants.PPM, y / Constants.PPM);
            pointLight.attachToBody(body);
        }
    }

    public void turnOffLight() {
        if (pointLight != null) {
            pointLight.setActive(false);
        }
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();
    public abstract void resume();
    public abstract void pause();
}
