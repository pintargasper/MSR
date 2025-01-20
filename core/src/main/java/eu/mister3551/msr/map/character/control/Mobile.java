package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Gdx;
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
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.MovementCollision;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameState;

import java.util.ArrayList;

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

    public Mobile(Player player, Options options) {
        super(player, options);
        this.stage = Static.stage;
        this.skin = Static.skin;
        this.shootDrawable = skin.getDrawable("shoot");
        this.reloadDrawable = skin.getDrawable("reload");
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

        touchPad = new Touchpad(0.0f, skin);
        table.add(touchPad).minSize(150.0f).maxSize(150.0f);
        stage.addActor(table);

        table = new Table();
        table.setName("right");
        table.padRight(20.0f);
        table.padBottom(70.0f);
        table.align(Align.bottomRight);
        table.setFillParent(true);

        doorImage = new Image(skin, "door");
        doorImage.setName("door");
        table.add(doorImage).padRight(10.0f).maxSize(90.0f).colspan(2);
        addControlListener(doorImage);
        table.row();

        shootImage = new Image(skin, "shoot");
        shootImage.setName("shoot");
        table.add(shootImage).padRight(10.0f).maxSize(90.0f);
        addControlListener(shootImage);

        jumpImage = new Image(skin, "jump");
        jumpImage.setName("jump");
        table.add(jumpImage).maxSize(90.0f);
        addControlListener(jumpImage);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
    public void inputs(MovementCollision movementCollision, float delta) {
        if (movementCollision.getZipline() != null && getZipline() == null && movementCollision.getZipline().isZiplineCollision()) {
            setZipline(movementCollision.getZipline());
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            setVisibility(false, false);
            onZipline(currentZipline.getPoints());
        } else {
            walking(movementCollision, delta);
        }
    }

    @Override
    public void walking(MovementCollision movementCollision, float delta) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        float touchpadX = touchPad.getKnobPercentX();
        float touchpadY = touchPad.getKnobPercentY();

        if (touchpadX < -0.5f && !player.isOnLeftSide()) {
            player.setCurrentAnimation(movementCollision.isWater() ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() : player.getCharacterAnimation().getJumpLeft());
            player.setLastMove(Character.LastMove.LEFT);
            player.setVelocityX(-1);
        } else if (touchpadX > 0.5f && !player.isOnRightSide()) {
            player.setCurrentAnimation(movementCollision.isWater() ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() : player.getCharacterAnimation().getJumpRight());
            player.setLastMove(Character.LastMove.RIGHT);
            player.setVelocityX(1);
        } else if (player.isOnFloor()) {
            if (!movementCollision.isWater()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (touchpadY > 0.8f && movementCollision.isLadder()) {
            player.setCurrentAnimation(!movementCollision.isStopOnLadder() ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            player.setVelocityY(player.getSpeedOnLadder());
        } else if (touchpadY < -0.8f && movementCollision.isLadder()) {
            player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
            player.setVelocityY(-player.getSpeedOnLadder());
        } else if (!movementCollision.isStopOnLadder() && movementCollision.isLadder()) {
            player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
        }

        if (player.isOnJump()) {
            player.setJumps(0);

            if (player.getVelocityX() == 0 && movementCollision.isStopOnLadder()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }
        }

        if (movementCollision.isLadder() && player.getVelocityY() == 0 && !player.isOnFloor()) {
            player.setCurrentAnimation(!movementCollision.isStopOnLadder() ? player.getCharacterAnimation().getStandingOnLadder() : player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
        }

        if (moveJumpPressed && player.getJumps() < 1 && player.isOnJump() && player.isBodyOnFloor() && !movementCollision.isLadder()) {
            jump();
            player.getBody().setGravityScale(1);
        }

        if (movementCollision.isLadder()) {
            player.getBody().setGravityScale(0);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
            setVisibility(false, player.isBodyOnFloor() || movementCollision.isStopOnLadder());
        } else {
            player.getBody().setGravityScale(1);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(player.getBody().getLinearVelocity().y, 25));
            setVisibility(player.isOnFloor(),
                !movementCollision.isWater()
                    && (player.isOnFloor()
                    || (player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineRight()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimRight())
                )
            );
        }
        if (moveShootPressed && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }

        if (shootImage.getDrawable().equals(shootDrawable) && player.getWeapon().getActiveMagazineCapacity() == 0 && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player, shootImage, reloadDrawable, shootDrawable);
        }

        if (movementCollision.getDoor() != null && player.isOnFloor()) {
            Object map = movementCollision.getDoor().getProperties().get("map");
            if (map != null) {
                doorImage.setVisible(true);
                if (moveDoorPressed) {
                    GameState gameState = Static.gameState.values().stream()
                        .findFirst()
                        .orElse(null);

                    if (gameState != null) {
                        Mission mission = new Mission();
                        mission.setId(gameState.getMission().getId());
                        mission.setIdUser(gameState.getMission().getIdUser());
                        mission.setMap(map.toString());
                        Static.screenChanger.changeScreen("GameScreen", mission);
                    }
                }
            }
        } else {
            doorImage.setVisible(false);
        }
    }

    @Override
    public void jump() {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 12), player.getBody().getPosition(), true);
        player.setJumps(player.getJumps() + 1);
    }

    @Override
    public void onZipline(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(player, zipline, points);
    }

    private void addControlListener(final Image control) {
        control.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (control.getName()) {
                    case "jump": moveJumpPressed = true; break;
                    case "shoot": moveShootPressed = true; break;
                    case "door": moveDoorPressed = true; break;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (control.getName()) {
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
