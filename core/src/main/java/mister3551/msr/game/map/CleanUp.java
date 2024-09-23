package mister3551.msr.game.map;

import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Bullet;

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
}
