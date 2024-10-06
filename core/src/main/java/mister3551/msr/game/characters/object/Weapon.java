package mister3551.msr.game.characters.object;

public class Weapon {

    private final String name;
    private final int damage;
    private final int accuracy;
    private final int range;
    private final int fireRate;
    private final int magazineCapacity;
    private int activeMagazineCapacity;
    private int backupMagazinesCapacity;

    public Weapon(String name, int damage, int accuracy, int range, int fireRate, int magazineCapacity) {
        this.name = name;
        this.damage = damage;
        this.accuracy = accuracy;
        this.range = range;
        this.fireRate = fireRate;
        this.magazineCapacity = magazineCapacity;
        this.activeMagazineCapacity = magazineCapacity;
        this.backupMagazinesCapacity = magazineCapacity;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getRange() {
        return range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getMagazineCapacity() {
        return magazineCapacity;
    }

    public int getActiveMagazineCapacity() {
        return activeMagazineCapacity;
    }

    public void setActiveMagazineCapacity(int activeMagazineCapacity) {
        this.activeMagazineCapacity = activeMagazineCapacity;
    }

    public int getBackupMagazinesCapacity() {
        return backupMagazinesCapacity;
    }

    public void setBackupMagazinesCapacity(int backupMagazinesCapacity) {
        this.backupMagazinesCapacity = backupMagazinesCapacity;
    }
}
