package eu.mister3551.msr.map.character.weapon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.bullet.Bullet;
import eu.mister3551.msr.map.character.control.Device;
import eu.mister3551.msr.screen.GameScreen;

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
                    character.getX() - character.getWidth() - character.getWidth() / 3 :
                    character.getX() + character.getWidth() + character.getWidth() / 3;

                Body body = Static.bodyHelper.body("Bullet", 5, 5, x, (character.getY() - 5) + variance, false);
                body.setUserData("bullet");

                String shooterType = (character instanceof Player) ? "Player" : "Enemy";

                if (shooterType.equals("Player")) {
                    Static.statistics.setShotsFired(Static.statistics.getShotsFired() + 1);
                    float currentAmmoCosts = Static.statistics.getMoney().getOrDefault("ammoCosts", 0.0f);
                    float newAmmoCosts = currentAmmoCosts + character.getWeapon().getAmmunitionCosts();
                    Static.statistics.getMoney().put("ammoCosts", newAmmoCosts);
                }
                GameScreen.bullets.add(new Bullet(body, character, shooterType));
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
