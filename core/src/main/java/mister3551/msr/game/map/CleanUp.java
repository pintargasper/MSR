package mister3551.msr.game.map;

import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Bullet;
import mister3551.msr.game.characters.object.Enemy;
import mister3551.msr.game.characters.object.Hostage;
import mister3551.msr.game.characters.object.Item;

public class CleanUp {

    public void cleanUpBullets() {
        for (Bullet bullet : Static.getBulletsToRemove()) {
            Static.getBodyHelper().destroyBody(bullet.getBody());
            Static.getBullets().remove(bullet);
        }

        if (Static.getBullets().isEmpty()) {
            Static.getBulletsToRemove().clear();
        }
    }

    public void cleanUpCharacters() {
        for (Enemy enemy : Static.getEnemiesToRemove()) {
            Static.getBodyHelper().destroyBody(enemy.getBody());
            Static.getEnemies().remove(enemy);
        }

        for (Hostage hostage : Static.getHostagesToRemove()) {
            Static.getBodyHelper().destroyBody(hostage.getBody());
            Static.getHostages().remove(hostage);
        }

        if (Static.getEnemies().isEmpty()) {
            Static.getEnemiesToRemove().clear();
        }

        if (Static.getHostages().isEmpty()) {
            Static.getHostagesToRemove().clear();
        }
    }

    public void cleanUpItems() {
        for (Item item : Static.getItemsToRemove()) {
            Static.getBodyHelper().destroyBody(item.getBody());
        }

        if (Static.getItems().isEmpty()) {
            Static.getItemsToRemove().clear();
        }
    }
}
