package pl.agh.edu.actor.component.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.actor.HotelSkin;

public class OptionButton extends Button {
	public OptionButton(Runnable clickedCallback) {
		super(HotelSkin.getInstance(), "options");
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickedCallback.run();
			}
		});
	}
}
