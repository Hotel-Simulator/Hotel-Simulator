package pl.agh.edu.ui.utils;

import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ShadowBackground extends Table implements GameSkinProvider {
	private final Actor actor;

	public ShadowBackground(Actor actor, Runnable action) {
		super();
		this.actor = actor;
		this.setTouchable(Touchable.enabled);
		this.setFillParent(true);
		this.setBackground(getGameSkin().getDrawable("shadow-background"));
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!isActorAbove(x, y)) {
					CLICK.playSound();
					action.run();
				}
				return true;
			}
		});
	}

	private boolean isActorAbove(float x, float y) {
		Vector2 vector2 = this.actor.localToStageCoordinates(new Vector2(0, 0));

		float actorWidth = actor.getWidth();
		float actorHeight = actor.getHeight();

		vector2.x -= actorWidth / 2;
		vector2.y -= actorHeight / 2;

		return x >= vector2.x && x <= vector2.x + actorWidth &&
				y >= vector2.y && y <= vector2.y + actorHeight;
	}
}
