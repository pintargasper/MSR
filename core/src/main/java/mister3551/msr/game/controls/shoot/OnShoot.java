package mister3551.msr.game.controls.shoot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;
import mister3551.msr.game.characters.object.Bullet;

public class OnShoot {
    private float lastShotTime = 0;

    public int shoot(Character character, float deltaTime) {
        int fireRate = character.getWeapon().getFireRate();
        float shootInterval = 60f / fireRate;

        lastShotTime += deltaTime;

        if (lastShotTime >= shootInterval) {
            lastShotTime = 0;

            if (character.getCurrentAnimation() == character.getCharacterAnimation().getStandLeft()
                || character.getCurrentAnimation() == character.getCharacterAnimation().getStandRight()
                || character.getCurrentAnimation() == character.getCharacterAnimation().getWalkLeft()
                || character.getCurrentAnimation() == character.getCharacterAnimation().getWalkRight()) {

                float accuracyFactor = 1.0f - (character.getWeapon().getAccuracy() / 100.0f);
                float variance = MathUtils.random(-character.getSpeed() * accuracyFactor, character.getSpeed() * accuracyFactor);

                if (MathUtils.randomBoolean()) {
                    variance = -variance;
                }

                float x = character.getLastMove().equals("left") ? character.getX() - character.getWidth() - character.getWidth() / 3 : character.getX() + character.getWidth() + character.getWidth() / 3;
                Body body = Static.getBodyHelper().body("Bullet", 5, 5, x, (character.getY() - 5) + variance, false);
                body.setUserData("bullet");
                Static.getBullets().add(new Bullet(body, character.getLastMove(), character.getWeapon()));
                return 1;
            }
        }
        return 0;
    }
}
