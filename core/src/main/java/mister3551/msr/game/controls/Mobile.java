package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import mister3551.msr.game.Static;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.controls.movement.OnZipline;
import mister3551.msr.game.controls.movement.Zipline;

import java.util.ArrayList;

public class Mobile extends Device {

    private final Stage stage;
    private final Skin skin;
    private Image shootImage;
    private Image jumpImage;
    private final Drawable shootDrawable;
    private final Drawable reloadDrawable;
    private Touchpad touchPad;
    private boolean moveJumpPressed;
    private boolean moveShootPressed;

    public Mobile(Body body, Player player) {
        super(body, player);
        this.stage = Static.getStage();
        this.skin = Static.getSkin();
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

        shootImage = new Image(skin, "shoot");
        shootImage.setName("shoot");
        table.add(shootImage).padRight(10.0f).maxSize(90.0f);
        addControlListener(shootImage, shootImage.getName());

        jumpImage = new Image(skin, "jump");
        jumpImage.setName("jump");
        table.add(jumpImage).maxSize(90.0f);
        addControlListener(jumpImage, jumpImage.getName());

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
    public void inputs(float delta, boolean ladderCollision, boolean stopOnLadder, boolean waterCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            setVisibility(false, false);
            onZipline(currentZipline.getPoints());
        } else {
            walking(delta, ladderCollision, stopOnLadder, waterCollision);
        }
    }

    @Override
    public void walking(float delta, boolean ladderCollision, boolean stopOnLadder, boolean waterCollision) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        float touchpadX = touchPad.getKnobPercentX();
        float touchpadY = touchPad.getKnobPercentY();

        if (touchpadX < -0.5f && !player.isOnLeftSide()) {
            player.setCurrentAnimation(waterCollision ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() : player.getCharacterAnimation().getZiplineLeft());
            player.setLastMove("left");
            player.setVelocityX(-1);
        } else if (touchpadX > 0.5f && !player.isOnRightSide()) {
            player.setCurrentAnimation(waterCollision ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() : player.getCharacterAnimation().getZiplineRight());
            player.setLastMove("right");
            player.setVelocityX(1);
        } else if (player.isOnFloor()) {
            if (!waterCollision) {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (touchpadY > 0.8f && ladderCollision) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            player.setVelocityY(player.getSpeedOnLadder());
        } else if (touchpadY < -0.8f && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
            player.setVelocityY(-player.getSpeedOnLadder());
        } else if (!stopOnLadder && ladderCollision) {
            player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
        }

        if (player.isOnFloor()) {
            player.setJumps(0);

            if (player.getVelocityX() == 0 && stopOnLadder) {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }
        }

        if (ladderCollision && player.getVelocityY() == 0 && !player.isOnFloor()) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getStandingOnLadder() : player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
        }

        if (moveJumpPressed && player.getJumps() < 1 && player.isOnFloor() && !ladderCollision) {
            jump();
            body.setGravityScale(1);
        }

        if (ladderCollision) {
            body.setGravityScale(0);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
            setVisibility(false, player.isBodyOnFloor() || stopOnLadder);
        } else {
            body.setGravityScale(1);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(body.getLinearVelocity().y, 25));
            setVisibility(player.isOnFloor(),
                !waterCollision
                    && (player.isOnFloor()
                    || (player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getZiplineRight()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimLeft()
                    && player.getCurrentAnimation() != player.getCharacterAnimation().getSwimRight())
                )
            );
        }

        System.out.println(isShooting);
        if (moveShootPressed && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }

        if (shootImage.getDrawable().equals(shootDrawable) && player.getWeapon().getActiveMagazineCapacity() == 0 && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player, shootImage, reloadDrawable, shootDrawable);
        }
    }

    @Override
    public void jump() {
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.applyLinearImpulse(new Vector2(0, body.getMass() * 12), body.getPosition(), true);
        player.setJumps(player.getJumps() + 1);
    }

    @Override
    public void onZipline(ArrayList<Vector2> points) {
        zipline = OnZipline.movement(body, player, zipline, points, player.getSpeedOnZipline());
    }

    private void addControlListener(Image control, final String type) {
        control.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (type) {
                    case "jump": moveJumpPressed = true; break;
                    case "shoot": moveShootPressed = true; break;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (type) {
                    case "jump": moveJumpPressed = false; break;
                    case "shoot": moveShootPressed = false; break;
                }
            }
        });
    }

    private void setVisibility(boolean jump, boolean shoot) {
        shootImage.setVisible(shoot);
        jumpImage.setVisible(jump);
    }
}
