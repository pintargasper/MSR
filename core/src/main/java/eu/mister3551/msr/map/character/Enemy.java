package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.ObjectData;
import eu.mister3551.msr.map.character.movement.enemy.Movement;
import eu.mister3551.msr.screen.GameScreen;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
public class Enemy extends Character {

    private final Movement movement;
    private boolean playerDetected;
    private boolean playerPreviouslyDetected;
    private boolean bulletDetected;
    private int shoots;
    private int range;

    public Enemy(ObjectData objectData) {
        super(objectData);
        this.movement = new Movement();
        this.playerDetected = false;
        this.playerPreviouslyDetected = false;
        this.range = 15;
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
            "enemy_jump_left",
            "enemy_jump_right",
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
        bounds.setPosition(new Vector2(x - width / 2, y - height / 2));

        movementCollision = this.collision.check(this);
        movement.move(this, movementCollision, points(), delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }

    @Override
    public void resume() {
        if (animationPaused) {
            elapsedTime = pausedTime;
        }
        animationPaused = false;
    }

    @Override
    public void pause() {
        if (!movementCollision.isWater()) {
            animationPaused = true;
            pausedTime = elapsedTime;
        }
    }

    private ArrayList<Vector2> points() {
        return GameScreen.enemyMovement.entrySet().stream()
            .filter(entry -> entry.getKey().equals(name))
            .flatMap(entry -> entry.getValue().stream())
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
