package pl.agh.edu.actor.frame;

import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.LinkLabel;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		LinkLabel label = new LinkLabel("test.test", FontType.BUTTON_1.getWhiteVariantName(), () -> System.out.println("test"));
		innerTable.add(label);
		innerTable.row();
		LinkLabel label2 = new LinkLabel("test.test", FontType.H1.getWhiteVariantName(), () -> System.out.println("test"));
		label2.setDisabled(true);
		innerTable.add(label2);

	}
}
