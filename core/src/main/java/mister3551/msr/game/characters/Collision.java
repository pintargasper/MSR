package mister3551.msr.game.characters;

import com.badlogic.gdx.physics.box2d.*;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.*;

import java.util.Objects;

public class Collision {

    private final World world;
    private final Player player;

    public Collision(World world, Player player) {
        this.world = world;
        this.player = player;
        collide();
    }

    public void collide() {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-left-side")) {
                    player.setOnLeftSide(true);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-left-side")) {
                    player.setOnLeftSide(true);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-right-side")) {
                    player.setOnRightSide(true);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-right-side")) {
                    player.setOnRightSide(true);
                }

                if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals("player-foot") || fixtureB.getBody().getUserData().equals("player-foot-backup"))) {
                    player.setOnFloor(true);
                } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals("player-foot") || fixtureB.getBody().getUserData().equals("player-foot-backup"))) {
                    player.setOnFloor(true);
                }

                if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals("player-foot"))) {
                    player.setBodyOnFloor(true);
                } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals("player-foot"))) {
                    player.setBodyOnFloor(true);
                }

                for (Enemy enemy : Static.getEnemies()) {
                    if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals(enemy.getName().toLowerCase() + "-foot"))) {
                        enemy.setBodyOnFloor(true);
                    } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals(enemy.getName().toLowerCase() + "-foot"))) {
                        enemy.setBodyOnFloor(true);
                    }

                    if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals(enemy.getName().toLowerCase() + "-foot-backup"))) {
                        enemy.setOnFloor(true);
                    } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals(enemy.getName().toLowerCase() + "-foot-backup"))) {
                        enemy.setOnFloor(true);
                    }
                }

                if ((Objects.equals(fixtureA.getBody().getUserData(), "bullet") && Objects.equals(fixtureB.getBody().getType(), BodyDef.BodyType.StaticBody))
                    || Objects.equals(fixtureB.getBody().getUserData(), "bullet") && Objects.equals(fixtureA.getBody().getType(), BodyDef.BodyType.StaticBody)) {

                    for (Bullet bullet : Static.getBullets()) {
                        if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                            Static.getBulletsToRemove().add(bullet);
                            //TODO fix just for player
                            Static.getStatistics().setMissedShots(Static.getStatistics().getMissedShots() + 1);
                        }
                    }
                }

                if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
                    String fixture = fixtureA.getBody().getUserData().toString() + "-" + fixtureB.getBody().getUserData().toString();
                    if (fixture.startsWith("bullet-Enemy") || fixture.matches("Enemy\\d*-bullet")) {
                        for (Bullet bullet : Static.getBullets()) {
                            if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                                for (Enemy enemy : Static.getEnemies()) {
                                    if (enemy.getBody() == fixtureA.getBody() || enemy.getBody() == fixtureB.getBody()) {
                                        if (enemy.getLive() - bullet.getDamage() > 0) {
                                            enemy.setLive(enemy.getLive() - bullet.getDamage());
                                        } else {
                                            Static.getEnemiesToRemove().add(enemy);
                                            Static.getStatistics().setScore(Static.getStatistics().getScore() + bullet.getDamage());

                                            String enemyType = enemy.getType().replaceAll("\\d+$", "").toLowerCase();
                                            Static.getStatistics().getEnemyTypesKilled().put(enemyType, Static.getStatistics().getEnemyTypesKilled().getOrDefault(enemyType, 0) + 1);
                                        }
                                    }
                                }
                                Static.getBulletsToRemove().add(bullet);
                            }
                        }
                    }

                    if (fixture.startsWith("bullet-Hostage") || fixture.matches("Hostage\\d*-bullet")) {
                        for (Bullet bullet : Static.getBullets()) {
                            if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                                for (Hostage hostage : Static.getHostages()) {
                                    if (hostage.getBody() == fixtureA.getBody() || hostage.getBody() == fixtureB.getBody()) {
                                        if (hostage.getLive() - bullet.getDamage() > 0) {
                                            hostage.setLive(hostage.getLive() - bullet.getDamage());
                                        } else {
                                            Static.getHostagesToRemove().add(hostage);
                                            //Static.getStatistics().setScore(Static.getStatistics().getScore() + bullet.getDamage());

                                            String hostageType = hostage.getType().replaceAll("\\d+$", "").toLowerCase();
                                            Static.getStatistics().getHostageTypesKilled().put(hostageType, Static.getStatistics().getEnemyTypesKilled().getOrDefault(hostageType, 0) + 1);
                                        }
                                    }
                                }
                                Static.getBulletsToRemove().add(bullet);
                            }
                        }
                    }

                    if (fixture.startsWith("bullet-Player") || fixture.matches("Player-bullet")) {
                        for (Bullet bullet : Static.getBullets()) {
                            if (player.getBody() == fixtureA.getBody() || player.getBody() == fixtureB.getBody()) {
                                player.setLive(Math.max(player.getLive() - bullet.getDamage(), 0));
                                Static.getBulletsToRemove().add(bullet);
                            }
                        }
                    }

                    if (fixture.matches("bullet-bullet")) {
                        for (Bullet bullet : Static.getBullets()) {
                            if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                                Static.getBulletsToRemove().add(bullet);
                            }
                        }
                    }

                    if (fixture.startsWith("Player-Hostage") || fixture.matches("Hostage\\d*-Player")) {
                        for (Hostage hostage : Static.getHostages()) {
                            if (hostage.getBody() == fixtureA.getBody() || hostage.getBody() == fixtureB.getBody()) {
                                String hostageGroup = hostage.getGroup().replaceAll("\\d+$", "").toLowerCase();
                                Static.getStatistics().getItemsCollected().put(hostageGroup, Static.getStatistics().getItemsCollected().getOrDefault(hostageGroup, 0) + 1);
                                Static.getHostagesToRemove().add(hostage);
                            }
                        }
                    }

                    if (fixture.startsWith("Player-Item") || fixture.matches("Item-Player")) {
                        for (Item item : Static.getItems()) {
                            if (item.getBody() == fixtureA.getBody() || item.getBody() == fixtureB.getBody()) {
                                Static.getStatistics().getItemsCollected().put(item.getName(), Static.getStatistics().getItemsCollected().getOrDefault(item.getName(), 0) + 1);
                                Static.getItemsToRemove().add(item);
                            }
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-left-side")) {
                    player.setOnLeftSide(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-left-side")) {
                    player.setOnLeftSide(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-right-side")) {
                    player.setOnRightSide(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-right-side")) {
                    player.setOnRightSide(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-foot")) {
                    player.setBodyOnFloor(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-foot")) {
                    player.setBodyOnFloor(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-foot-backup")) {
                    player.setOnFloor(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-foot-backup")) {
                    player.setOnFloor(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("player-foot-backup")) {
                    player.setOnFloor(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("player-foot-backup")) {
                    player.setOnFloor(false);
                }

                for (Enemy enemy : Static.getEnemies()) {
                    if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals(enemy.getName().toLowerCase() + "-foot"))) {
                        enemy.setBodyOnFloor(false);
                    } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals(enemy.getName().toLowerCase() + "-foot"))) {
                        enemy.setBodyOnFloor(false);
                    }

                    if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals(enemy.getName().toLowerCase() + "-foot-backup"))) {
                        enemy.setOnFloor(false);
                    } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals(enemy.getName().toLowerCase() + "-foot-backup"))) {
                        enemy.setOnFloor(false);
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
}
