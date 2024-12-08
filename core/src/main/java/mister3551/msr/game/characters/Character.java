package mister3551.msr.game.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.characters.object.Weapon;
import mister3551.msr.game.controls.shoot.OnShoot;

public abstract class Character {

    protected Body body;
    protected Rectangle rectangle;
    protected Weapon weapon;
    protected TextureAtlas textureAtlas;
    protected CharacterAnimation characterAnimation;
    protected Animation<TextureRegion> currentAnimation;
    protected final OnShoot onShoot;
    protected String name;
    protected String type;
    protected String group;
    protected float width;
    protected float height;
    protected float x;
    protected float y;
    protected float velocityX;
    protected float velocityY;
    protected float offset;
    protected int live;
    protected float speed;
    protected float speedOnLadder;
    protected float speedOnZipline;
    protected float elapsedTime;
    protected int jumps;
    protected int shots;
    protected boolean bodyOnFloor;
    protected boolean onFloor;
    protected boolean onLeftSide;
    protected boolean onRightSide;
    protected String lastMove;
    protected boolean bulletComing;

    public Character(Body body, Rectangle rectangle, Weapon weapon, float width, float height) {
        this.body = body;
        this.rectangle = rectangle;
        this.weapon = weapon;
        this.onShoot = new OnShoot();
        this.width = width;
        this.height = height;
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.live = 100;
        this.speed = 0;
        this.speedOnLadder = 0;
        this.speedOnZipline = 0;
        this.elapsedTime = 0;
        this.jumps = 0;
        this.shots = 0;
        this.bodyOnFloor = false;
        this.onFloor = false;
        this.lastMove = "right";
    }

    public abstract void show();
    public abstract void render(SpriteBatch spriteBatch, float delta);
    public abstract void update(float delta);
    public abstract void resize(int width, int height);
    public abstract void dispose();

    public Body getBody() {
        return body;
    }

    public Weapon getWeapon() {
        return weapon;
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

    public OnShoot getOnShoot() {
        return onShoot;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
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

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public boolean isBodyOnFloor() {
        return bodyOnFloor;
    }

    public void setBodyOnFloor(boolean bodyOnFloor) {
        this.bodyOnFloor = bodyOnFloor;
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

    public String getLastMove() {
        return lastMove;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    public boolean isBulletComing() {
        return bulletComing;
    }

    public void setBulletComing(boolean bulletComing) {
        this.bulletComing = bulletComing;
    }
}
