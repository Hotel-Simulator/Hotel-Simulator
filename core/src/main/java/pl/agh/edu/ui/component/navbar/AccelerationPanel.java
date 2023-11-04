package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.BUTTON_1;
import static pl.agh.edu.ui.audio.SoundAudio.BUTTON_2;
import static pl.agh.edu.ui.audio.SoundAudio.BUTTON_3;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class AccelerationPanel extends WrapperTable {

	private static final Time time = Time.getInstance();
	private final Label accelerationLabel = new Label(time.getStringAcceleration(), skin, H4.getName());
	private final Button playButton = new Button(skin, "navbar-play");

	public AccelerationPanel() {

		accelerationLabel.setAlignment(center, center);
		this.setBackground("navbar-acceleration-background");

		Button increaseButton = new Button(skin, "navbar-plus");
		increaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BUTTON_1.playAudio();
				Time.getInstance().increaseAcceleration();
				setAcceleration();
			}
		});

		Button decreaseButton = new Button(skin, "navbar-minus");
		decreaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				BUTTON_1.playAudio();
				Time.getInstance().decreaseAcceleration();
				setAcceleration();
			}
		});

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (playButton.isChecked()) {
					playTime();
					BUTTON_2.playAudio();
				} else {
					stopTime();
					BUTTON_3.playAudio();
				}
			}
		});

		innerTable.add(decreaseButton);
		innerTable.add(accelerationLabel).grow();
		innerTable.add(increaseButton);
		innerTable.add(playButton);

		this.size(220, 80);

		time.addTimeStopChangeHandler(() -> this.playButton.setChecked(false));
	}

	private void playTime() {
		playButton.setChecked(true);
		time.start();
	}

	private void stopTime() {
		playButton.setChecked(false);
		time.stop();
	}

	public void setAcceleration() {
		accelerationLabel.setText(time.getStringAcceleration());
	}

}
