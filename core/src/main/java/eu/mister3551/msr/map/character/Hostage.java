package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.object.ObjectData;

public class Hostage extends Character {

    public Hostage(ObjectData objectData) {
        super(objectData);
        this.textureAtlas = new TextureAtlas("maps/tiles/character/hostage/atlas/hostage.atlas");
        this.characterAnimation = new CharacterAnimation(textureAtlas, "hostage");
        setCurrentAnimation(characterAnimation.getStanding());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        elapsedTime += delta;
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - (width / 2f), y - (height / 2f));
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM;
        bounds.setPosition(new Vector2(x - width / 2f, y - height / 2f));
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
}
