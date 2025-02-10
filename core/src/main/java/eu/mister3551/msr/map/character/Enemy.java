package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.object.ObjectData;
import eu.mister3551.msr.map.character.movement.enemy.Movement;
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
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - (width / 2f) - offset, y - (height / 2f));
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM;
        bounds.setPosition(new Vector2(x - width / 2f, y - height / 2f));
        movementCollision = this.collision.check(this, Constants.gameScreen.getMission().getMap());
        movement.move(this, points(), delta);
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

    }

    @Override
    public void pause() {

    }

    private ArrayList<Vector2> points() {
        return Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getEnemyMovement().entrySet().stream()
            .filter(entry -> entry.getKey().equals(name))
            .flatMap(entry -> entry.getValue().stream())
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
