package mister3551.msr.game.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ObjectData {

    private final TextureRegion textureRegion;
    private final int height;
    private final int width;
    private final float positionX;
    private final float positionY;

    public ObjectData(TextureRegion textureRegion, int height, int width, float positionX, float positionY) {
        this.textureRegion = textureRegion;
        this.height = height;
        this.width = width;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }
}
