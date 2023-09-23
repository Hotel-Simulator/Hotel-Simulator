package pl.agh.edu.model.time;

import java.time.LocalDateTime;

import pl.agh.edu.enums.PartOfDay;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Time {
	private static Time instance;
	public static final int timeUnitInMinutes = 10;
	public static final float interval = 5;
	private int minutes = 0;
	private int hours = 0;
	private int days = 1;
	private int months = 1;
	private int years = 2020;
	private int acceleration = 1;
	private boolean isRunning = false;
	private float remaining = interval;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	private Time() {}

	public static Time getInstance() {
		if (instance == null) {
			instance = new Time();
		}
		return instance;
	}

	public void reset() {
		remaining = interval;
	}

	public void update(float delta) {
		if (isRunning) {
			remaining -= delta * acceleration;

			if (remaining < 0.0F) {
				minutes += timeUnitInMinutes;
				this.reset();

				if (minutes >= 60) {
					hours++;
					minutes = minutes % 60;

					if (hours >= 24) {
						days++;
						hours = hours % 24;

						int daysInMonth = switch (months) {
							case 1 ->
								(years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) ? 29 : 28;
							case 3, 5, 8, 10 ->
								30;
							default -> 31;
						};

						if (days >= daysInMonth) {
							months++;
							days = days % daysInMonth;

							if (months >= 12) {
								years++;
								months = months % 12;
							}
						}
					}
				}
				timeCommandExecutor.executeCommands(getTime());
			}
		}
	}

	public void increaseAcceleration() {
		int maxAcceleration = 8;
		acceleration = Math.min(acceleration * 2, maxAcceleration);
	}

	public void decreaseAcceleration() {
		int minAcceleration = 1;
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

	public float getRemaining() {
		return remaining;
	}

	public float getInterval() {
		return interval;
	}

	public LocalDateTime getTime() {
		return LocalDateTime.of(years, months, days, hours, minutes);
	}

	public String getStringTime() {
		return String.format("%02d.%02d.%02d\n%02d:%02d", days, months, years, hours, minutes);
	}

	public String getStringAcceleration() {
		return String.format("x%d", acceleration);
	}

	public int getTimeUnitInMinutes() {
		return timeUnitInMinutes;
	}

	public LocalDateTime getNextPartOfDayTime() {
		PartOfDay partOfDay = getPartOfDay();
		LocalDateTime currentTime = getTime();
		int hoursUntilNextPart = (partOfDay.startHour + partOfDay.getDuration() - currentTime.getHour() + 24) % 24;
		return currentTime.plusHours(hoursUntilNextPart);
	}

	public PartOfDay getPartOfDay() {
		return PartOfDay.parseHour(hours);
	}
}
