package pl.agh.edu.ui.frame;

import static pl.agh.edu.ui.utils.FontType.BODY_1;
import static pl.agh.edu.ui.utils.FontType.H1;

import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.SkinColor;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		CustomLabel label = new CustomLabel("body1");
		label.setText("test");
		mainTable.add(label);
		mainTable.row().row();

		CustomLabel label2 = new CustomLabel(H1.getWhiteVariantName());
		label2.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		label2.setText("test");
		label2.makeItLink(() -> System.out.println("h1"));
		mainTable.add(label2);

		LanguageLabel languageLabel1 = new LanguageLabel("test.test", BODY_1.getWhiteVariantName());
		mainTable.add(languageLabel1).row();
		languageLabel1.makeItLink(() -> System.out.println("test"));
		languageLabel1.setDisabled(true);

	}
}
