package pl.agh.edu.time;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ThreadLocalRandom;

public class Time {
    private static Time instance;
    protected static float remaining;
    protected static float interval;
    protected static int minutes;
    protected static int hours;
    protected static int days;
    protected static int months;
    protected static int years;
    private static float acceleration = 1.0f;
    private static boolean isRunning = true;

    private Time() {
        Time.interval = 5;
        remaining = Time.interval;
        minutes = 0;
        hours = 0;
        days = 1;
        months = 1;
        years = 2020;
    }

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    public boolean hasTimeElapsed() {
        return (remaining < 0.0F);
    }

    public void reset() {
        remaining = interval;
    }

    public void reset(float interval) {
        Time.interval = interval;
        remaining = interval;
    }

    public void update(float delta) {
        if (isRunning) {
            remaining -= delta * acceleration;

            if (remaining < 0.0F) {
                minutes+=10;
                remaining = interval;

                if (minutes >= 60) {
                    hours++;
                    minutes = 0;

                    if (hours >= 24) {
                        days++;
                        hours = 0;

                        int daysInMonth = switch (months) {
                            case 1 ->
                                    (years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) ? 29 : 28;
                            case 3, 5, 8, 10 ->
                                    30;
                            default -> 31;
                        };

                        if (days >= daysInMonth) {
                            months++;
                            days = 0;

                            if (months >= 12) {
                                years++;
                                months = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setAcceleration(float acceleration) {
        Time.acceleration = acceleration;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LocalDateTime getTime() {
        return LocalDateTime.of(years, months, days, hours, minutes);
    }

    public LocalDateTime generateRandomTime(long range, TemporalUnit unit) {
        long max = unit.getDuration().toMinutes() * range;
        long randomOffset = ThreadLocalRandom.current().nextLong(max)/10*10;
        return getTime().plus(randomOffset, ChronoUnit.MINUTES);
    }
}