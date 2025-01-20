package eu.mister3551.msr.map.character.collision;

import com.badlogic.gdx.physics.box2d.*;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Enemy;
import eu.mister3551.msr.map.character.Hostage;
import eu.mister3551.msr.map.character.Item;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.bullet.Bullet;
import eu.mister3551.msr.screen.GameScreen;

import java.util.Objects;

public class WorldCollision {

    private final Player player;
    private int floorContactCount;
    private int bodyFloorContactCount;
    private final String mapName;

    public WorldCollision(World world, Player player, String mapName) {
        this.player = player;
        this.mapName = mapName;
        this.floorContactCount = 0;
        this.bodyFloorContactCount = 0;
        setupCollisionListener(world);
    }

    private void setupCollisionListener(World world) {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                handleSideContact(fixtureA, fixtureB, true);

                if (isFixtureWithUserData(fixtureA, "player-foot-backup") || isFixtureWithUserData(fixtureB, "player-foot-backup")) {
                    floorContactCount++;
                    player.setOnFloor(true);
                }

                if (isFixtureWithUserData(fixtureA, "player-foot") || isFixtureWithUserData(fixtureB, "player-foot")) {
                    bodyFloorContactCount++;
                    player.setBodyOnFloor(true);
                }

                if (isFixtureWithUserData(fixtureA, "player-jump") || isFixtureWithUserData(fixtureB, "player-jump")) {
                    player.setOnJump(true);
                }
                processCollision(fixtureA, fixtureB);
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                handleSideContact(fixtureA, fixtureB, false);

                if (isFixtureWithUserData(fixtureA, "player-foot-backup") || isFixtureWithUserData(fixtureB, "player-foot-backup")) {
                    floorContactCount = Math.max(0, floorContactCount - 1);
                    if (floorContactCount == 0) {
                        player.setOnFloor(false);
                    }
                }

                if (isFixtureWithUserData(fixtureA, "player-foot") || isFixtureWithUserData(fixtureB, "player-foot")) {
                    bodyFloorContactCount = Math.max(0, bodyFloorContactCount - 1);
                    if (bodyFloorContactCount == 0) {
                        player.setBodyOnFloor(false);
                    }
                }

                if (isFixtureWithUserData(fixtureA, "player-jump") || isFixtureWithUserData(fixtureB, "player-jump")) {
                    player.setOnJump(false);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}

            private void handleSideContact(Fixture fixtureA, Fixture fixtureB, boolean isBegin) {
                if (isFixtureWithUserData(fixtureA, "player-left-side") || isFixtureWithUserData(fixtureB, "player-left-side")) {
                    player.setOnLeftSide(isBegin);
                }

                if (isFixtureWithUserData(fixtureA, "player-right-side") || isFixtureWithUserData(fixtureB, "player-right-side")) {
                    player.setOnRightSide(isBegin);
                }
            }

            private boolean isFixtureWithUserData(Fixture fixture, String userData) {
                return fixture != null && fixture.getUserData() != null && fixture.getUserData().equals(userData);
            }

            private void processCollision(Fixture fixtureA, Fixture fixtureB) {
                if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
                    String fixturePair = fixtureA.getBody().getUserData().toString() + "-" + fixtureB.getBody().getUserData().toString();

                    if (fixturePair.startsWith("bullet-player") || fixturePair.matches("player-bullet")) {
                        handleBulletPlayerCollision(fixtureA, fixtureB);
                    } else if (fixturePair.startsWith("bullet-enemy") || fixturePair.matches("enemy\\d*-bullet")) {
                        handleBulletEnemyCollision(fixtureA, fixtureB);
                    } else if (fixturePair.startsWith("player-hostage") || fixturePair.matches("hostage\\d*-player")) {
                        handlePlayerHostageCollision(fixtureA, fixtureB);
                    } else if (fixturePair.startsWith("player-Items") || fixturePair.matches("Items-player")) {
                        handlePlayerItemCollision(fixtureA, fixtureB);
                    } else if (fixturePair.startsWith("bullet-bullet") || fixturePair.matches("bullet-bullet")) {
                        handleBulletStaticCollision(fixtureA, fixtureB);
                    }
                } else if ((Objects.equals(fixtureA.getBody().getUserData(), "bullet") && Objects.equals(fixtureB.getBody().getType(), BodyDef.BodyType.StaticBody))
                    || (Objects.equals(fixtureB.getBody().getUserData(), "bullet") && Objects.equals(fixtureA.getBody().getType(), BodyDef.BodyType.StaticBody))) {
                    handleBulletStaticCollision(fixtureA, fixtureB);
                }
            }

            private void handleBulletPlayerCollision(Fixture fixtureA, Fixture fixtureB) {
                for (Bullet bullet : GameScreen.bullets) {
                    if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                        player.setLife(Math.max(player.getLife() - bullet.getDamage(), 0));
                        GameScreen.bulletsToRemove.add(bullet);
                    }
                }
            }

            private void handleBulletEnemyCollision(Fixture fixtureA, Fixture fixtureB) {
                for (Enemy enemy : Static.gameState.get(mapName).getEnemies()) {
                    for (Bullet bullet : GameScreen.bullets) {
                        if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                            if (enemy.getLife() - bullet.getDamage() > 0) {
                                enemy.setLife(enemy.getLife() - bullet.getDamage());
                            } else {
                                GameScreen.enemiesToRemove.add(enemy);
                                String enemyType = enemy.getType().replaceAll("\\d+$", "").toLowerCase();
                                Static.statistics.getEnemyTypesKilled().put(enemyType, Static.statistics.getEnemyTypesKilled().getOrDefault(enemyType, 0) + 1);
                                Static.statistics.getMoney().put("enemies", Static.statistics.getMoney().getOrDefault("enemies", 0.0f) + enemy.getAward());
                                Static.statistics.setScore((int) (Static.statistics.getScore() + enemy.getAward()));

                                if ((player.getLife() < (bullet.getDamage() / 10.0f) * ((bullet.getDamage() / 10.0f) / 10.0f) + (player.getMaxLife() / (bullet.getDamage() / 10.0f)) && bullet.getOwner().equals(player.getName()))) {
                                    Static.statistics.setCriticalHits(Static.statistics.getCriticalHits() + 1);
                                }

                                if ((fixtureA.getUserData() != null && fixtureA.getUserData().equals(enemy.getName().toLowerCase() + "-head")) ||
                                    (fixtureB.getUserData() != null && fixtureB.getUserData().equals(enemy.getName().toLowerCase() + "-head"))) {
                                    Static.statistics.setHeadshots(Static.statistics.getHeadshots() + 1);
                                }
                                enemy.setProcessed(true);
                            }
                            GameScreen.bulletsToRemove.add(bullet);
                        }
                    }
                }
            }

            private void handlePlayerHostageCollision(Fixture fixtureA, Fixture fixtureB) {
                for (Hostage hostage : Static.gameState.get(mapName).getHostages()) {
                    if ((hostage.getBody() == fixtureA.getBody() || hostage.getBody() == fixtureB.getBody()) && !hostage.isProcessed()) {
                        String hostageGroup = hostage.getGroup().replaceAll("\\d+$", "").toLowerCase();
                        Static.statistics.getItemsCollected().put(hostageGroup, Static.statistics.getItemsCollected().getOrDefault(hostageGroup, 0) + 1);
                        Static.statistics.getMoney().put("hostages", Static.statistics.getMoney().getOrDefault("hostages", 0.0f) + hostage.getAward());
                        Static.statistics.setScore((int) (Static.statistics.getScore() + hostage.getAward()));
                        GameScreen.hostagesToRemove.add(hostage);
                        hostage.setProcessed(true);
                    }
                }
            }

            private void handlePlayerItemCollision(Fixture fixtureA, Fixture fixtureB) {
                for (Item item : Static.gameState.get(mapName).getItems()) {
                    if ((item.getBody() == fixtureA.getBody() || item.getBody() == fixtureB.getBody()) && !item.isProcessed()) {
                        String itemName = item.getName().toLowerCase();
                        if (itemName.matches("ammunition")) {
                            int backupCapacity = player.getWeapon().getBackupMagazinesCapacity();
                            int magazineCapacity = player.getWeapon().getMagazineCapacity();
                            int neededBullets = magazineCapacity - backupCapacity;
                            if (neededBullets > 0) {
                                player.getWeapon().setBackupMagazinesCapacity(backupCapacity + neededBullets);
                            }
                        } else if (itemName.matches("heart")) {
                            player.setLife(player.getLife() + item.getLife());
                        }
                        Static.statistics.getItemsCollected().put(item.getName(), Static.statistics.getItemsCollected().getOrDefault(item.getName(), 0) + 1);
                        GameScreen.itemsToRemove.add(item);
                        item.setProcessed(true);
                    }
                }
            }

            private void handleBulletStaticCollision(Fixture fixtureA, Fixture fixtureB) {
                for (Bullet bullet : GameScreen.bullets) {
                    if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                        GameScreen.bulletsToRemove.add(bullet);
                    }
                }
            }
        });
    }
}
