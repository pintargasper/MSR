package mister3551.msr.game.characters.object;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;
import mister3551.msr.game.characters.CharacterAnimation;
import mister3551.msr.game.controls.Computer;
import mister3551.msr.game.controls.Controller;
import mister3551.msr.game.controls.Mobile;
import mister3551.msr.game.controls.zipline.Zipline;
import mister3551.msr.game.map.ObjectData;

import java.util.*;

public class Player extends Character {

    private final Computer computer;
    private final Mobile mobile;
    private final Controller controller;
    private float elapsedTime = 0;
    private float offset;

    public Player(Body body, Rectangle rectangle, Weapon weapon, ObjectData objectData) {
        super(body, rectangle, weapon, objectData.getWidth(), objectData.getHeight());
        this.speed = 10;
        this.speedOnLadder = 5;
        this.speedOnZipline = 15;
        this.jumps = 1;
        this.computer = new Computer(body, this);
        this.mobile = new Mobile(body, this);
        this.controller = new Controller(body, this);
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
                } else {
                    computer.show();
                }
                break;
            case Android:
                mobile.show();
                break;
            case iOS:
                break;
            case Applet:
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        elapsedTime += delta;
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - (width / 2) - offset, y - (height / 2));
        spriteBatch.end();
        spriteBatch.begin();
        computer.render(delta);
        controller.render(delta);
        mobile.render(delta);
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        rectangle.setPosition(new Vector2(x - width / 2, y - height / 2));
        inputs(delta);

        if (lastMove.equals("left") && (currentAnimation.equals(characterAnimation.getWalkLeft()) || currentAnimation.equals(characterAnimation.getStandLeft()))) {
            offset = 45;
        } else {
            offset = 0;
        }
    }

    @Override
    public void resize(int width, int height) {
        computer.resize(width, height);
        controller.resize(width, height);
        mobile.resize(width, height);
    }

    private void inputs(float delta) {

        switch (Gdx.app.getType()) {
            case Desktop:
            case WebGL:
                if (controller.getConnected()) {
                    controller.inputs(delta, ladderCollision(), stopOnLadder(), waterCollision(), ziplineCollision());
                } else {
                    computer.inputs(delta, ladderCollision(), stopOnLadder(), waterCollision(), ziplineCollision());
                }
                break;
            case Android:
                mobile.inputs(delta, ladderCollision(), stopOnLadder(), waterCollision(), ziplineCollision());
                break;
            case iOS:
                break;
            case Applet:
        }
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        computer.dispose();
        controller.dispose();
        mobile.dispose();
    }

   private boolean ladderCollision() {
        return Static.getLadders().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle));
    }

    private boolean stopOnLadder() {
        float offset = 2;
        return Static.getStopOnLadders().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle)
                || (this.rectangle.y <= rectangle.y + rectangle.height + offset && this.rectangle.y >= rectangle.y + rectangle.height - offset) &&
            (this.rectangle.x < rectangle.x + rectangle.width && this.rectangle.x + this.rectangle.width > rectangle.x));
    }

    private Zipline ziplineCollision() {
        HashMap<String, ArrayList<Vector2>> allPoints = Static.getZiplines();
        float threshold = 1;

        return allPoints.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> body.getPosition().dst(entry.getValue().get(0)) < threshold)
            .findFirst()
            .map(entry -> new Zipline(entry.getValue(), true))
            .orElse(null);
    }

    private boolean waterCollision() {
        return Static.getWaters().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle));
    }
}
