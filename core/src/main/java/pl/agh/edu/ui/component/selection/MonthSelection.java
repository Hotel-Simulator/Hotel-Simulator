package pl.agh.edu.ui.component.selection;

import static pl.agh.edu.ui.utils.SkinFont.BODY_1;

import java.time.YearMonth;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.utils.LanguageString;

public class MonthSelection extends BaseSelection<YearMonth> {
	private static final Time time = Time.getInstance();
	private final boolean isBlockedByTime;

	public MonthSelection(YearMonth startValue, Consumer<YearMonth> action, Boolean isBlockedByTime) {
		super(startValue, createNewLabel(), action);
		this.isBlockedByTime = isBlockedByTime;
		this.checkButtons();
	}

	public void updateState(YearMonth yearMonth) {
		setValue(yearMonth);
		this.updateLabel();
		this.checkButtons();
	}

	private static LanguageLabel createNewLabel() {
		return new LanguageLabel(getMonthLanguageString(YearMonth.from(time.startingTime)), BODY_1.getName());
	}

	@Override
	protected boolean isNextButtonCheck() {
		if (isBlockedByTime)
			return getValue().plusMonths(1).isBefore(time.getYearMonth().plusYears(2));
		return true;
	}

	@Override
	protected boolean isPreviousButtonCheck() {
		if (isBlockedByTime)
			return (getValue().minusMonths(1).isAfter(time.getYearMonth().minusYears(2))
					&& !getValue().minusMonths(1).isBefore(YearMonth.from(time.startingTime)));
		return true;
	}

	@Override
	protected void nextButtonHandler() {
		setValue(getValue().plusMonths(1));
	}

	@Override
	protected void previousButtonHandler() {
		setValue(getValue().minusMonths(1));
	}

	@Override
	protected void updateLabel(Label label) {
		if (label instanceof LanguageLabel) {
			((LanguageLabel) label).updateLanguagePath(getMonthLanguageString(getValue()));
		}
	}

	private static LanguageString getMonthLanguageString(YearMonth yearMonth) {
		return new LanguageString("selection.month." + yearMonth.getMonth().toString().toLowerCase());
	}
}
