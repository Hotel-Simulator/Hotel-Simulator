package pl.agh.edu.ui.frame;

import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.tab.TabSelector;
import pl.agh.edu.ui.utils.SkinColor;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		mainTable.add(new TabSelector("test.test", "test.test", () -> {
		}, () -> {
		}));
	}
}
