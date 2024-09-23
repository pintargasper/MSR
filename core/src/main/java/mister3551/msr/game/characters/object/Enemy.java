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

public class Enemy extends Character {

    private float elapsedTime = 0;

    public Enemy(Body body, Rectangle rectangle, Weapon weapon, ObjectData objectData) {
        super(body, rectangle, weapon, objectData.getWidth(), objectData.getHeight());
        this.speed = 10;
        this.speedOnLadder = 5;
        this.speedOnZipline = 15;
        this.jumps = 1;
        this.textureAtlas = new TextureAtlas("maps/tiles/character/enemy/atlas/enemy.atlas");
        this.characterAnimation = new CharacterAnimation(
            textureAtlas,
            "enemy",
            "enemy_climb_stand",
            "enemy_stand_left",
            "enemy_stand_right",
            "enemy_walk_left1",
            "enemy_walk_left2",
            "enemy_walk_right1",
            "enemy_walk_right2",
            "enemy_climb1",
            "enemy_climb2",
            "enemy_fly_left",
            "enemy_fly_right",
            "enemy_swim_left1",
            "enemy_swim_left2",
            "enemy_swim_right1",
            "enemy_swim_right2"
        );
        setCurrentAnimation(characterAnimation.getStanding());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        elapsedTime += delta;
        spriteBatch.draw(getCurrentAnimation().getKeyFrame(elapsedTime, true), x - width / 2, y - height / 2);
        spriteBatch.end();
        spriteBatch.begin();
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
