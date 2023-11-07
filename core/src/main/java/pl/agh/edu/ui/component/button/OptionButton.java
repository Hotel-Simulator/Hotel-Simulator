package pl.agh.edu.ui.component.button;

import static pl.agh.edu.ui.audio.SoundAudio.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.ui.GameSkin;

public class OptionButton extends Button {
	public OptionButton(Runnable clickedHandler) {
		super(GameSkin.getInstance(), "options");
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				POP.playSound();
				clickedHandler.run();
			}
		});
	}
}
