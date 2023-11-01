package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.engine.time.Frequency;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.CustomLabel;

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
	}

	private void setTime() {
		timeLabel.setText(time.getStringTime());
	}

	public void initializeSyncWithClock() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_TIME_TICK, this::setTime, time.getTime()));
	}

	private static class TimeLabel extends CustomLabel {
		public TimeLabel() {
			super(H4.getName());
			this.setBackground("label-time-background");
			this.setAlignment(center, center);
		}
	}
}
