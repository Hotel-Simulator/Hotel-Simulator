package pl.agh.edu.ui.component.selection;

import static pl.agh.edu.ui.utils.SkinFont.BODY_1;

import java.time.YearMonth;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.label.CustomLabel;

public class YearSelection extends BaseSelection<YearMonth> {

	private final Time time = Time.getInstance();
	private final boolean isBlockedByTime;

	private static final int timeRange = 2;

	public YearSelection(YearMonth startValue, Consumer<YearMonth> action, Boolean isBlockedByTime) {
		super(startValue, createNewLabel(), action);
		this.isBlockedByTime = isBlockedByTime;
		this.checkButtons();
	}

	private static CustomLabel createNewLabel() {
		return new CustomLabel(BODY_1.getName());
	}

	public void updateState(YearMonth yearMonth) {
		setValue(yearMonth);
		this.updateLabel();
		this.checkButtons();
	}

	@Override
	protected boolean isNextButtonCheck() {
		if (isBlockedByTime)
			return getValue().plusYears(1).getYear() <= time.getYearMonth().plusYears(timeRange).getYear();
		return true;
	}

	@Override
	protected boolean isPreviousButtonCheck() {
		if (isBlockedByTime) {
			int newYear = getValue().minusYears(1).getYear();
			return newYear >= time.getYearMonth().minusYears(timeRange).getYear()
					&& newYear >= time.startingTime.getYear();
		}
		return true;
	}

	@Override
	protected void nextButtonHandler() {
		if (getValue().getYear() == time.getTime().getYear() + timeRange - 1)
			setValue(getValue().plusYears(1).withMonth(Math.min(time.startingTime.getMonthValue(), getValue().getMonthValue())));
		else
			setValue(getValue().plusYears(1));
	}

	@Override
	protected void previousButtonHandler() {
		if (getValue().getYear() == time.startingTime.getYear())
			setValue(getValue().minusYears(1).withMonth(Math.max(time.startingTime.getMonthValue(), getValue().getMonthValue())));
		else
			setValue(getValue().minusYears(1));
	}

	@Override
	protected void updateLabel(Label label) {
		label.setText(String.valueOf(getValue().getYear()));
	}
}
