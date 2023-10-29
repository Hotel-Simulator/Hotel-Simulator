package pl.agh.edu.ui.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.calendar.Calendar;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		Container<Calendar> calendarContainer = new Container<>();
		Calendar calendar = new Calendar(
				Time.getInstance().getTime().toLocalDate(),
				System.out::println);

		calendarContainer.setActor(calendar);
		mainTable.add(calendarContainer).align(Align.topLeft);

	}
}
