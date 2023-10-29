package pl.agh.edu.ui.frame;

import pl.agh.edu.ui.component.tab.TabSelector;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);
		mainTable.add(new TabSelector("test.test", "test.test", () -> System.out.println("left"), () -> System.out.println("right"))).row();

	}
}
