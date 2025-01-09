package eu.mister3551.msr.map.character.collision;

import com.badlogic.gdx.physics.box2d.*;
import eu.mister3551.msr.map.character.Player;

import java.util.HashSet;
import java.util.Set;

public class WorldCollision {

    private final Player player;
    private final Set<Fixture> floorContacts = new HashSet<>();
    private final Set<Fixture> bodyFloorContacts = new HashSet<>();

    public WorldCollision(World world, Player player) {
        this.player = player;
        collision(world);
    }

    private void collision(World world) {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                handleSideContact(fixtureA, fixtureB, true);

                if (isFixtureWithUserData(fixtureA, "player-foot-backup") || isFixtureWithUserData(fixtureB, "player-foot-backup")) {
                    floorContacts.add(contact.getFixtureA());
                    floorContacts.add(contact.getFixtureB());
                    player.setOnFloor(true);
                }

                if (isFixtureWithUserData(fixtureA, "player-foot") || isFixtureWithUserData(fixtureB, "player-foot")) {
                    bodyFloorContacts.add(contact.getFixtureA());
                    bodyFloorContacts.add(contact.getFixtureB());
                    player.setBodyOnFloor(true);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                handleSideContact(fixtureA, fixtureB, false);

                if (isFixtureWithUserData(fixtureA, "player-foot-backup") || isFixtureWithUserData(fixtureB, "player-foot-backup")) {
                    floorContacts.remove(contact.getFixtureA());
                    floorContacts.remove(contact.getFixtureB());
                    if (floorContacts.isEmpty()) {
                        player.setOnFloor(false);
                    }
                }

                if (isFixtureWithUserData(fixtureA, "player-foot") || isFixtureWithUserData(fixtureB, "player-foot")) {
                    bodyFloorContacts.remove(contact.getFixtureA());
                    bodyFloorContacts.remove(contact.getFixtureB());
                    if (bodyFloorContacts.isEmpty()) {
                        player.setBodyOnFloor(false);
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            private void handleSideContact(Fixture fixtureA, Fixture fixtureB, boolean isBegin) {
                if (isFixtureWithUserData(fixtureA, "player-left-side") || isFixtureWithUserData(fixtureB, "player-left-side")) {
                    player.setOnLeftSide(isBegin);
                }

                if (isFixtureWithUserData(fixtureA, "player-right-side") || isFixtureWithUserData(fixtureB, "player-right-side")) {
                    player.setOnRightSide(isBegin);
                }
            }

            private boolean isFixtureWithUserData(Fixture fixture, String userData) {
                return fixture.getUserData() != null && fixture.getUserData().equals(userData);
            }
        });
    }
}
