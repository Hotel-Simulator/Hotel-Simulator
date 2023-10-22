package pl.agh.edu.actor.frame;

import pl.agh.edu.actor.utils.label.CustomLabel;
import pl.agh.edu.actor.utils.SkinColor;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		CustomLabel label = new CustomLabel("body1");
		label.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		label.setText("test");
		mainTable.add(label);
		mainTable.row().row();
		CustomLabel label2 = new CustomLabel("h1");
		label2.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		label2.setText("test");
		mainTable.add(label2);
	}
}
