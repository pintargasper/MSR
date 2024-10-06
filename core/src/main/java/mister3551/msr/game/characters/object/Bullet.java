package mister3551.msr.game.characters.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Ammunition;

public class Bullet extends Ammunition {

    private final Sprite sprite;
    private final String lastMove;

    private final Weapon weapon;
    private final float startX;

    public Bullet(Body body, String lastMove, Weapon weapon) {
        super(body);
        this.lastMove = lastMove;
        this.speed = 20;
        this.sprite = new Sprite(new Texture("maps/tiles/character/enemy/enemy.png"));
        this.weapon = weapon;
        this.damage = weapon.getDamage();
        this.startX = body.getPosition().x;
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(sprite, x - (width / 2), y - (height / 2), width, height);
    }

    @Override
    public void update() {
        x = body.getPosition().x * Static.PPM;
        y = body.getPosition().y * Static.PPM;
        body.setLinearVelocity(lastMove.equals("left") ? -speed : speed, 0);

        float currentX = body.getPosition().x;
        float distanceTraveled = Math.abs(currentX - startX);

        if (distanceTraveled > weapon.getRange()) {
            Static.getBulletsToRemove().add(this);
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
