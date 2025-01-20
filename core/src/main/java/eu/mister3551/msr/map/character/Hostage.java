package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.ObjectData;

public class Hostage extends Character {

    public Hostage(ObjectData objectData) {
        super(objectData);
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
        bounds.setPosition(new Vector2(x - width / 2, y - height / 2));
        movementCollision = this.collision.check(this);
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
}
