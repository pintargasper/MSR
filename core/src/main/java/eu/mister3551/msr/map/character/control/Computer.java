package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Static;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.Collision;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;

import java.util.ArrayList;

public class Computer extends Device {

    public Computer(Player player, Options options) {
        super(player, options);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void inputs(float delta, Collision collision) {
        if (collision.getZipline() != null && getZipline() == null && collision.getZipline().isZiplineCollision()) {
            setZipline(collision.getZipline());
        }

        Zipline currentZipline = getZipline();
        if (currentZipline != null) {
            onZipline(currentZipline.getPoints());
        } else {
            walking(delta, collision);
        }
    }

    @Override
    public void walking(float delta, Collision collision) {
        player.setVelocityX(0);
        player.setVelocityY(0);

        if (Gdx.input.isKeyPressed(options.getKeyboardFootLeft())
            && !Gdx.input.isKeyPressed(options.getKeyboardFootRight())
            && !player.isOnLeftSide()) {

            player.setCurrentAnimation(collision.isWater() ? player.getCharacterAnimation().getSwimLeft()
                : player.isOnFloor() ? player.getCharacterAnimation().getWalkLeft()
                : player.getCharacterAnimation().getJumpLeft());
            player.setLastMove(Character.LastMove.LEFT);
            player.setVelocityX(-1);

        } else if (Gdx.input.isKeyPressed(options.getKeyboardFootRight())
            && !Gdx.input.isKeyPressed(options.getKeyboardFootLeft())
            && !player.isOnRightSide()) {

            player.setCurrentAnimation(collision.isWater() ? player.getCharacterAnimation().getSwimRight()
                : player.isOnFloor() ? player.getCharacterAnimation().getWalkRight()
                : player.getCharacterAnimation().getJumpRight());
            player.setLastMove(Character.LastMove.RIGHT);
            player.setVelocityX(1);

        } else if (player.isOnFloor()) {
            if (!collision.isWater()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getStandLeft()
                    : player.getCharacterAnimation().getStandRight());
            } else {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT)
                    ? player.getCharacterAnimation().getSwimLeft()
                    : player.getCharacterAnimation().getSwimRight());
            }
        }

        if (Gdx.input.isKeyPressed(options.getKeyboardLadderUp()) && collision.isLadder()) {
            player.setCurrentAnimation(!collision.isStopOnLadder() ? player.getCharacterAnimation().getClimb() : player.getCharacterAnimation().getStanding());
            player.setVelocityY(player.getSpeedOnLadder());
        } else if (Gdx.input.isKeyPressed(options.getKeyboardLadderDown()) && collision.isLadder()) {
            player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
            player.setVelocityY(-player.getSpeedOnLadder());
        } else if (!collision.isStopOnLadder() && collision.isLadder()) {
            player.setCurrentAnimation(player.getCharacterAnimation().getStandingOnLadder());
        }

        if (player.isOnFloor()) {
            player.setJumps(0);
            if (player.getVelocityX() == 0 && collision.isStopOnLadder()) {
                player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
            }
        }

        if (collision.isLadder() && player.getVelocityY() == 0 && !player.isOnFloor()) {
            player.setCurrentAnimation(!collision.isStopOnLadder() ? player.getCharacterAnimation().getStandingOnLadder() : player.getLastMove().equals(Character.LastMove.LEFT) ? player.getCharacterAnimation().getStandLeft() : player.getCharacterAnimation().getStandRight());
        }

        if (Gdx.input.isKeyJustPressed(options.getKeyboardFootJump()) && player.getJumps() < 1 && player.isOnFloor() && !collision.isLadder()) {
            jump();
            player.getBody().setGravityScale(1);
        }

        if (collision.isLadder()) {
            player.getBody().setGravityScale(0);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getVelocityY());
        } else {
            player.getBody().setGravityScale(1);
            player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), Math.min(player.getBody().getLinearVelocity().y, 25));
        }

        /*if (Gdx.input.isKeyPressed(options.getKeyboardFootShoot()) && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }*/

        /*if (Gdx.input.isKeyJustPressed(options.getKeyboardFootReload() | options.getKeyboardLadderReload()) && player.getWeapon().getActiveMagazineCapacity() != player.getWeapon().getMagazineCapacity() && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player);
        }*/

        if (collision.getDoor() != null && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Object map = collision.getDoor().getProperties().get("map");
            if (map != null) {
                Static.screenChanger.changeScreen("GameScreen", new Mission(map.toString()));
            }
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
        zipline = OnZipline.movement(player.getBody(), player, zipline, points, player.getSpeedOnZipline());
    }
}
