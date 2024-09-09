package mister3551.msr.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mister3551.msr.game.Static;

public class Converter {

    public static Vector2 coordinates(Rectangle rectangle) {
        float x = (rectangle.getX() + (rectangle.getWidth() / 2)) / Static.PPM;
        float y = (rectangle.getY() + (rectangle.getHeight() / 2)) / Static.PPM;
        return new Vector2(x, y);
    }
}
