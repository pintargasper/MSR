package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.ObjectData;
import eu.mister3551.msr.map.character.control.Computer;

public class Player extends Character {

    private final Computer computer;

    public Player(ObjectData objectData) {
        super(objectData);
        this.computer = new Computer(this, Static.options);
        this.textureAtlas = new TextureAtlas("maps/tiles/character/player/atlas/player.atlas");
        this.characterAnimation = new CharacterAnimation(
            textureAtlas,
            "player",
            "player_climb_stand",
            "player_stand_left_arm",
            "player_stand_right_arm",
            "player_walk_left1_arm",
            "player_walk_left2_arm",
            "player_walk_right1_arm",
            "player_walk_right2_arm",
            "player_climb1",
            "player_climb2",
            "player_fly_left",
            "player_fly_right",
            "player_jump_left",
            "player_jump_right",
            "player_swim_left1",
            "player_swim_left2",
            "player_swim_right1",
            "player_swim_right2"
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
        spriteBatch.end();
        spriteBatch.begin();
        computer.render(delta);
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        bounds.setPosition(new Vector2(x - width / 2, y - height / 2));

        computer.inputs(delta, collision.check(this));

        offset = lastMove.equals(LastMove.LEFT)
            && (currentAnimation.equals(characterAnimation.getWalkLeft())
            || currentAnimation.equals(characterAnimation.getStandLeft())
            || currentAnimation.equals(characterAnimation.getJumpLeft())) ? 45 : 0;
    }

    @Override
    public void resize(int width, int height) {
        computer.resize(width, height);
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        computer.dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
