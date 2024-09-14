package mister3551.msr.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Character {

    protected Body body;
    protected Rectangle rectangle;
    protected TextureAtlas textureAtlas;
    protected CharacterAnimation characterAnimation;
    private Animation<TextureRegion> currentAnimation;
    protected float width;
    protected float height;
    protected float x;
    protected float y;
    protected float speed;
    protected float speedOnLadder;
    protected float speedOnZipline;
    protected int jumps;
    protected boolean onFloor;
    protected boolean onLeftSide;
    protected boolean onRightSide;
    protected boolean jumping;

    public Character(Body body, Rectangle rectangle, float width, float height) {
        this.body = body;
        this.rectangle = rectangle;
        this.width = width;
        this.height = height;
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.speed = 0;
        this.speedOnLadder = 0;
        this.speedOnZipline = 0;
        this.jumps = 0;
        this.onFloor = false;
        this.jumping = false;
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();

    public Body getBody() {
        return body;
    }

    public CharacterAnimation getCharacterAnimation() {
        return characterAnimation;
    }

    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSpeedOnLadder() {
        return speedOnLadder;
    }

    public float getSpeedOnZipline() {
        return speedOnZipline;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public boolean isOnFloor() {
        return onFloor;
    }

    public void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
    }

    public boolean isOnLeftSide() {
        return onLeftSide;
    }

    public void setOnLeftSide(boolean onLeftSide) {
        this.onLeftSide = onLeftSide;
    }

    public boolean isOnRightSide() {
        return onRightSide;
    }

    public void setOnRightSide(boolean onRightSide) {
        this.onRightSide = onRightSide;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
}
