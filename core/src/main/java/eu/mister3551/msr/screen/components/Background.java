package eu.mister3551.msr.screen.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Background {

    public static Color black = new Color(toRgb(0, 0, 0));
    public static Color white = new Color(toRgb(255, 255, 255));
    public static Color lightBlue = new Color(toRgb(173, 216, 230));
    public static Color lightGray = new Color(toRgb(211, 211, 211));
    public static Color lightGreen = new Color(toRgb(44, 238, 144));

    public static Color strongRed = new Color(toRgb(206, 18, 18));

    public static TextureRegionDrawable setBackground(Color color) {
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    private static Color toRgb(int r, int g, int b) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1);
    }
}
