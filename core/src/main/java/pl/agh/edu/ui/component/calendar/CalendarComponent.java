package pl.agh.edu.ui.component.calendar;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.calendar.Calendar;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.selection.MonthSelection;
import pl.agh.edu.ui.component.selection.YearSelection;
import pl.agh.edu.ui.component.tooltip.EventDescriptionTooltip;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class CalendarComponent extends WrapperTable {
	private final Time time = Time.getInstance();
	private final Container<CalendarMatrix> calendarMatrixContainer = new Container<>();
	private final LocalDate chosenDate;
	private final Consumer<LocalDate> dateChangeHandler;
	private YearMonth currentYearMonth;
	private final MonthSelection monthSelection;
	private final YearSelection yearSelection;
	private final boolean isActive;
	private final Calendar calendar = Calendar.getInstance();

	public CalendarComponent(LocalDate chosenDate, Consumer<LocalDate> dateChangeHandler, Boolean isBlockedByTime, Boolean isActive) {
		super();
		this.chosenDate = chosenDate;
		this.dateChangeHandler = dateChangeHandler;
		this.isActive = isActive;

		currentYearMonth = YearMonth.from(chosenDate);

		this.monthSelection = new MonthSelection(YearMonth.from(time.getTime()), this::monthSelectionHandler, isBlockedByTime);
		this.yearSelection = new YearSelection(YearMonth.from(time.getTime()), this::yearSelectionHandle, isBlockedByTime);

		this.setBackground("modal-glass-background");

		innerTable.add(monthSelection).growX().center().row();
		monthSelection.updateState(currentYearMonth);
		innerTable.add(yearSelection).growX().center().row();
		yearSelection.updateState(currentYearMonth);
		this.resize();

		updateCalendarMatrix();
		innerTable.add(calendarMatrixContainer);

		this.setResolutionChangeHandler(this::resize);
		innerTable.setFillParent(false);
	}

	private void monthSelectionHandler(YearMonth newYearMonth) {
		currentYearMonth = newYearMonth;
		yearSelection.updateState(newYearMonth);
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
		monthSelection.pad(CalendarComponentStyle.getPadding());
		yearSelection.pad(CalendarComponentStyle.getPadding());
		calendarMatrixContainer.pad(CalendarComponentStyle.getPadding());
		innerTable.pad(CalendarComponentStyle.getPadding());
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
			IntStream.range(1, 43 - (startingColumn + daysInMonth))
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(currentYearMonth.getYear(), nextMonth, day)))
					.toList()
					.forEach(this::addButton);
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
			if (!isActive)
				button.removeListener(button.getClickListener());
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
			if (!calendar.getEventsForDate(date).isEmpty())
				button.setStyle(skin.get("calendar-special-cell", TextButton.TextButtonStyle.class));
			else
				button.setStyle(skin.get("calendar-cell", TextButton.TextButtonStyle.class));
		}

		public void addEventListener(TextButton button, LocalDate date) {
			if (isActive) {
				button.addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						SoundAudio.BUTTON_3.playAudio();
						closeAll();
						dateChangeHandler.accept(date);
						return true;
					}
				});
			}
			if (!calendar.getEventsForDate(date).isEmpty()) {
				button.addListener(new EventDescriptionTooltip(calendar.getEventsForDate(date)));
			}
		}

		private void resize() {
			this.size(CalendarComponentStyle.getCellSize());
		}
	}

	private static class CalendarComponentStyle {
		private static float getCellSize() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 50f;
				case MEDIUM -> 60f;
				case LARGE -> 70f;
			};
		}

		private static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}
	}
}
