package eu.mister3551.msr.screen.gamescreen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import eu.mister3551.msr.Constants;

public class Visualisation {

    private Label enemiesLabel;
    private Label hostagesLabel;
    private Label playerLifeLabel;
    private Label timerLabel;
    private Label scoreLabel;

    public void setElements(Label enemiesLabel, Label hostagesLabel, Label playerLifeLabel, Label timerLabel, Label scoreLabel) {
        this.enemiesLabel = enemiesLabel;
        this.hostagesLabel = hostagesLabel;
        this.playerLifeLabel = playerLifeLabel;
        this.timerLabel = timerLabel;
        this.scoreLabel = scoreLabel;
    }

    public void visualize() {
        int totalEnemies = Constants.screenChanger.getGameState().getGameStates().values().stream()
            .mapToInt(gameState -> gameState.getEnemies().size())
            .sum();

        int totalHostages = Constants.screenChanger.getGameState().getGameStates().values().stream()
            .mapToInt(gameState -> gameState.getHostages().size())
            .sum();

        playerLifeLabel.setText(Constants.gameScreen.getPlayer().getLife());
        enemiesLabel.setText(totalEnemies);
        hostagesLabel.setText(totalHostages);
        timerLabel.setText(Constants.screenChanger.getTimer().toString());
        scoreLabel.setText("Score: " + Constants.statistics.getScore());
    }
}
