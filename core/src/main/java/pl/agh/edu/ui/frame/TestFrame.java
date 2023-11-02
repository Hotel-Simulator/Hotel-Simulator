package pl.agh.edu.ui.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.component.button.ScenarioButton;
import pl.agh.edu.ui.component.tab.TabSelector;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);
//		mainTable.add(new TabSelector("test.test", "test.test", () -> System.out.println("left"), () -> System.out.println("right"))).row();

		ButtonGroup<Button> buttons = new ButtonGroup();

		ScenarioButton btn1 = new ScenarioButton(HotelType.RESORT);
		ScenarioButton btn2 = new ScenarioButton(HotelType.HOTEL);
		ScenarioButton btn3 = new ScenarioButton(HotelType.SANATORIUM);


		buttons.add(btn1.getActor(), btn2.getActor(), btn3.getActor());
		buttons.setMaxCheckCount(1);
		buttons.setMinCheckCount(1);

		mainTable.add(btn1);
		mainTable.add(btn2);
		mainTable.add(btn3);
	}
}
