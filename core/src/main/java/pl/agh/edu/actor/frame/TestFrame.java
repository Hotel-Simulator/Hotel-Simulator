package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.TextActors.ValueTag;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		ValueTag valueTag = new ValueTag("valueTag.credit.interest", "3%");

		root.add(valueTag);
		this.add(root);
	}
}
