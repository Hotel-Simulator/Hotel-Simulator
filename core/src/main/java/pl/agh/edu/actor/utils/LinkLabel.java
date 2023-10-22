package pl.agh.edu.actor.utils;

import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._700;
import static pl.agh.edu.actor.utils.SkinColor.ColorLevel._900;
import static pl.agh.edu.actor.utils.SkinColor.GRAY;
import static pl.agh.edu.actor.utils.SkinColor.SECONDARY;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

public class LinkLabel extends LanguageLabel {
	private boolean isDisabled = false;

	public LinkLabel(String languagePath, String font, Runnable linkAction) {
		super(languagePath, font);
		setColor(SECONDARY.getColor(_300));
		addListener(
				new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							setColor(SECONDARY.getColor(_900));
							return true;
						}
						return false;
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							setColor(SECONDARY.getColor(_500));
							event.cancel();
						}
					}

					@Override
					public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
						if (!isDisabled && pointer == -1) {
							setColor(SECONDARY.getColor(_500));

						}
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						if (!isDisabled && pointer == -1) {
							setColor(SECONDARY.getColor(_300));
						}
					}
				});
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!isDisabled) {
					linkAction.run();
				}
			}
		});
	}

	@Override
	public void setColor(Color color) {
		super.setColor(color);
		setUnderscoreColor(color);
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		setColor(isDisabled ? GRAY.getColor(_700) : SECONDARY.getColor(_300));
	}
}
