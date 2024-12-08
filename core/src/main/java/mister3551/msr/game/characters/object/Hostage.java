package mister3551.msr.game.characters.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;
import mister3551.msr.game.characters.CharacterAnimation;
import mister3551.msr.game.map.ObjectData;

public class Hostage extends Character {

    public Hostage(Body body, Rectangle rectangle, ObjectData objectData, String name, String type, String group) {
        super(body, rectangle, null, objectData.getWidth(), objectData.getHeight());
        this.name = name;
        this.type = type;
        this.group = group;
        this.textureAtlas = new TextureAtlas("maps/tiles/character/hostage/atlas/hostage.atlas");
        this.characterAnimation = new CharacterAnimation(
            textureAtlas,
            "hostage",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
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
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
    }
}
