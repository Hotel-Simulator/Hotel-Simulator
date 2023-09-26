package pl.agh.edu.actor.component.rating;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.GraphicConfig;

public class Star extends Table {
	private final Skin skin = HotelSkin.getInstance();

	private final Button button = new Button(skin, "star-normal");
	public final int index;

	public Star(int index, Rating rating) {
		this.add(button).width(StarStyle.getSize()).height(StarStyle.getSize()).center();
		this.index = index;
		button.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				rating.setRating(index);
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				rating.setOverRating(index);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				rating.setDefaultRating();
			}
		});

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
	public static float getSize() {
		return StarStyle.getSize();
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
