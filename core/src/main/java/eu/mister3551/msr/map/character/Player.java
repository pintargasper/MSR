package eu.mister3551.msr.map.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.object.ObjectData;
import eu.mister3551.msr.map.character.control.Computer;
import eu.mister3551.msr.map.character.control.Controller;
import eu.mister3551.msr.map.character.control.Mobile;
import eu.mister3551.msr.screen.GameScreen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends Character {

    private final Computer computer;
    private final Controller controller;
    private Mobile mobile;

    public Player(ObjectData objectData) {
        super(objectData);
        this.computer = new Computer(this, Constants.options);
        this.controller = new Controller(this, Constants.options);
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
        switch (Gdx.app.getType()) {
            case Desktop:
            case WebGL:
                if (controller.getConnected()) {
                    controller.show();
                }
                break;
            case Android:
                mobile.show();
            case iOS:
            case Applet:
                break;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        elapsedTime += delta;
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - (width / 2f) - offset, y - (height / 2f));
        spriteBatch.end();
        spriteBatch.begin();
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM;
        bounds.setPosition(new Vector2(x - (float) width / 2, y - (float) height / 2));
        movementCollision = this.collision.check(this, Constants.gameScreen.getMission().getMap());

        switch (Gdx.app.getType()) {
            case Desktop:
            case WebGL:
                if (controller.getConnected()) {
                    controller.update(delta);
                } else {
                    computer.update(delta);
                }
                break;
            case Android:
                mobile.update(delta);
                break;
            case iOS:
            case Applet:
                break;
        }

        offset = lastMove.equals(LastMove.LEFT)
            && (currentAnimation.equals(characterAnimation.getWalkLeft())
            || currentAnimation.equals(characterAnimation.getStandLeft())
            || currentAnimation.equals(characterAnimation.getJumpLeft())) ? 45 : 0;

        if (getLife() == 0) {
            Constants.gameScreen.gameStats = GameScreen.GameStats.FAILED;
        }
    }

    @Override
    public void resize(int width, int height) {
        mobile.resize(width, height);
    }

    @Override
    public void dispose() {
        computer.dispose();
        controller.dispose();
        mobile.dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
