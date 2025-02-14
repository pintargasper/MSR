package eu.mister3551.msr.map;

import com.badlogic.gdx.physics.box2d.*;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.*;
import eu.mister3551.msr.map.character.weapon.bullet.Bullet;

public class WorldCollision implements ContactListener {

    private final Player player;
    private int groundPlayerContacts;
    private int leftPlayerSideContacts;
    private int rightPlayerSideContacts;

    public WorldCollision(Player player) {
        this.player = player;
        this.groundPlayerContacts = 0;
        this.leftPlayerSideContacts = 0;
        this.rightPlayerSideContacts = 0;
    }

    @Override
    public void beginContact(Contact contact) {

        if (isContact(contact, "player-jump")) {
            groundPlayerContacts++;
            player.setJumping(false);
        }

        if (isContact(contact, "player-left-side")) {
            leftPlayerSideContacts++;
            player.setLeftSide(true);
        }

        if (isContact(contact, "player-right-side")) {
            rightPlayerSideContacts++;
            player.setRightSide(true);
        }

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getEnemies()) {
            BodyUserData userData = (BodyUserData) enemy.getBody().getUserData();

            if (userData != null && isContact(contact, userData.getSensors()[0]) && !enemy.isProcessed()) {
                String enemyType = enemy.getType().replaceAll("\\d+$", "").toLowerCase();
                Constants.statistics.getEnemyTypesKilled().put(enemyType, Constants.statistics.getEnemyTypesKilled().getOrDefault(enemyType, 0) + 1);
                Constants.statistics.getMoney().put("enemies", Constants.statistics.getMoney().getOrDefault("enemies", 0.0f) + enemy.getAward());
                Constants.statistics.setScore((int) (Constants.statistics.getScore() + enemy.getAward()));
                Constants.statistics.setHeadshots(Constants.statistics.getHeadshots() + 1);
                enemy.turnOffLight();
                enemy.setProcessed(true);
                Constants.gameScreen.getEnemiesToRemove().add(enemy);
            }

            if (userData != null && isContact(contact, userData.getSensors()[3])) {
                enemy.setJumping(false);
            }

            if (userData != null && isContact(contact, userData.getSensors()[1])) {
                enemy.setLeftSide(true);
            }

            if (userData != null && isContact(contact, userData.getSensors()[2])) {
                enemy.setRightSide(true);
            }

            if (userData != null && isContact(contact, userData.getSensors()[4])) {
                enemy.setLeftOffset(false);
            }

            if (userData != null && isContact(contact, userData.getSensors()[5])) {
                enemy.setRightOffset(false);
            }
        }
        processCollision(contact);
    }

    @Override
    public void endContact(Contact contact) {
        if (isContact(contact, "player-jump")) {
            groundPlayerContacts = Math.max(0, groundPlayerContacts - 1);
            if (groundPlayerContacts == 0) {
                player.setJumping(true);
            }
        }

        if (isContact(contact, "player-left-side")) {
            leftPlayerSideContacts = Math.max(0, leftPlayerSideContacts - 1);
            if (leftPlayerSideContacts == 0) {
                player.setLeftSide(false);
            }
        }

        if (isContact(contact, "player-right-side")) {
            rightPlayerSideContacts = Math.max(0, rightPlayerSideContacts - 1);
            if (rightPlayerSideContacts == 0) {
                player.setRightSide(false);
            }
        }

        for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getEnemies()) {
            BodyUserData userData = (BodyUserData) enemy.getBody().getUserData();

            if (userData != null && isContact(contact, userData.getSensors()[3])) {
                enemy.setJumping(true);
            }

            if (userData != null && isContact(contact, userData.getSensors()[1])) {
                enemy.setLeftSide(false);
            }

            if (userData != null && isContact(contact, userData.getSensors()[2])) {
                enemy.setRightSide(false);
            }

            if (userData != null && isContact(contact, userData.getSensors()[4])) {
                enemy.setLeftOffset(true);
            }

            if (userData != null && isContact(contact, userData.getSensors()[5])) {
                enemy.setRightOffset(true);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void processCollision(Contact contact) {
        Object userDataA = contact.getFixtureA().getBody().getUserData();
        Object userDataB = contact.getFixtureB().getBody().getUserData();

        if (contact.getFixtureA().getBody().getUserData() != null
            && "bullet".equals(((BodyUserData) contact.getFixtureA().getBody().getUserData()).getName())
            && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody
            || contact.getFixtureB().getBody().getUserData() != null
            && "bullet".equals(((BodyUserData) contact.getFixtureB().getBody().getUserData()).getName())
            && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody) {
            handleBulletCollision(contact);
        }

        if (userDataA == null || userDataB == null) {
            return;
        }

        if (userDataA instanceof BodyUserData && userDataB instanceof BodyUserData) {
            userDataA = ((BodyUserData) userDataA).getName();
            userDataB = ((BodyUserData) userDataB).getName();


            if ("enemy1-head".equals(userDataA) && "enemy1-head".equals(userDataB)) {
                System.out.println("enemy");
            }

            if ("bullet".equals(userDataA) && "bullet".equals(userDataB)) {
                handleBulletCollision(contact);
            }

            if ("player".equals(userDataA) && "bullet".equals(userDataB)
                || "player".equals(userDataB) && "bullet".equals(userDataA)) {
                handleBulletCollision(contact);

                int bulletDamage = 0;
                for (Bullet bullet : Constants.gameScreen.getBullets()) {
                    String bulletName = ((BodyUserData) bullet.getBody().getUserData()).getName();
                    if (userDataA.equals(bulletName) || userDataB.equals(bulletName)) {
                        bulletDamage = bullet.getDamage();
                        Constants.gameScreen.getBullets().remove(bullet);
                        break;
                    }
                }
                player.setLife(Math.max(0, player.getLife() - bulletDamage));
            }

            if ((userDataA.toString().matches("enemy\\d*") && "bullet".equals(userDataB))
                || (userDataB.toString().matches("enemy\\d*") && "bullet".equals(userDataA))) {
                handleBulletCollision(contact);

                for (Enemy enemy : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getEnemies()) {
                    if ((enemy.getName().equals(userDataA) || enemy.getName().equals(userDataB)) && !enemy.isProcessed()) {

                        int bulletDamage = 0;
                        for (Bullet bullet : Constants.gameScreen.getBullets()) {
                            String bulletName = ((BodyUserData) bullet.getBody().getUserData()).getName();
                            if (userDataA.equals(bulletName) || userDataB.equals(bulletName)) {
                                bulletDamage = bullet.getDamage();
                                if (bullet.getOwner().equals("player")) {
                                    Constants.statistics.setShotsHit(Constants.statistics.getShotsHit() + 1);

                                    if (player.getLife() < 40) {
                                        Constants.statistics.setCriticalHits(Constants.statistics.getCriticalHits() + 1);
                                        player.setLife(player.getLife() + bulletDamage / 2);
                                    }
                                }
                                Constants.gameScreen.getBullets().remove(bullet);
                                break;
                            }
                        }

                        if (enemy.getLife() > 0) {
                            enemy.setLife(enemy.getLife() - bulletDamage);
                            return;
                        }

                        String enemyType = enemy.getType().replaceAll("\\d+$", "").toLowerCase();
                        Constants.statistics.getEnemyTypesKilled().put(enemyType, Constants.statistics.getEnemyTypesKilled().getOrDefault(enemyType, 0) + 1);
                        Constants.statistics.getMoney().put("enemies", Constants.statistics.getMoney().getOrDefault("enemies", 0.0f) + enemy.getAward());
                        Constants.statistics.setScore((int) (Constants.statistics.getScore() + enemy.getAward()));
                        enemy.turnOffLight();
                        enemy.setProcessed(true);
                        Constants.gameScreen.getEnemiesToRemove().add(enemy);
                    }
                }
            }

            if ((userDataA.toString().matches("hostage\\d*") && "bullet".equals(userDataB))
                || (userDataB.toString().matches("hostage\\d*") && "bullet".equals(userDataA))) {
                handleBulletCollision(contact);

                for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getHostages()) {
                    if ((hostage.getName().equals(userDataA) || hostage.getName().equals(userDataB)) && !hostage.isProcessed()) {

                        int bulletDamage = 0;
                        for (Bullet bullet : Constants.gameScreen.getBullets()) {
                            String bulletName = ((BodyUserData) bullet.getBody().getUserData()).getName();
                            if (userDataA.equals(bulletName) || userDataB.equals(bulletName)) {
                                bulletDamage = bullet.getDamage();
                                if (bullet.getOwner().equals("player")) {
                                    Constants.statistics.setShotsHit(Constants.statistics.getShotsHit() + 1);
                                }
                                Constants.gameScreen.getBullets().remove(bullet);
                                break;
                            }
                        }

                        if (hostage.getLife() > 0) {
                            hostage.setLife(hostage.getLife() - bulletDamage);
                            return;
                        }

                        String hostageType = hostage.getType().replaceAll("\\d+$", "").toLowerCase();
                        Constants.statistics.getHostageTypesKilled().put(hostageType, Constants.statistics.getHostageTypesKilled().getOrDefault(hostageType, 0) + 1);
                        hostage.turnOffLight();
                        hostage.setProcessed(true);
                        Constants.gameScreen.getHostageToRemove().add(hostage);
                    }
                }
            }

            if ((userDataA.toString().matches("items\\d*") && "bullet".equals(userDataB))
                || (userDataB.toString().matches("items\\d*") && "bullet".equals(userDataA))) {
                handleBulletCollision(contact);

                for (Item item : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getItems()) {
                    int bulletDamage = 0;
                    for (Bullet bullet : Constants.gameScreen.getBullets()) {
                        String bulletName = ((BodyUserData) bullet.getBody().getUserData()).getName();
                        if (userDataA.equals(bulletName) || userDataB.equals(bulletName)) {
                            bulletDamage = bullet.getDamage();
                            if (bullet.getOwner().equals("player")) {
                                Constants.statistics.setShotsHit(Constants.statistics.getShotsHit() + 1);
                            }
                            Constants.gameScreen.getBullets().remove(bullet);
                            break;
                        }
                    }

                    if (item.getLife() > 0) {
                        item.setLife(item.getLife() - bulletDamage);
                        return;
                    }
                    item.setProcessed(true);
                    Constants.gameScreen.getItemsToRemove().add(item);
                }
            }

            if (userDataA.toString().matches("items\\d*") && "player".equals(userDataB)
                || (userDataB.toString().matches("items\\d*") && "player".equals(userDataA))) {

                for (Item item : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getItems()) {
                    if ((item.getBody() == contact.getFixtureA().getBody() || item.getBody() == contact.getFixtureB().getBody()) && !item.isProcessed()) {

                        if (item.getName().equals("heart")) {
                            player.setLife(player.getLife() + item.getLife());
                        } else if (item.getName().equals("ammunition")) {
                            int backupCapacity = player.getWeapon().getBackupMagazinesCapacity();
                            int magazineCapacity = player.getWeapon().getMagazineCapacity();
                            int neededBullets = magazineCapacity - backupCapacity;
                            if (neededBullets > 0) {
                                player.getWeapon().setBackupMagazinesCapacity(backupCapacity + neededBullets);
                            }
                        }
                        item.setProcessed(true);
                        Constants.gameScreen.getItemsToRemove().add(item);
                    }
                }
            }

            if (userDataA.toString().matches("hostage\\d*") && "player".equals(userDataB)
                || (userDataB.toString().matches("hostage\\d*") && "player".equals(userDataA))) {

                for (Hostage hostage : Constants.screenChanger.getGameState().getGameStates().get(Constants.gameScreen.getMission().getMap()).getHostages()) {
                    if (hostage.getName().equals(userDataA) || hostage.getName().equals(userDataB)) {
                        String hostageGroup = hostage.getGroup().replaceAll("\\d+$", "").toLowerCase();
                        Constants.statistics.getItemsCollected().put(hostageGroup, Constants.statistics.getItemsCollected().getOrDefault(hostageGroup, 0) + 1);
                        Constants.statistics.getMoney().put("hostages", Constants.statistics.getMoney().getOrDefault("hostages", 0.0f) + hostage.getAward());
                        Constants.statistics.setScore((int) (Constants.statistics.getScore() + hostage.getAward()));
                        Constants.gameScreen.getHostageToRemove().add(hostage);
                    }
                }
            }
        }
    }

    private void handleBulletCollision(Contact contact) {
        Bullet bulletToRemove = null;
        for (Bullet bullet : Constants.gameScreen.getBullets()) {
            if (bullet.getBody() == contact.getFixtureA().getBody() || bullet.getBody() == contact.getFixtureB().getBody()) {
                bulletToRemove = bullet;
                break;
            }
        }
        if (bulletToRemove != null) {
            Constants.gameScreen.getBulletsToRemove().add(bulletToRemove);
        }
    }


    private boolean isContact(Contact contact, String userData) {
        return isFixtureWithUserData(contact.getFixtureA(), userData) ||
            isFixtureWithUserData(contact.getFixtureB(), userData);
    }

    private boolean isFixtureWithUserData(Fixture fixture, String userData) {
        return fixture != null && userData.equals(fixture.getUserData());
    }
}
