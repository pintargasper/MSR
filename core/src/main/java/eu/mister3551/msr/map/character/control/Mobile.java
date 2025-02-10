package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;
import lombok.Setter;

public class Mobile extends Device {

    private final Stage stage;
    private final Skin skin;
    private Image shootImage;
    private Image jumpImage;
    private Image doorImage;
    private final Drawable shootDrawable;
    private final Drawable reloadDrawable;
    private Touchpad touchPad;
    private boolean moveJumpPressed;
    private boolean moveShootPressed;
    private boolean moveDoorPressed;
    private final boolean lightTheme;

    public Mobile(Player player, Options options, boolean lightTheme) {
        super(player, options);
        this.stage = Constants.stage;
        this.skin = Constants.skin;
        this.lightTheme = lightTheme;
        this.shootDrawable = skin.getDrawable(lightTheme ? "shoot-light": "shoot-dark");
        this.reloadDrawable = skin.getDrawable(lightTheme ? "reload-light": "reload-dark");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setName("left");
        table.padLeft(40.0f);
        table.padBottom(40.0f);
        table.align(Align.bottomLeft);
        table.setFillParent(true);

        touchPad = new Touchpad(0.0f, skin.get(lightTheme ? "light" : "default", Touchpad.TouchpadStyle.class));
        table.add(touchPad).minSize(150.0f).maxSize(150.0f);
        stage.addActor(table);

        table = new Table();
        table.setName("right");
        table.padRight(20.0f);
        table.padBottom(70.0f);
        table.align(Align.bottomRight);
        table.setFillParent(true);

        doorImage = new Image(skin, lightTheme ? "door-light": "door-dark");
        doorImage.setName("door");
        table.add(doorImage).padRight(10.0f).maxSize(90.0f).colspan(2);
        addControlListener(doorImage);
        table.row();

        shootImage = new Image(skin, lightTheme ? "shoot-light": "shoot-dark");
        shootImage.setName("shoot");
        table.add(shootImage).padRight(10.0f).maxSize(90.0f);
        addControlListener(shootImage);

        jumpImage = new Image(skin, lightTheme ? "jump-light": "jump-dark");
        jumpImage.setName("jump");
        table.add(jumpImage).maxSize(90.0f);
        addControlListener(jumpImage);

        stage.addActor(table);
    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {
        spriteBatch.end();
        stage.act(delta);
        stage.draw();
        spriteBatch.begin();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void update(float delta) {
        Zipline zipline = player.getMovementCollision().getZipline();
        if (zipline != null && this.zipline == null && zipline.isZiplineCollision()) {
            this.zipline = zipline;
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            this.zipline = OnZipline.movement(player, currentZipline);
            return;
        }

        player.setVelocityX(0);
        player.setVelocityY(0);

        float touchpadX = touchPad.getKnobPercentX();
        float touchpadY = touchPad.getKnobPercentY();

        if (touchpadX < -0.5f
            && !player.isLeftSide()) {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getSwimLeft());
            } else {
                player.setCurrentAnimation(player.getCharacterAnimation().getWalkLeft());
            }
            player.setLastMove(Character.LastMove.LEFT);
            player.setVelocityX(-1);
        } else if (touchpadX > 0.5f
            && !player.isRightSide()) {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getSwimRight());
            } else {
                player.setCurrentAnimation(player.getCharacterAnimation().getWalkRight());
            }
            player.setLastMove(Character.LastMove.RIGHT);
            player.setVelocityX(1);
        } else {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getSwimLeft()
                    : player.getCharacterAnimation().getSwimRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getStandLeft()
                    : player.getCharacterAnimation().getStandRight());
            }
        }

        if (moveJumpPressed
            && !player.isJumping()
            && !player.getMovementCollision().isWater()) {
            jump();
        }

        if (player.isJumping()) {
            player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ?
                player.getCharacterAnimation().getJumpLeft() :
                player.getCharacterAnimation().getJumpRight());
        }

        if (moveShootPressed && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting()) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }

        if (shootImage.getDrawable().equals(shootDrawable) && player.getWeapon().getActiveMagazineCapacity() == 0 && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player, shootImage, reloadDrawable, shootDrawable);
        }

        if (player.getMovementCollision().isLadder() && (!(touchpadX < -0.8f)) && (!(touchpadX > 0.8f))) {
            setShooting(false);

            if (touchpadY > 0.8f) {
                if (player.getMovementCollision().isStopOnTopOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStanding());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                    player.setVelocityY(player.getSpeedOnLadder());
                }
            } else if (touchpadY < -0.8f) {
                if (player.getMovementCollision().isStopOnBottomOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                    player.setVelocityY(-player.getSpeedOnLadder());
                }
            } else {
                if (player.getMovementCollision().isStopOnTopOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStanding());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
                }
            }
            player.getBody().setGravityScale(0);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
        } else {
            player.getBody().setGravityScale(1);
            if (!isReloading()) {
                setShooting(true);
            }
        }

        if (player.getMovementCollision().getDoor() != null) {
            Object map = player.getMovementCollision().getDoor().getProperties().get("map");
            if (map != null) {
                doorImage.setVisible(true);
                if (moveDoorPressed) {
                    Mission mission = new Mission();
                    mission.setMap(map.toString());
                    Constants.screenChanger.changeScreen("GameScreen", mission);
                }
            }
        } else {
            doorImage.setVisible(false);
        }
        player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getBody().getLinearVelocity().y);
    }

    private void jump() {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 12), player.getBody().getPosition(), true);
    }

    private void addControlListener(final Image image) {
        image.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (image.getName()) {
                    case "jump": moveJumpPressed = true; break;
                    case "shoot": moveShootPressed = true; break;
                    case "door": moveDoorPressed = true; break;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (image.getName()) {
                    case "jump": moveJumpPressed = false; break;
                    case "shoot": moveShootPressed = false; break;
                    case "door": moveDoorPressed = false; break;
                }
            }
        });
    }

    private void setVisibility(boolean jump, boolean shoot) {
        shootImage.setVisible(shoot);
        jumpImage.setVisible(jump);
    }
}
