package eu.mister3551.msr.map;

import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.bullet.Bullet;
import eu.mister3551.msr.screen.GameScreen;

public class CleanUp {

    public void all(String mapName) {
        for (Bullet bullet : GameScreen.bulletsToRemove) {
            Static.bodyHelper.destroyBody(bullet.getBody());
            GameScreen.bullets.remove(bullet);
        }

        for (Enemy enemy : GameScreen.enemiesToRemove) {
            Static.bodyHelper.destroyBody(enemy.getBody());
            Static.gameState.get(mapName).getEnemies().remove(enemy);
        }

        for (Hostage hostage : GameScreen.hostagesToRemove) {
            Static.bodyHelper.destroyBody(hostage.getBody());
            Static.gameState.get(mapName).getHostages().remove(hostage);
        }

        for (Item item : GameScreen.itemsToRemove) {
            Static.bodyHelper.destroyBody(item.getBody());
            Static.gameState.get(mapName).getItems().remove(item);
        }

        if (GameScreen.bullets.isEmpty()) {
            GameScreen.bulletsToRemove.clear();
        }
    }
}
