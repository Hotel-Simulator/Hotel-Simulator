package pl.agh.edu.actor.component.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.model.time.Time;

public class AccelerationPanel extends WrapperTable {

	private final Skin skin = GameSkin.getInstance();
	private final Label accelerationLabel = new Label(time.getStringAcceleration(), skin, FontType.H4.getName());
	private final Button playButton = new Button(skin, "navbar-play");
	private final Button increaseButton = new Button(skin, "navbar-plus");
	private final Button decreaseButton = new Button(skin, "navbar-minus");
	private static final Time time = Time.getInstance();

	public AccelerationPanel() {

		accelerationLabel.setAlignment(Align.center, Align.center);
		this.setBackground("navbar-acceleration-background");

		increaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundAudio.BUTTON_1.play();
				Time.getInstance().increaseAcceleration();
				setAcceleration();
			}
		});

		decreaseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundAudio.BUTTON_1.play();
				Time.getInstance().decreaseAcceleration();
				setAcceleration();
			}
		});

		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (playButton.isChecked()) {
					playTime();
					SoundAudio.BUTTON_2.play();
				} else {
					stopTime();
					SoundAudio.BUTTON_3.play();
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
