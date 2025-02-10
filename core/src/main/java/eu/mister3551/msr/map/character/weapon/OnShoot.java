package eu.mister3551.msr.map.character.weapon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.BodyUserData;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.control.Device;
import eu.mister3551.msr.map.character.weapon.bullet.Bullet;

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

                float x = character.getLastMove().equals(Character.LastMove.LEFT) ?
                    character.getX() - character.getWidth() - character.getWidth() / 3f :
                    character.getX() + character.getWidth() + character.getWidth() / 3f;

                String shooterType = (character instanceof Player) ? "player" : "enemy";
                Body body = Constants.gameScreen.getBodyHelper().body(new BodyUserData("bullet", shooterType), 5, 5, x, (character.getY() - 5) + variance, false);

                if (shooterType.equals("player")) {
                    Constants.statistics.setShotsFired(Constants.statistics.getShotsFired() + 1);
                    float currentAmmoCosts = Constants.statistics.getMoney().getOrDefault("ammoCosts", 0.0f);
                    float newAmmoCosts = currentAmmoCosts + character.getWeapon().getAmmunitionCosts();
                    Constants.statistics.getMoney().put("ammoCosts", newAmmoCosts);
                }
                Constants.gameScreen.getBullets().add(new Bullet(body, character, shooterType));
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
        device.setReloading(true);

        if (image != null && drawable1 != null) {
            image.setDrawable(drawable1);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                device.setShooting(true);
                device.setReloading(false);

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
