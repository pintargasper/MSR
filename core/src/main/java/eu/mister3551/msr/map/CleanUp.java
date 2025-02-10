package eu.mister3551.msr.map;

import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.weapon.bullet.Bullet;

public class CleanUp {

    public void all(String mapName) {
        for (Bullet bullet : Constants.gameScreen.getBulletsToRemove()) {
            Constants.gameScreen.getBodyHelper().destroyBody(bullet.getBody());
            Constants.gameScreen.getBullets().remove(bullet);
        }

        for (Enemy enemy : Constants.gameScreen.getEnemiesToRemove()) {
            Constants.gameScreen.getBodyHelper().destroyBody(enemy.getBody());
            Constants.gameScreen.getGameState().getGameStates().get(mapName).getEnemies().remove(enemy);
        }

        for (Hostage hostage : Constants.gameScreen.getHostageToRemove()) {
            Constants.gameScreen.getBodyHelper().destroyBody(hostage.getBody());
            Constants.gameScreen.getGameState().getGameStates().get(mapName).getHostages().remove(hostage);
        }

        for (Item item : Constants.gameScreen.getItemsToRemove()) {
            Constants.gameScreen.getBodyHelper().destroyBody(item.getBody());
            Constants.gameScreen.getGameState().getGameStates().get(mapName).getItems().remove(item);
        }

        if (Constants.gameScreen.getBullets().isEmpty()) {
            Constants.gameScreen.getBulletsToRemove().clear();
        }

        if (Constants.gameScreen.getGameState().getGameStates().get(mapName).getEnemies().isEmpty()) {
            Constants.gameScreen.getEnemiesToRemove().clear();
        }

        if (Constants.gameScreen.getGameState().getGameStates().get(mapName).getHostages().isEmpty()) {
            Constants.gameScreen.getHostageToRemove().clear();
        }

        if (Constants.gameScreen.getGameState().getGameStates().get(mapName).getItems().isEmpty()) {
            Constants.gameScreen.getItemsToRemove().clear();
        }
    }
}
