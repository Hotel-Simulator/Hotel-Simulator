package pl.agh.edu.actor.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.enums.Frequency;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class TimePanel extends Table {
	private final Label timeLabel;
	private static final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();

	private static final Time time = Time.getInstance();

	public TimePanel() {
		Skin skin = HotelSkin.getInstance();
		Label.LabelStyle labelStyle = skin.get("navbar", Label.LabelStyle.class);
		timeLabel = new Label(time.getStringTime(), labelStyle);
		this.pad(0, 0, 0, 0);
		timeLabel.setAlignment(Align.center);
		add(timeLabel);

		initializeSyncWithClock();
	}

	private void setTime() {
		timeLabel.setText(time.getStringTime());
	}

	public void initializeSyncWithClock() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_TIME_TICK, this::setTime, time.getTime()));
	}
}
