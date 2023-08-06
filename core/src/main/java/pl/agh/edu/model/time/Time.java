package pl.agh.edu.model.time;

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
    private static int acceleration = 1;

    private static final int minAcceleration = 1;
    private static final int maxAcceleration = 8;
    private static boolean isRunning = true;

    private static final int timeUnitInMinutes = 10;

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

    public void update(float delta) {
        if (isRunning) {
            remaining -= delta * acceleration;

            if (remaining < 0.0F) {
                minutes+=timeUnitInMinutes;
                this.reset();

                if (minutes >= 60) {
                    hours++;
                    minutes = minutes%60;

                    if (hours >= 24) {
                        days++;
                        hours = hours%24;

                        int daysInMonth = switch (months) {
                            case 1 ->
                                    (years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) ? 29 : 28;
                            case 3, 5, 8, 10 ->
                                    30;
                            default -> 31;
                        };

                        if (days >= daysInMonth) {
                            months++;
                            days = days%daysInMonth;

                            if (months >= 12) {
                                years++;
                                months = months%12;
                            }
                        }
                    }
                }
            }
        }
    }
    public void increaseAcceleration() {
        acceleration = Math.min(acceleration * 2, maxAcceleration);
    }

    public void decreaseAcceleration() {
        acceleration = Math.max(acceleration / 2, minAcceleration);
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void toggle() {
        isRunning = !isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LocalDateTime getTime() {
        return LocalDateTime.of(years, months, days, hours, minutes);
    }

    public String getStringTime() {
        return String.format("%02d.%02d.%02d\n%02d:%02d",days,months,years, hours, minutes);
    }

    public String getStringAcceleration() {
        return String.format("x%d", acceleration);
    }

    public LocalDateTime generateRandomTime(long range, TemporalUnit unit) {
        long max = unit.getDuration().toMinutes() * range;
        long randomOffset = ThreadLocalRandom.current().nextLong(max)/10*10;
        return getTime().plus(randomOffset, ChronoUnit.MINUTES);
    }

    public int getTimeUnitInMinutes(){
        return timeUnitInMinutes;
    }

}