package eu.mister3551.msr.map.character.weapon.bullet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.weapon.Weapon;
import eu.mister3551.msr.screen.GameScreen;

public class Bullet extends Ammunition {

    private final Sprite sprite;
    private final Character.LastMove lastMove;
    private final Weapon weapon;
    private final float startX;

    public Bullet(Body body, Character character, String owner) {
        super(body, owner);
        this.lastMove = character.getLastMove();
        this.speed = 20;
        this.sprite = new Sprite(new TextureRegion(Constants.skin.getRegion("bullet")));
        this.weapon = character.getWeapon();
        this.damage = weapon.getDamage();
        this.startX = body.getPosition().x;
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.draw(sprite, x - (width / 2), y - (height / 2), width / 2, height / 2, width, height, 1, 1, sprite.getRotation());
    }

    @Override
    public void update() {
        x = body.getPosition().x * Constants.PPM;
        y = body.getPosition().y * Constants.PPM;
        body.setLinearVelocity(lastMove.equals(Character.LastMove.LEFT) ? -speed : speed, 0);
        sprite.setRotation(lastMove.equals(Character.LastMove.LEFT) ? 180 : 0);

        float currentX = body.getPosition().x;
        float distanceTraveled = Math.abs(currentX - startX);

        if (distanceTraveled > weapon.getRange()) {
            Constants.gameScreen.getBulletsToRemove().add(this);
        }
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
