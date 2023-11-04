package pl.agh.edu.ui.component.navbar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.engine.time.Frequency.EVERY_TIME_TICK;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import static pl.agh.edu.ui.audio.SoundAudio.BUTTON_1;
import pl.agh.edu.ui.component.calendar.CalendarLayer;
import pl.agh.edu.ui.component.label.CustomLabel;
import static pl.agh.edu.ui.utils.SkinFont.H4;

public class TimePanel extends Table {
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private static final Time time = Time.getInstance();
	private final Label timeLabel;

	public TimePanel() {
		timeLabel = new TimeLabel();
		this.pad(0, 0, 0, 0);
		add(timeLabel).grow().center();
		setTime();
		initializeSyncWithClock();

		this.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BUTTON_1.playAudio();
				getStage().addActor(setUpCalendarLayer());
			}
		});
	}

	private CalendarLayer setUpCalendarLayer() {
		return new CalendarLayer(this, time.getTime().toLocalDate(), true);
	}

	private void setTime() {
		timeLabel.setText(time.getStringTime());
	}

	public void initializeSyncWithClock() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_TIME_TICK, this::setTime, time.getTime()));
	}

	private static class TimeLabel extends CustomLabel {
		public TimeLabel() {
			super(H4.getName());
			this.setBackground("label-time-background");
			this.setAlignment(center, center);
		}
	}
}
