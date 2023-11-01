package pl.agh.edu.ui.component.calendar;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.calendar.Calendar;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.selection.MonthSelection;
import pl.agh.edu.ui.component.selection.YearSelection;
import pl.agh.edu.ui.component.tooltip.DescriptionTooltip;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class CalendarComponent extends WrapperTable {
	private final Skin skin = GameSkin.getInstance();
	private final Time time = Time.getInstance();
	private final Container<CalendarMatrix> calendarMatrixContainer = new Container<>();
	private final LocalDate chosenDate;
	private final Consumer<LocalDate> dateChangeHandler;
	private final Map<LocalDate, CalendarEvent> eventsMap = new HashMap<>();
	private YearMonth currentYearMonth = time.getYearMonth();
	private final MonthSelection monthSelection;
	private final YearSelection yearSelection;

	public CalendarComponent(LocalDate chosenDate, Consumer<LocalDate> dateChangeHandler, Boolean isBlockedByTime) {
		super();
		this.chosenDate = chosenDate;
		this.dateChangeHandler = dateChangeHandler;

		this.monthSelection = new MonthSelection(YearMonth.from(time.getTime()), this::monthSelectionHandler, isBlockedByTime);
		this.yearSelection = new YearSelection(YearMonth.from(time.getTime()), this::yearSelectionHandle, isBlockedByTime);

		this.setBackground("modal-glass-background");

		innerTable.add(monthSelection).growX().center().padTop(20f).row();
		monthSelection.updateState(currentYearMonth);
		innerTable.add(yearSelection).growX().center().padTop(20f).row();
		yearSelection.updateState(currentYearMonth);

		updateCalendarMatrix();
		innerTable.add(calendarMatrixContainer).pad(20f);

		this.setResolutionChangeHandler(this::resize);
		this.fill(false);
		this.setFillParent(false);
		innerTable.setFillParent(false);
	}

	private void monthSelectionHandler(YearMonth newYearMonth) {
		if (newYearMonth.getMonth().equals(DECEMBER) && currentYearMonth.getMonth().equals(JANUARY)) {
			yearSelection.updateState(newYearMonth);
		} else if (newYearMonth.getMonth().equals(JANUARY) && currentYearMonth.getMonth().equals(DECEMBER)) {
			yearSelection.updateState(newYearMonth);
		}
		currentYearMonth = newYearMonth;
		updateCalendarMatrix();
	}

	private void yearSelectionHandle(YearMonth newYearMonth) {
		currentYearMonth = newYearMonth;
		monthSelection.updateState(newYearMonth);
		updateCalendarMatrix();
	}

	private void updateCalendarMatrix() {
		calendarMatrixContainer.setActor(new CalendarMatrix());
	}

	private void closeAll() {
		this.remove();
	}

	private void resize() {
		updateCalendarMatrix();
	}

	private class CalendarMatrix extends WrapperTable {
		public CalendarMatrix() {
			super();
			populateCalendarMatrix();
		}

		private void populateCalendarMatrix() {
			LocalDate firstDayOfMonth = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), 1);
			int daysInMonth = firstDayOfMonth.lengthOfMonth();
			int startingColumn = firstDayOfMonth.getDayOfWeek().getValue() % 7;
			int daysInPreviousMonth = firstDayOfMonth.minusMonths(1).lengthOfMonth();

			updateEventsMap(firstDayOfMonth, 7);

			populateDaysBeforeCurrentMonth(startingColumn, daysInPreviousMonth, currentYearMonth.minusMonths(1).getMonth());
			populateCurrentMonth(daysInMonth);
			populateDaysAfterCurrentMonth(daysInMonth, startingColumn != 0 ? startingColumn - 1 : 0, currentYearMonth.plusMonths(1).getMonth());
		}

		public void populateDaysBeforeCurrentMonth(int startingColumn, int daysInPreviousMonth, Month previousMonth) {
			IntStream.range(0, startingColumn - 1)
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(currentYearMonth.getYear(), previousMonth, daysInPreviousMonth - day)))
					.toList()
					.forEach(this::addButton);
		}

		public void populateCurrentMonth(int daysInMonth) {
			IntStream.range(1, daysInMonth + 1)
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), day)))
					.toList()
					.forEach(this::addButton);
		}

		public void populateDaysAfterCurrentMonth(int daysInMonth, int startingColumn, Month nextMonth) {
			System.out.println(startingColumn + " | " + daysInMonth);
			IntStream.range(1, 43 - (startingColumn + daysInMonth))
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(currentYearMonth.getYear(), nextMonth, day)))
					.toList()
					.forEach(this::addButton);
		}

		public void updateEventsMap(LocalDate firstDay, int numberOfWeeks) {
			eventsMap.clear();
			IntStream.range(0, numberOfWeeks)
					.mapToObj(firstDay::plusWeeks)
					.forEach(week -> Calendar.getInstance().getEventsForWeek(week)
							.forEach(event -> eventsMap.put(event.date(), event)));
		}

		private void addButton(CalendarCellButton button) {
			innerTable.add(button).grow().uniform();
			if (innerTable.getChildren().size % 7 == 0) {
				innerTable.row();
			}
		}

	}

	private class CalendarCellButton extends WrapperContainer<TextButton> {
		public CalendarCellButton(LocalDate date) {
			TextButton button = new TextButton(String.valueOf(date.getDayOfMonth()), skin);

			setStyle(button, date);

			if (date.getMonth().equals(currentYearMonth.getMonth())) {
				if (date.equals(chosenDate)) {
					button.setChecked(true);
				}
				addEventListener(button, date);
			} else
				button.setDisabled(true);

			this.setActor(button);
			this.resize();
		}

		public void setStyle(TextButton button, LocalDate date) {
			if (eventsMap.containsKey(date))
				button.setStyle(skin.get("calendar-special-cell", TextButton.TextButtonStyle.class));
			else
				button.setStyle(skin.get("calendar-cell", TextButton.TextButtonStyle.class));
		}

		public void addEventListener(TextButton button, LocalDate date) {
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					SoundAudio.BUTTON_3.play();
					closeAll();
					dateChangeHandler.accept(date);
					return true;
				}
			});
			if (eventsMap.containsKey(date)) {
				button.addListener(new DescriptionTooltip(eventsMap.get(date).title(), eventsMap.get(date).description()));
			}
		}

		private void resize() {
			switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> this.size(40f, 40f);
				case MEDIUM -> this.size(50f, 50f);
				case LARGE -> this.size(70f, 70f);
			}
		}

	}
}
