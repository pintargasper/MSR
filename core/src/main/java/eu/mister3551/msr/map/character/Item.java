package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.object.ObjectData;

public class Item extends Character {

    private final TextureRegion textureRegion;

    public Item(ObjectData objectData) {
        super(objectData);
        this.textureAtlas = new TextureAtlas("maps/tiles/character/items/atlas/items.atlas");
        this.textureRegion = textureAtlas.findRegion(objectData.getName().toLowerCase());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(textureRegion, x - (width / 2f) - offset, y - (height / 2f));
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
