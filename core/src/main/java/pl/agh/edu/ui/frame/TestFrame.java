package pl.agh.edu.ui.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.calendar.CalendarComponent;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		Container<CalendarComponent> calendarContainer = new Container<>();
		CalendarComponent calendarComponent = new CalendarComponent(
				Time.getInstance().getTime().toLocalDate(),
				System.out::println);

		calendarContainer.setActor(calendarComponent);
		mainTable.add(calendarContainer).align(Align.topLeft);

	}
}
