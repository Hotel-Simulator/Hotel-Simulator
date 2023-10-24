package pl.agh.edu.ui.component.rating;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;

public class Star extends Container<Button> {
	public final int index;
	private final Skin skin = GameSkin.getInstance();
	private final Button button = new Button(skin, "star-normal");

	public Star(int index, Rating rating) {
		this.setActor(button);
		this.size(StarStyle.getSize(), StarStyle.getSize());
		this.center();

		this.index = index;

		button.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return !rating.isDisabled();
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				rating.setRating(index);
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				if (!rating.isDisabled())
					rating.setOverRating(index);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (!rating.isDisabled()) {
					rating.setDefaultRating();
				}
			}
		});
	}

	public static float getSize() {
		return StarStyle.getSize();
	}

	private void changeStateToOver() {
		button.setStyle(skin.get("star-over", Button.ButtonStyle.class));
	}

	private void changeStateToDimmed() {
		button.setStyle(skin.get("star-dimmed", Button.ButtonStyle.class));
	}

	private void changeStateToNormal() {
		button.setStyle(skin.get("star-normal", Button.ButtonStyle.class));
	}

	public void updateState(int rating) {
		if (index <= rating) {
			changeStateToNormal();
		} else {
			changeStateToDimmed();
		}
	}

	public void updateOverState(int rating) {
		if (index <= rating) {
			changeStateToOver();
		} else {
			changeStateToDimmed();
		}

	}

	private static class StarStyle {
		public static float getSize() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}
	}
}
