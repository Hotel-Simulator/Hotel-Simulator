package pl.agh.edu.ui.component.calendar;

import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.utils.Align.center;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static pl.agh.edu.ui.utils.FontType.BODY_1;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class Calendar extends WrapperTable {
	private final Skin skin = GameSkin.getInstance();
	private final Time time = Time.getInstance();
	private Month month = time.getTime().getMonth();
	private int year = time.getTime().getYear();
	private final String languagePathPrefix = "calendar.month.";
	LanguageLabel monthLabel = new LanguageLabel(languagePathPrefix + time.getTime().getMonth().toString().toLowerCase(), BODY_1.getName());
	Label yearLabel = new Label(String.valueOf(year), skin);
	private final Table yearTable = new Table();
	private final Table monthTable = new Table();
	private final Container<CalendarMatrix> calendarMatrixContainer = new Container<>();

	private final LocalDate chosenDate;

	private final Consumer<LocalDate> dateChangeHandler;

	public Calendar(LocalDate chosenDate, Consumer<LocalDate> dateChangeHandler) {
		this.chosenDate = chosenDate;
		this.dateChangeHandler = dateChangeHandler;

		this.setBackground("modal-glass-background");

		setUpYearTable();
		innerTable.add(yearTable).growX().padTop(10f).row();

		setUpMonthTable();
		innerTable.add(monthTable).growX().padTop(10f).row();

		updateCalendarMatrix();
		innerTable.add(calendarMatrixContainer).pad(30f);

		this.setResolutionChangeHandler(this::resize);
	}
	private void updateYearLabel() {
		yearLabel.setText(String.valueOf(year));
	}

	private void updateMonthLabel() {
		monthLabel.updateLanguagePath(languagePathPrefix + month.toString().toLowerCase());
	}

	private void updateCalendarMatrix() {
		calendarMatrixContainer.setActor(new CalendarMatrix());
	}

	private void closeAll() {
		this.remove();
	}

	private void setUpYearTable() {
		yearTable.center();
		yearLabel.setAlignment(center, center);
		Button leftButton = new Button(skin, "selection-left");
		leftButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				year--;
				updateYearLabel();
				updateCalendarMatrix();
				return true;
			}
		});
		Button rightButton = new Button(skin, "selection-right");
		rightButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				year++;
				updateYearLabel();
				updateCalendarMatrix();
				return true;
			}
		});
		yearTable.add(leftButton);
		yearTable.add(yearLabel).width(200f);
		yearTable.add(rightButton);
	}

	private void resize() {
		updateCalendarMatrix();
	}

	private void setUpMonthTable() {
		monthTable.center();
		monthLabel.setAlignment(center, center);
		Button leftButton = new Button(skin, "selection-left");
		leftButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				if (month == JANUARY) {
					year--;
					updateYearLabel();
				}
				month = month.minus(1);
				updateMonthLabel();
				updateCalendarMatrix();
				return true;
			}
		});
		Button rightButton = new Button(skin, "selection-right");
		rightButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				if (month == DECEMBER) {
					year++;
					updateYearLabel();
				}
				month = month.plus(1);
				updateMonthLabel();
				updateCalendarMatrix();
				return true;
			}
		});
		monthTable.add(leftButton);
		monthTable.add(monthLabel).width(200f);
		monthTable.add(rightButton);
	}

	private class CalendarMatrix extends WrapperTable {
		public CalendarMatrix() {
			super();
			populateCalendarMatrix();
		}

		private void populateCalendarMatrix() {
			LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
			int daysInMonth = firstDayOfMonth.lengthOfMonth();
			int startingColumn = firstDayOfMonth.getDayOfWeek().getValue() % 7;

			Month previousMonth = month.minus(1);
			int daysInPreviousMonth = firstDayOfMonth.minusMonths(1).lengthOfMonth();
			IntStream.range(0, startingColumn)
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(year, previousMonth, daysInPreviousMonth - day)))
					.toList()
					.forEach(this::addButton);

			IntStream.range(1, daysInMonth + 1)
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(year, month, day)))
					.toList()
					.forEach(this::addButton);

			Month nextMonth = month.plus(1);
			IntStream.range(1, 43 - (startingColumn + daysInMonth))
					.mapToObj(day -> new CalendarCellButton(LocalDate.of(year, nextMonth, day)))
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
			TextButton button = new TextButton(String.valueOf(date.getDayOfMonth()), skin, "calendar-cell");
			if (date.getMonth().equals(month)) {
				if (date.equals(chosenDate))
					button.setChecked(true);
				button.addListener(new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						SoundAudio.BUTTON_3.play();
						closeAll();
						dateChangeHandler.accept(date);
						return true;
					}
				});
			} else
				button.setDisabled(true);

			this.setActor(button);
			this.resize();
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
