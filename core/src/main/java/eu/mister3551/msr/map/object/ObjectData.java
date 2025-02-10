package eu.mister3551.msr.map.object;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import eu.mister3551.msr.map.character.weapon.Weapon;
import lombok.Getter;

@Getter
public class ObjectData {

    private final Body body;
    private final Rectangle bounds;
    private final Weapon weapon;
    private final int height;
    private final int width;
    private final int life;
    private final int speed;
    private final int speedOnLadder;
    private final int speedOnZipLine;
    private final float award;
    private final String name;
    private final String type;
    private final String group;
    private final String lastMove;

    public ObjectData(Body body, Rectangle bounds, Weapon weapon, int height, int width, int life, int speed, int speedOnLadder, int speedOnZipLine, float award, String name, String type, String group, String lastMove) {
        this.body = body;
        this.bounds = bounds;
        this.weapon = weapon;
        this.height = height;
        this.width = width;
        this.life = life;
        this.speed = speed;
        this.speedOnLadder = speedOnLadder;
        this.speedOnZipLine = speedOnZipLine;
        this.award = award;
        this.name = name;
        this.type = type;
        this.group = group;
        this.lastMove = lastMove;
    }
}
