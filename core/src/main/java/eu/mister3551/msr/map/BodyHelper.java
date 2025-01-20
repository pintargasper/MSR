package eu.mister3551.msr.map;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.map.character.Character;

public class BodyHelper {

    private World world;

    public BodyHelper setWorld(World world) {
        this.world = world;
        return this;
    }

    public void staticBody(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        Shape shape = polygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    public Body body(String name, float width, float height, float x, float y, boolean isStatic) {

        name = name.toLowerCase();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / Static.PPM, y / Static.PPM);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 4 / Static.PPM, height / 2 / Static.PPM);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef(shape));
        shape.dispose();

        sensors(body, width, height, name);
        return body;
    }

    public Body copyBody(Body originalBody, Character character) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = originalBody.getType();
        bodyDef.position.set(originalBody.getPosition());
        bodyDef.angle = originalBody.getAngle();
        bodyDef.linearVelocity.set(originalBody.getLinearVelocity());
        bodyDef.angularVelocity = originalBody.getAngularVelocity();
        bodyDef.linearDamping = originalBody.getLinearDamping();
        bodyDef.angularDamping = originalBody.getAngularDamping();
        bodyDef.gravityScale = originalBody.getGravityScale();
        bodyDef.fixedRotation = originalBody.isFixedRotation();
        bodyDef.bullet = originalBody.isBullet();
        bodyDef.active = originalBody.isActive();
        bodyDef.allowSleep = originalBody.isSleepingAllowed();

        Body newBody = world.createBody(bodyDef);

        for (Fixture fixture : originalBody.getFixtureList()) {
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = fixture.getShape();
            fixtureDef.density = fixture.getDensity();
            fixtureDef.friction = fixture.getFriction();
            fixtureDef.restitution = fixture.getRestitution();
            fixtureDef.isSensor = fixture.isSensor();
            newBody.createFixture(fixtureDef);
        }

        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-left-side", character.getHeight() / 2);
        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-right-side", character.getHeight() / 2);
        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-foot", character.getHeight() / 2);
        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-foot-backup", (character.getHeight() + 50) / 2);
        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-jump", (character.getHeight() + 50) / 2);
        createSensor(newBody, character.getWidth(), character.getHeight(), character.getName() + "-head", (character.getHeight() + 50) / 2);

        newBody.setUserData(originalBody.getUserData());
        return newBody;
    }

    public void destroyBody(Body body) {
        if (body.getFixtureList().size > 0) {
            body.destroyFixture(body.getFixtureList().first());
        }
    }

    private void sensors(Body body, float width, float height, String name) {
        if (!name.equals("bullet")) {
            createSensor(body, width, height, name + "-left-side", height / 2);
            createSensor(body, width, height, name + "-right-side", height / 2);
            createSensor(body, width, height, name + "-foot", height / 2);
            createSensor(body, width, height, name + "-foot-backup", (height + 50) / 2);
            createSensor(body, width, height, name + "-jump", (height + 50) / 2);
            createSensor(body, width, height, name + "-head", (height + 50) / 2);
        }
    }

    private void createSensor(Body body, float width, float height, String userData, float offsetY) {
        PolygonShape sensorShape = new PolygonShape();

        String name = userData.split("-")[0];

        if (userData.equals(name + "-foot")) {
            sensorShape.setAsBox(width / 4 / Static.PPM, 0.1f / Static.PPM, new Vector2(0, -offsetY / Static.PPM), 0);
        } else if (userData.equals(name + "-foot-backup")) {
            sensorShape.setAsBox(width / 4 / Static.PPM, 0.1f / Static.PPM, new Vector2(0, -offsetY / Static.PPM), 0);
        } else if (userData.equals(name + "-jump")) {
            sensorShape.setAsBox(width / 4 / Static.PPM, 0.1f / Static.PPM, new Vector2(0, -offsetY / Static.PPM), 0);
        } else if (userData.equals(name + "-right-side")) {
            sensorShape.setAsBox(0.1f / Static.PPM, height / 4 / Static.PPM, new Vector2(width / 2 / Static.PPM, 0), 0);
        } else if (userData.equals(name + "-left-side")) {
            sensorShape.setAsBox(0.1f / Static.PPM, height / 4 / Static.PPM, new Vector2(-width / 2 / Static.PPM, 0), 0);
        } else if (userData.equals(name + "-head")) {
            sensorShape.setAsBox((width / 3.5f / Static.PPM), height / 8 / Static.PPM, new Vector2(0, height / 6 / Static.PPM), 0);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = sensorShape;
        fixtureDef.isSensor = true;
        Fixture sensor = body.createFixture(fixtureDef);
        sensor.setUserData(userData);
        sensorShape.dispose();
    }

    private FixtureDef fixtureDef(PolygonShape shape) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.density = 1;
        fixtureDef.shape = shape;
        return fixtureDef;
    }

    private Shape polygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / Static.PPM, vertices[i * 2 + 1] / Static.PPM);
            worldVertices[i] = current;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
