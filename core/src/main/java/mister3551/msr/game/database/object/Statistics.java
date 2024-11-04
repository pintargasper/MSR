package mister3551.msr.game.database.object;

public class Statistics {

    private int score;
    private float earnedMoney;
    private int hostageKilled;
    private int enemyKilled;
    private int ammoCosts;
    private String usedTime;
    private int totalEarnedMoney;

    public Statistics() {

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getEarnedMoney() {
        return earnedMoney;
    }

    public void setEarnedMoney(float earnedMoney) {
        this.earnedMoney = earnedMoney;
    }

    public int getHostageKilled() {
        return hostageKilled;
    }

    public void setHostageKilled(int hostageKilled) {
        this.hostageKilled = hostageKilled;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public void setEnemyKilled(int enemyKilled) {
        this.enemyKilled = enemyKilled;
    }

    public int getAmmoCosts() {
        return ammoCosts;
    }

    public void setAmmoCosts(int ammoCosts) {
        this.ammoCosts = ammoCosts;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public int getTotalEarnedMoney() {
        return totalEarnedMoney;
    }

    public void setTotalEarnedMoney(int totalEarnedMoney) {
        this.totalEarnedMoney = totalEarnedMoney;
    }
}
