package eu.mister3551.msr.map.character;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyUserData {

    private final String name;
    private String[] sensors;

    public BodyUserData(String name, String... sensors) {
        this.name = name;
        this.sensors = sensors;
    }
}
