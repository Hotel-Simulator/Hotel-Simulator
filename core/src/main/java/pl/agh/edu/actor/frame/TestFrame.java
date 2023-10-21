package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.utils.CustomLabel;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		CustomLabel label = new CustomLabel("button1");
		label.setUnderscore(true);
		label.setText("test");
		root.add(label);

		this.add(root);
	}
}
