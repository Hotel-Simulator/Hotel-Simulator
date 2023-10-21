package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.utils.CustomLabel;
import pl.agh.edu.actor.utils.SkinColor;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		CustomLabel label = new CustomLabel("body1");
		label.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		label.setText("test");
		root.add(label);
		root.row().row();
		CustomLabel label2 = new CustomLabel("h1");
		label2.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		label2.setText("test");
		root.add(label2);

		this.add(root);
	}
}
