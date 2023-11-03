package pl.agh.edu.ui.frame;

import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.tooltip.DescriptionTooltip;
import static pl.agh.edu.ui.utils.SkinColor.ALERT;
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

		label.addListener(new DescriptionTooltip(new LanguageString("test.long"), new LanguageString("test.long")));

		mainTable.add(label);

	}
}
