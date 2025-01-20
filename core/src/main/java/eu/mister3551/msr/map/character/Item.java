package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.ObjectData;

public class Item extends Character {

    private final TextureRegion textureRegion;

    public Item(ObjectData objectData) {
        super(objectData);
        this.textureAtlas = new TextureAtlas("maps/tiles/character/items/atlas/items.atlas");
        this.textureRegion = textureAtlas.findRegion(name.toLowerCase());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(textureRegion, x - (width / 2) - offset, y - (height / 2));
    }

    @Override
    public void update(float delta) {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        bounds.setPosition(new Vector2(x - width / 2, y - height / 2));
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
