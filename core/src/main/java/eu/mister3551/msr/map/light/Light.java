package eu.mister3551.msr.map.light;

import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;

@Getter
public class Light {

    private final String type;
    private final Rectangle bounds;
    private final int distance;
    private final float softness;

    public Light(String type, Rectangle bounds, int distance, float softness) {
        this.type = type;
        this.bounds = bounds;
        this.distance = distance;
        this.softness = softness;
    }
}
