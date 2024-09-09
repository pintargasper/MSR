package mister3551.msr.game.characters;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.controls.Computer;
import mister3551.msr.game.controls.Mobile;
import mister3551.msr.game.controls.zipline.Zipline;
import mister3551.msr.game.map.ObjectData;

import java.util.*;

public class Player extends Character {

    private final Sprite sprite;
    private final Computer computer;
    private final Mobile mobile;

    public Player(Body body, Rectangle rectangle, ObjectData objectData) {
        super(body, rectangle, objectData.getWidth(), objectData.getHeight());
        this.sprite = new Sprite(objectData.getTextureRegion());
        this.computer = new Computer(body, 10, 5, 15);
        this.mobile = new Mobile(body, 10, 5, 15);
    }

    @Override
    public void show() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            mobile.show();
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(sprite, x - (width / 2), y - (height / 2), width, height);
        spriteBatch.end();
        spriteBatch.begin();
        mobile.render(delta);
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        rectangle.setPosition(new Vector2(x - (width / 2), y - (height / 2)));
        inputs();
    }

    @Override
    public void resize(int width, int height) {
        mobile.resize(width, height);
    }

    private void inputs() {
        switch (Gdx.app.getType()) {
            case Desktop:
            case WebGL:
                computer.inputs(ladderCollision(), ziplineCollision());
                break;
            case Android:
                mobile.inputs(ladderCollision(), ziplineCollision());
                break;
            case iOS:
                break;
        }    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        mobile.dispose();
    }

    private boolean ladderCollision() {
        return Static.getLadders().stream().anyMatch(rectangle -> this.rectangle.overlaps(rectangle));
    }

    private Zipline ziplineCollision() {
        HashMap<String, ArrayList<Vector2>> allPoints = Static.getZiplines();
        float threshold = 1.0f;

        return allPoints.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .filter(entry -> body.getPosition().dst(entry.getValue().get(0)) < threshold)
            .findFirst()
            .map(entry -> new Zipline(entry.getValue(), true))
            .orElse(null);
    }
}
