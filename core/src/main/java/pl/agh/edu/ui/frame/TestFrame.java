package pl.agh.edu.ui.frame;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.selection.MonthSelection;
import pl.agh.edu.ui.component.selection.YearSelection;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		MonthSelection monthSelection = new MonthSelection(Time.getInstance().getYearMonth(), System.out::println, false);
		YearSelection yearSelection = new YearSelection(Time.getInstance().getYearMonth(), System.out::println, false);

		MonthSelection monthSelection2 = new MonthSelection(Time.getInstance().getYearMonth(), System.out::println, true);
		YearSelection yearSelection2 = new YearSelection(Time.getInstance().getYearMonth(), System.out::println, true);
		monthSelection2.changeStyleToBig();
		yearSelection2.changeStyleToBig();

		mainTable.add(monthSelection).growX().center().padTop(20f).row();
		mainTable.add(yearSelection).growX().center().padTop(20f).row();
		mainTable.add(monthSelection2).growX().center().padTop(20f).row();
		mainTable.add(yearSelection2).growX().center().padTop(20f).row();

		mainTable.debugCell();

	}
}
