package mister3551.msr.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mister3551.msr.game.characters.object.Player;
import mister3551.msr.game.controls.zipline.OnZipline;
import mister3551.msr.game.controls.zipline.Zipline;

import java.util.ArrayList;

public class Computer extends Device {

    private final Viewport viewport;
    private final Stage stage;
    private Label reloadingLabel;

    public Computer(Body body, Player player) {
        super(body, player);
        this.viewport = new ExtendViewport(800, 480);
        this.stage = new Stage(viewport);
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);

        reloadingLabel = new Label("Reloading...", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        reloadingLabel.setVisible(false);

        table.right();
        table.add(reloadingLabel).pad(10);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void inputs(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision, Zipline zipLine) {
        if (zipLine != null && getZipline() == null && zipLine.isZiplineCollision()) {
            setZipline(zipLine);
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            onZipline(currentZipline.getPoints());
        } else {
            walking(delta, ladderCollision, stopOnLadder, watterCollision);
        }
    }

    @Override
    public void walking(float delta, boolean ladderCollision, boolean stopOnLadder, boolean watterCollision) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !player.isOnLeftSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimLeft() : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft() : player.getCharacterAnimation().getZiplineLeft());
            player.setLastMove("left");
            player.setVelocityX(-1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) && !player.isOnRightSide()) {
            player.setCurrentAnimation(watterCollision ? player.getCharacterAnimation().getSwimRight() : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight() :  player.getCharacterAnimation().getZiplineRight());
            player.setLastMove("right");
            player.setVelocityX(1);
        } else if (player.isOnFloor()) {
            if (!watterCollision) {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals("left") ? player.getCharacterAnimation().getSwimLeft() : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && ladderCollision) {
            player.setCurrentAnimation(!stopOnLadder ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            player.setVelocityY(player.getSpeedOnLadder());
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) && ladderCollision) {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && player.getJumps() < 1 && player.isOnFloor() && !ladderCollision) {
            jump();
            body.setGravityScale(1);
        }

        if (ladderCollision) {
            body.setGravityScale(0);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
        } else {
            body.setGravityScale(1);
            body.setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(body.getLinearVelocity().y, 25));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shots < player.getWeapon().getMagazineCapacity() && isShooting) {
            shots += player.getOnShoot().shoot(player, delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R) && shots != 0) {
            isShooting = false;
            reloadingLabel.setVisible(true);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    shots = 0;
                    isShooting = true;
                    reloadingLabel.setVisible(false);
                }
            }, 3);
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
}
