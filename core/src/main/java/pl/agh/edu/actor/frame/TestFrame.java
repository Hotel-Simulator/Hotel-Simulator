package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.TextActors.ValueTag;
import pl.agh.edu.actor.component.rating.Rating;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		ValueTag valueTag = new ValueTag("sth", "sth");

		root.add(valueTag);
		this.add(root);
	}
}
