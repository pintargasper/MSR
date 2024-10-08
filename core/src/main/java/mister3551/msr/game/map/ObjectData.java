package mister3551.msr.game.map;

public class ObjectData {

    private final int height;
    private final int width;
    private final float positionX;
    private final float positionY;

    public ObjectData(int height, int width, float positionX, float positionY) {
        this.height = height;
        this.width = width;
        this.positionX = positionX;
        this.positionY = positionY;
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
