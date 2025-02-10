package eu.mister3551.msr.map.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;

@Getter
public class CharacterAnimation {

    private final Animation<TextureRegion> standing;
    private final Animation<TextureRegion> standingOnLadder;
    private final Animation<TextureRegion> standLeft;
    private final Animation<TextureRegion> standRight;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> climb;
    private final Animation<TextureRegion> ziplineLeft;
    private final Animation<TextureRegion> ziplineRight;
    private final Animation<TextureRegion> jumpLeft;
    private final Animation<TextureRegion> jumpRight;
    private final Animation<TextureRegion> swimLeft;
    private final Animation<TextureRegion> swimRight;

    public CharacterAnimation(TextureAtlas textureAtlas, String standing,
                              String standingOnLadder,
                              String standLeft,
                              String standRight,
                              String walkLeft1,
                              String walkLeft2,
                              String walkRight1,
                              String walkRight2,
                              String climb1,
                              String climb2,
                              String flyLeft,
                              String flyRight,
                              String jumpLeft,
                              String jumpRight,
                              String swimLeft1,
                              String swimLeft2,
                              String swimRight1,
                              String swimRight2
    ) {
        this.standing = new Animation<>(0f, textureAtlas.findRegion(standing));
        this.standingOnLadder = new Animation<>(0, textureAtlas.findRegion(standingOnLadder));
        this.standLeft = new Animation<>(0, textureAtlas.findRegion(standLeft));
        this.standRight = new Animation<>(0, textureAtlas.findRegion(standRight));
        this.walkLeft = new Animation<>(0.3f, textureAtlas.findRegion(walkLeft1), textureAtlas.findRegion(walkLeft2));
        this.walkRight = new Animation<>(0.3f, textureAtlas.findRegion(walkRight1), textureAtlas.findRegion(walkRight2));
        this.climb = new Animation<>(0.3f, textureAtlas.findRegion(climb1), textureAtlas.findRegion(climb2));
        this.ziplineLeft = new Animation<>(0, textureAtlas.findRegion(flyLeft));
        this.ziplineRight = new Animation<>(0, textureAtlas.findRegion(flyRight));
        this.jumpLeft = new Animation<>(0, textureAtlas.findRegion(jumpLeft));
        this.jumpRight = new Animation<>(0, textureAtlas.findRegion(jumpRight));
        this.swimLeft = new Animation<>(0.3f, textureAtlas.findRegion(swimLeft1), textureAtlas.findRegion(swimLeft2));
        this.swimRight = new Animation<>(0.3f, textureAtlas.findRegion(swimRight1), textureAtlas.findRegion(swimRight2));
    }

    public CharacterAnimation(TextureAtlas textureAtlas, String standing) {
        this.standing = new Animation<>(0f, textureAtlas.findRegion(standing));
        this.standingOnLadder = null;
        this.standLeft = null;
        this.standRight = null;
        this.walkLeft = null;
        this.walkRight = null;
        this.climb = null;
        this.ziplineLeft = null;
        this.ziplineRight = null;
        this.jumpLeft = null;
        this.jumpRight = null;
        this.swimLeft = null;
        this.swimRight = null;
    }
}
