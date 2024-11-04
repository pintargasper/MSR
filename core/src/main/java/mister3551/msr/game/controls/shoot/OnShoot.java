package mister3551.msr.game.controls.shoot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.Character;
import mister3551.msr.game.characters.object.Bullet;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.controls.Device;

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
                || character.getCurrentAnimation() == character.getCharacterAnimation().getWalkRight()
                || character.getCurrentAnimation() == character.getCharacterAnimation().getJumpLeft()
                || character.getCurrentAnimation() == character.getCharacterAnimation().getJumpRight()) {

                float accuracyFactor = 1.0f - (character.getWeapon().getAccuracy() / 100.0f);
                float variance = MathUtils.random(-character.getSpeed() * accuracyFactor, character.getSpeed() * accuracyFactor);

                if (MathUtils.randomBoolean()) {
                    variance = -variance;
                }

                float x = character.getLastMove().equals("left") ? character.getX() - character.getWidth() - character.getWidth() / 3 : character.getX() + character.getWidth() + character.getWidth() / 3;
                Body body = Static.getBodyHelper().body("Bullet", 5, 5, x, (character.getY() - 5) + variance, false);
                body.setUserData("bullet");
                Static.getBullets().add(new Bullet(body, character.getLastMove(), character.getWeapon()));

                if (character instanceof Player) {
                    Static.getStatistics().setAmmoCosts(Static.getStatistics().getAmmoCosts() + 12);
                }
                return 1;
            }
        }
        return 0;
    }

    public void reload(Device device, Player player) {
        reload(device, player, null, null, null);
    }

    public void reload(Device device, Player player, Image image, Drawable drawable1, Drawable drawable2) {
        device.setShooting(false);
        player.setReloading(true);

        if (image != null && drawable1 != null) {
            image.setDrawable(drawable1);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                device.setShooting(true);
                player.setReloading(false);

                int ammoNeeded = player.getWeapon().getMagazineCapacity() - player.getWeapon().getActiveMagazineCapacity();
                int ammoToReload = Math.min(ammoNeeded, player.getWeapon().getBackupMagazinesCapacity());

                player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() + ammoToReload);
                player.getWeapon().setBackupMagazinesCapacity(player.getWeapon().getBackupMagazinesCapacity() - ammoToReload);

                if (image != null && drawable2 != null) {
                    image.setDrawable(drawable2);
                }
            }
        }, 3);
    }
}
