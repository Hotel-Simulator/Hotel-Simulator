package pl.agh.edu.engine.time;

import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Time {
	public static final int timeUnitInMinutes = 60;
	public static final float interval = 1;
	private static Time instance;
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final List<Runnable> timeStopChangeHandlers = new ArrayList<>();
	private final List<Runnable> timeStartChangeHandlers = new ArrayList<>();

	public final LocalDateTime startingTime = LocalDateTime.of(2020, 1, 1, 0, 0);
	private int minutes = startingTime.getMinute();
	private int hours = startingTime.getHour();
	private int days = startingTime.getDayOfMonth();
	private int months = startingTime.getMonthValue();
	private int years = startingTime.getYear();
	private int acceleration = 1;
	private boolean isRunning = false;
	private float remaining = interval;

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
							case 2 -> (years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) ? 29 : 28;
							case 4, 6, 9, 11 -> 30;
							default -> 31;
						};

						if (days > daysInMonth) {
							months++;
							days = days % daysInMonth;

							if (months > 12) {
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
		int maxAcceleration = 1024;
		acceleration = Math.min(acceleration * 2, maxAcceleration);
	}

	public void decreaseAcceleration() {
		int minAcceleration = 1;
		acceleration = Math.max(acceleration / 2, minAcceleration);
	}

	public int getAcceleration() {
		return acceleration;
	}

	public void start() {
		isRunning = true;
		timeStartChangeHandlers.forEach(Runnable::run);
	}

	public void stop() {
		isRunning = false;
		timeStopChangeHandlers.forEach(Runnable::run);
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

	public LocalDateTime getTime() {
		return LocalDateTime.of(years, months, days, hours, minutes);
	}

	public String getStringTime() {
		return String.format("%02d.%02d.%02d\n%02d:%02d", days, months, years, hours, minutes);
	}

	public String getStringAcceleration() {
		return String.format("x%d", acceleration);
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

	public MonthDay getMonthDay() {
		return MonthDay.of(months, days);
	}

	public YearMonth getYearMonth() {
		return YearMonth.of(years, months);
	}

	public void addTimeStopChangeHandler(Runnable handler) {
		timeStopChangeHandlers.add(handler);
	}

	public void addTimeStartChangeHandler(Runnable handler) {
		timeStartChangeHandlers.add(handler);
	}
}
