package mister3551.msr.game.map;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import mister3551.msr.game.Static;


public class BodyHelper {

    private final World world;

    public BodyHelper(World world) {
        this.world = world;
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

        if (!name.equals("bullet")) {
            createSensor(body, width, height, name + "-left-side", height / 2);
            createSensor(body, width, height, name + "-right-side", height / 2);
            createSensor(body, width, height, name + "-foot", height / 2);
            createSensor(body, width, height, name + "-foot-backup", (height + 50) / 2);
        }
        return body;
    }

    public void destroyBody(Body body) {
        if (body.getFixtureList().size > 0) {
            body.destroyFixture(body.getFixtureList().first());
        }
    }

    private void createSensor(Body body, float width, float height, String userData, float offsetY) {
        PolygonShape sensorShape = new PolygonShape();

        String name = userData.split("-")[0];

        if (userData.equals(name + "-foot")) {
            sensorShape.setAsBox(width / 4 / Static.PPM, 0.1f / Static.PPM, new Vector2(0, -offsetY / Static.PPM), 0);
        } else if (userData.equals(name + "-foot-backup")) {
            sensorShape.setAsBox(width / 4 / Static.PPM, 0.1f / Static.PPM, new Vector2(0, -offsetY / Static.PPM), 0);
        } else if (userData.equals(name + "-right-side")) {
            sensorShape.setAsBox(0.1f / Static.PPM, height / 4 / Static.PPM, new Vector2(width / 2 / Static.PPM, 0), 0);
        } else if (userData.equals(name + "-left-side")) {
            sensorShape.setAsBox(0.1f / Static.PPM, height / 4 / Static.PPM, new Vector2(-width / 2 / Static.PPM, 0), 0);
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
