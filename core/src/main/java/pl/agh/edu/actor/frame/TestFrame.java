package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.component.TextComponents.ValueTag;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();
		root.add(new ValueTag("Deposit interest rate","7%", ValueTag.ValueTagSize.LARGE));
		root.row();
		root.add(new ValueTag("Deposit żółć  ęą interest rate","7%", ValueTag.ValueTagSize.MEDIUM));
		root.row();
		root.add(new ValueTag("Debt","500$", ValueTag.ValueTagSize.SMALL));
		this.add(root);
	}
}
