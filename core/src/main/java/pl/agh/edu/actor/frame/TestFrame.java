package pl.agh.edu.actor.frame;

import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.resolution.Size;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super(name);

		LabeledButton button = new LabeledButton(Size.SMALL, "test.long");

		mainTable.add(button);
	}
}
