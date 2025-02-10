package eu.mister3551.msr.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;

public class Converter {

    public static Vector2 coordinates(Rectangle rectangle) {
        float x = (rectangle.getX() + (rectangle.getWidth() / 2)) / Constants.PPM;
        float y = (rectangle.getY() + (rectangle.getHeight() / 2)) / Constants.PPM;
        return new Vector2(x, y);
    }
}
