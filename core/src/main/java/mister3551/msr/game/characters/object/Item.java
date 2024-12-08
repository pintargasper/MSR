package mister3551.msr.game.characters.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;

public class Item extends Character {

    private final TextureRegion textureRegion;

    public Item(String name, Body body, Rectangle rectangle, float width, float height) {
        super(body, rectangle, null, width, height);
        this.name = name;
        this.textureAtlas = new TextureAtlas("maps/tiles/character/items/atlas/items.atlas");
        this.textureRegion = textureAtlas.findRegion(name);
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
        rectangle.setPosition(new Vector2(x - width / 2, y - height / 2));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
