package pl.agh.edu.ui.frame;

import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.utils.SkinFont;
import pl.agh.edu.utils.LanguageString;

public class TestFrame extends BaseFrame {
	public TestFrame(LanguageString languageString) {
		super(languageString);

		CustomLabel label = new CustomLabel(SkinFont.H4.getWhiteVariantName());
		label.setText("Test");
		label.makeItLink(() -> {});
		label.setUnderscore(true);

		mainTable.add(label).row();

		// MonthSelection monthSelection = new MonthSelection(Time.getInstance().getYearMonth(), System.out::println, false);
		// YearSelection yearSelection = new YearSelection(Time.getInstance().getYearMonth(), System.out::println, false);
		//
		// MonthSelection monthSelection2 = new MonthSelection(Time.getInstance().getYearMonth(), System.out::println, true);
		// YearSelection yearSelection2 = new YearSelection(Time.getInstance().getYearMonth(), System.out::println, true);
		// monthSelection2.changeStyleToBig();
		// yearSelection2.changeStyleToBig();
		//
		// mainTable.add(monthSelection).growX().center().padTop(20f).row();
		// mainTable.add(yearSelection).growX().center().padTop(20f).row();
		// mainTable.add(monthSelection2).growX().center().padTop(20f).row();
		// mainTable.add(yearSelection2).growX().center().padTop(20f).row();
		//
		// mainTable.debugCell();

	}
}
