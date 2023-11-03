package pl.agh.edu.ui.frame;

import static pl.agh.edu.ui.utils.SkinColor.ALERT;

import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.utils.LanguageString;

public class TestFrame extends BaseFrame {
	public TestFrame(LanguageString languageString) {
		super(languageString);

		LanguageLabel label = new LanguageLabel(new LanguageString("test.test"), "h1");
		label.setBaseColor(ALERT);
		label.setUnderscore(true);
		label.setBackground("value-tag-background");
		label.setDisabled(true);
		label.setDisabled(false);
		label.makeItLink(() -> System.out.println("test"));
		label.setDisabled(true);

		mainTable.add(label);

	}
}
