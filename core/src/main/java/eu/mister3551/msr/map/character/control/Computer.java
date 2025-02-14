package eu.mister3551.msr.map.character.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Mission;
import eu.mister3551.msr.database.object.Options;
import eu.mister3551.msr.map.character.Character;
import eu.mister3551.msr.map.character.Player;
import eu.mister3551.msr.map.character.movement.OnZipline;
import eu.mister3551.msr.map.character.movement.Zipline;
import eu.mister3551.msr.screen.GameScreen;

public class Computer extends Device {

    public Computer(Player player, Options options) {
        super(player, options);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(SpriteBatch spriteBatch, float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

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

        if (Gdx.input.isKeyPressed(options.getKeyboardFootLeft())
            && !Gdx.input.isKeyPressed(options.getKeyboardFootRight())
            && !player.isLeftSide()) {
            if (player.getMovementCollision().isWater()) {
                player.setCurrentAnimation(player.getCharacterAnimation().getSwimLeft());
            } else {
                player.setCurrentAnimation(player.getCharacterAnimation().getWalkLeft());
            }
            player.setLastMove(Character.LastMove.LEFT);
            player.setVelocityX(-1);
        } else if (Gdx.input.isKeyPressed(options.getKeyboardFootRight())
            && !Gdx.input.isKeyPressed(options.getKeyboardFootLeft())
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

        if (Gdx.input.isKeyJustPressed(options.getKeyboardFootJump())
            && !player.isJumping()
            && !player.getMovementCollision().isWater()) {
            jump();
        }

        if (player.isJumping()) {
            player.setCurrentAnimation(player.getLastMove().equals(Character.LastMove.LEFT) ?
                player.getCharacterAnimation().getJumpLeft() :
                player.getCharacterAnimation().getJumpRight());
        }

        if (Gdx.input.isKeyPressed(options.getKeyboardFootShoot()) && player.getWeapon().getActiveMagazineCapacity() != 0 && isShooting()) {
            player.getWeapon().setActiveMagazineCapacity(player.getWeapon().getActiveMagazineCapacity() - player.getOnShoot().shoot(player, delta));
        }

        if (Gdx.input.isKeyJustPressed(options.getKeyboardFootReload() | options.getKeyboardLadderReload()) && player.getWeapon().getActiveMagazineCapacity() != player.getWeapon().getMagazineCapacity() && player.getWeapon().getBackupMagazinesCapacity() != 0) {
            player.getOnShoot().reload(this, player);
        }

        if (player.getMovementCollision().isLadder() && (!Gdx.input.isKeyPressed(options.getKeyboardFootLeft()) &&
            !Gdx.input.isKeyPressed(options.getKeyboardFootRight()))) {
            setShooting(false);

            if (Gdx.input.isKeyPressed(options.getKeyboardLadderUp()) &&
                !Gdx.input.isKeyPressed(options.getKeyboardLadderDown())) {
                if (player.getMovementCollision().isStopOnTopOfLadder()) {
                    player.setCurrentAnimation(player.getCharacterAnimation().getStanding());
                } else {
                    player.setCurrentAnimation(player.getCharacterAnimation().getClimb());
                    player.setVelocityY(player.getSpeedOnLadder());
                }
            } else if (Gdx.input.isKeyPressed(options.getKeyboardLadderDown())
                && !Gdx.input.isKeyPressed(options.getKeyboardLadderUp())) {
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
                if (Gdx.input.isKeyJustPressed(options.getKeyboardFootDoor())) {
                    if (map.toString().matches("end-game")) {
                        Constants.gameScreen.gameStats = GameScreen.GameStats.COMPLETE;
                    } else {
                        Mission mission = Constants.gameScreen.getMission();
                        mission.setMap(map.toString());
                        Constants.screenChanger.changeScreen("GameScreen", mission);
                    }
                }
            }
        }
        player.getBody().setLinearVelocity(player.getVelocityX() * player.getSpeed(), player.getBody().getLinearVelocity().y);
    }

    private void jump() {
        player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
        player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 12), player.getBody().getPosition(), true);
    }
}
