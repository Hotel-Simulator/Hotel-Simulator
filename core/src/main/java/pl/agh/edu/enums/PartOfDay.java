package pl.agh.edu.enums;

import java.util.Arrays;

public enum PartOfDay {
    MORNING(6,10),
    DAY(10,17),
    EVENING(17,22),
    NIGHT(20,6);

    public final int startHour;
    public final int endHour;

    PartOfDay(int startHour, int endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public static PartOfDay parseHour(int ofHour) {
        return Arrays.stream(PartOfDay.values())
                .filter(partOfDay -> {
                    if (partOfDay.startHour < partOfDay.endHour) {
                        return ofHour >= partOfDay.startHour && ofHour < partOfDay.endHour;
                    } else {
                        return ofHour >= partOfDay.startHour || ofHour < partOfDay.endHour;
                    }
                })
                .findFirst()
                .orElseThrow();
    }
    public int getDuration() {
        return Math.abs(startHour-endHour);
    }
}
