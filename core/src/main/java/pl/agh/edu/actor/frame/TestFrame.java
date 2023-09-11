package pl.agh.edu.actor.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.component.ValueTag;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();
		root.add(new ValueTag("Deposit interest rate","7%", ValueTag.ValueTagSize.LARGE));

		this.add(root);
	}
}
