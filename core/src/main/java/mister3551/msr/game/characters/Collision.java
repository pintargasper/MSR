package mister3551.msr.game.characters;

import com.badlogic.gdx.physics.box2d.*;

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

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("left-side")) {
                    player.setOnLeftSide(true);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("left-side")) {
                    player.setOnLeftSide(true);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("right-side")) {
                    player.setOnRightSide(true);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("right-side")) {
                    player.setOnRightSide(true);
                }

                if (fixtureA.getUserData() != null && (fixtureA.getUserData().equals("foot") || fixtureB.getBody().getUserData().equals("foot-backup"))) {
                    player.setOnFloor(true);
                } else if (fixtureB.getUserData() != null && (fixtureB.getUserData().equals("foot") || fixtureB.getBody().getUserData().equals("foot-backup"))) {
                    player.setOnFloor(true);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("left-side")) {
                    player.setOnLeftSide(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("left-side")) {
                    player.setOnLeftSide(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("right-side")) {
                    player.setOnRightSide(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("right-side")) {
                    player.setOnRightSide(false);
                }

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("foot-backup")) {
                    player.setOnFloor(false);
                } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("foot-backup")) {
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
