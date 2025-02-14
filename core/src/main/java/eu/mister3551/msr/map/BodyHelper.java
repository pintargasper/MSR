package eu.mister3551.msr.map;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.map.character.BodyUserData;

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

    public Body body(BodyUserData bodyUserData, float width, float height, float x, float y, boolean isStatic) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / Constants.PPM, y / Constants.PPM);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 4 / Constants.PPM, height / 2 / Constants.PPM);

        Body body = world.createBody(bodyDef);
        body.setUserData(bodyUserData);

        body.createFixture(fixtureDef(shape));
        shape.dispose();

        createSensors(body, width, height);
        return body;
    }

    public void destroyBody(Body body) {
        if (body.getFixtureList().size > 0) {
            body.destroyFixture(body.getFixtureList().first());
        }
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
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }

    private void createSensors(Body body, float width, float height) {
        BodyUserData userData = (BodyUserData) body.getUserData();

        if (userData == null || userData.getSensors() == null) {
            return;
        }

        for (String sensor : userData.getSensors()) {
            PolygonShape sensorShape = new PolygonShape();
            Vector2 position = new Vector2();
            float boxWidth = 0, boxHeight = 0;

            String name = userData.getName();

            if ((name + "-head").equals(sensor.trim())) {
                boxWidth = width / 3.3f / Constants.PPM;
                boxHeight = height / 8 / Constants.PPM;
                position.set(0, height / 6 / Constants.PPM);
            } else if ((name + "-jump").equals(sensor.trim())) {
                boxWidth = (width / 4) / Constants.PPM;
                boxHeight = 10f / Constants.PPM;
                position.set(0, -(height / 2 + 0.1f) / Constants.PPM);
            } else if ((name + "-right-side").equals(sensor.trim())) {
                boxWidth = 0.1f / Constants.PPM;
                boxHeight = height / 4 / Constants.PPM;
                position.set(width / 2 / Constants.PPM, 0);
            } else if ((name + "-left-side").equals(sensor.trim())) {
                boxWidth = 0.1f / Constants.PPM;
                boxHeight = height / 4 / Constants.PPM;
                position.set(-width / 2 / Constants.PPM, 0);
            } else if ((name + "-bottom").equals(sensor.trim())) {
                boxWidth = width / 4 / Constants.PPM;
                boxHeight = 0.1f / Constants.PPM;
                position.set(0, -(height / 2) / Constants.PPM);
            } else if ((name + "-bottom-left").equals(sensor.trim())) {
                boxWidth = 5f / Constants.PPM;
                boxHeight = 5f / Constants.PPM;
                position.set(-width / Constants.PPM, -(height / 1.5f) / Constants.PPM);
            } else if ((name + "-bottom-right").equals(sensor.trim())) {
                boxWidth = 5f / Constants.PPM;
                boxHeight = 5f / Constants.PPM;
                position.set(width / Constants.PPM, -(height / 1.5f) / Constants.PPM);
            }
            sensorShape.setAsBox(boxWidth, boxHeight, position, 0);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = sensorShape;
            fixtureDef.isSensor = true;

            Fixture sensorFixture = body.createFixture(fixtureDef);
            sensorFixture.setUserData(sensor);
            sensorShape.dispose();
        }
    }
}
