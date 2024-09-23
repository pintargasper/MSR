package mister3551.msr.game.characters;

import com.badlogic.gdx.physics.box2d.*;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Bullet;
import mister3551.msr.game.characters.object.Player;

import java.util.ArrayList;
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

                if ((Objects.equals(fixtureA.getBody().getUserData(), "bullet") && Objects.equals(fixtureB.getBody().getType(), BodyDef.BodyType.StaticBody))
                    || Objects.equals(fixtureB.getBody().getUserData(), "bullet") && Objects.equals(fixtureA.getBody().getType(), BodyDef.BodyType.StaticBody)) {

                    for (Bullet bullet : Static.getBullets()) {
                        if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                            Static.getBulletsToRemove().add(bullet);
                        }
                    }
                }

                if (fixtureA.getBody().getUserData() != null && fixtureB.getBody().getUserData() != null) {
                    String fixture = fixtureA.getBody().getUserData().toString() + "-" + fixtureB.getBody().getUserData().toString();

                    if (fixture.equals("bullet-Enemy") || fixture.equals("Enemy-bullet")) {
                        for (Bullet bullet : Static.getBullets()) {
                            if (bullet.getBody() == fixtureA.getBody() || bullet.getBody() == fixtureB.getBody()) {
                                Static.getBulletsToRemove().add(bullet);
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
