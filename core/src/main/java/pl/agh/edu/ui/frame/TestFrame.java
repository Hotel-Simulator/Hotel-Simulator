package pl.agh.edu.ui.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.tab.TabSelector;
import pl.agh.edu.ui.utils.SkinColor;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		mainTable.add(new TabSelector("test.test", "test.test", () -> {
		}, () -> {
		}));

		Button button = new Button();
		ClickListener clickListener = new ClickListener() {
			@Override
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				System.out.println("dupa");
			}
		};
		button.addListener(clickListener);
		ClickListener changedListener = new ClickListener() {
			@Override
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				System.out.println("dupa2");
			}
		};
		button.removeListener(clickListener);
		button.addListener(changedListener);
		mainTable.add(button);

	}
}
