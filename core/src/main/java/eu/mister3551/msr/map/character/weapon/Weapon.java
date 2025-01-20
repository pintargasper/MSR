package eu.mister3551.msr.map.character.weapon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weapon {

    private final String name;
    private final int damage;
    private final int accuracy;
    private final int range;
    private final int fireRate;
    private final int ammunitionCosts;
    private final int magazineCapacity;
    private int activeMagazineCapacity;
    private int backupMagazinesCapacity;

    public Weapon(String name, int damage, int accuracy, int range, int fireRate, int magazineCapacity) {
        this.name = name;
        this.damage = damage;
        this.accuracy = accuracy;
        this.range = range;
        this.fireRate = fireRate;
        this.ammunitionCosts = 3;
        this.magazineCapacity = magazineCapacity;
        this.activeMagazineCapacity = magazineCapacity;
        this.backupMagazinesCapacity = magazineCapacity;
    }
}
