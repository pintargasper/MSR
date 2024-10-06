package mister3551.msr.game.characters.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;
import mister3551.msr.game.characters.CharacterAnimation;
import mister3551.msr.game.controls.movement.enemy.MovementAI;
import mister3551.msr.game.map.ObjectData;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Enemy extends Character {

    private final MovementAI movementAI;
    private final String name;
    private float elapsedTime = 0;
    private boolean playerDetected = false;

    public Enemy(Body body, Rectangle rectangle, Weapon weapon, ObjectData objectData, String name) {
        super(body, rectangle, weapon, objectData.getWidth(), objectData.getHeight());
        this.movementAI = new MovementAI();
        this.name = name;
        this.speed = 5;
        this.speedOnLadder = 5;
        this.speedOnZipline = 15;
        this.jumps = 1;
        this.textureAtlas = new TextureAtlas("maps/tiles/character/enemy/atlas/enemy.atlas");
        this.characterAnimation = new CharacterAnimation(
            textureAtlas,
            "enemy",
            "enemy_climb_stand",
            "enemy_stand_left_arm",
            "enemy_stand_right_arm",
            "enemy_walk_left1_arm",
            "enemy_walk_left2_arm",
            "enemy_walk_right1_arm",
            "enemy_walk_right2_arm",
            "enemy_climb1",
            "enemy_climb2",
            "enemy_fly_left",
            "enemy_fly_right",
            "enemy_swim_left1",
            "enemy_swim_left2",
            "enemy_swim_right1",
            "enemy_swim_right2"
        );
        setCurrentAnimation(characterAnimation.getStanding());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        elapsedTime += delta;
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - (width / 2) - offset, y - (height / 2));
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        rectangle.setPosition(new Vector2(x - width / 2, y - height / 2));
        movementAI.movement(this, points(), ladderCollision(), waterCollision(), playerDetected, delta, speed);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }

    public ArrayList<Vector2> points() {
        return Static.getEnemyMovement().entrySet().stream()
            .filter(entry -> entry.getKey().equals(name))
            .flatMap(entry -> entry.getValue().stream())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean ladderCollision() {
        float tolerance = 5f;
        return Static.getLadders().stream().anyMatch(ladder -> {
            if (this.rectangle.overlaps(ladder)) {
                float ladderCenterX = ladder.x + ladder.width / 2;
                return Math.abs(this.rectangle.x + this.rectangle.width / 2 - ladderCenterX) <= tolerance;
            }
            return false;
        });
    }

    private boolean stopOnLadder() {
        float offset = 2;
        return Static.getStopOnLadders().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle)
            || (this.rectangle.y <= rectangle.y + rectangle.height + offset && this.rectangle.y >= rectangle.y + rectangle.height - offset) &&
            (this.rectangle.x < rectangle.x + rectangle.width && this.rectangle.x + this.rectangle.width > rectangle.x));
    }

    private boolean waterCollision() {
        return Static.getWaters().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle));
    }

    public String getName() {
        return name;
    }

    public boolean isPlayerDetected() {
        return playerDetected;
    }

    public void setPlayerDetected(boolean playerDetected) {
        this.playerDetected = playerDetected;
    }
}
