package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.resolution.Size;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		LabeledButton button = new LabeledButton(Size.SMALL, "test.long");

		root.add(button);

		this.add(root);
	}
}
