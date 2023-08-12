package pl.agh.edu.enums;

import pl.agh.edu.model.time.Time;

import java.time.LocalDateTime;

public enum Frequency{
    EVERY_SHIFT,
    EVERY_UPDATE,
    EVERY_DAY,
    EVERY_MONTH,
    EVERY_YEAR;

    public LocalDateTime add(LocalDateTime localDateTime){
        return switch (this){
            case EVERY_SHIFT -> localDateTime.plusHours(8);
            case EVERY_UPDATE -> localDateTime.plusMinutes(Time.timeUnitInMinutes);
            case EVERY_DAY -> localDateTime.plusDays(1);
            case EVERY_MONTH -> localDateTime.plusMonths(1);
            case EVERY_YEAR -> localDateTime.plusYears(1);
        };
    }
}
