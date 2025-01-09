package eu.mister3551.msr.map;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class ObjectData {

    private final Body body;
    private final Rectangle bounds;
    private final Object weapon;
    private final ConeLight flashlight;
    private final PointLight pointLight;
    private final String name;
    private final String group;
    private final String skin;
    private final String type;
    private final Float award;
    private final Float penalty;
    private final int speed;
    private final int speedOnLadder;
    private final int speedOnZipline;
    private final int jumps;
    private final int life;
    private final int height;
    private final int width;
    private final float positionX;
    private final float positionY;

    public ObjectData(Body body, Rectangle bounds, Object weapon, ConeLight flashlight, PointLight pointLight, String name, String group, String skin, String type, Float award, Float penalty, int speed, int speedOnLadder, int speedOnZipline, int jumps, int life, int height, int width, float positionX, float positionY) {
        this.body = body;
        this.bounds = bounds;
        this.weapon = weapon;
        this.flashlight = flashlight;
        this.pointLight = pointLight;
        this.name = name;
        this.group = group;
        this.skin = skin;
        this.type = type;
        this.award = award;
        this.penalty = penalty;
        this.speed = speed;
        this.speedOnLadder = speedOnLadder;
        this.speedOnZipline = speedOnZipline;
        this.jumps = jumps;
        this.life = life;
        this.height = height;
        this.width = width;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Object getWeapon() {
        return weapon;
    }

    public ConeLight getFlashlight() {
        return flashlight;
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getSkin() {
        return skin;
    }

    public String getType() {
        return type;
    }

    public Float getAward() {
        return award;
    }

    public Float getPenalty() {
        return penalty;
    }

    public int getSpeed() {
        return speed;
    }

    public int getSpeedOnLadder() {
        return speedOnLadder;
    }

    public int getSpeedOnZipline() {
        return speedOnZipline;
    }

    public int getJumps() {
        return jumps;
    }

    public int getLife() {
        return life;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }
}
