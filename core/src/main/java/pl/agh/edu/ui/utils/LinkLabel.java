package pl.agh.edu.ui.utils;

import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._700;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._900;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinColor.SECONDARY;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.label.LanguageLabel;

public class LinkLabel extends LanguageLabel {
	private boolean isDisabled = false;
	private SkinColor baseColor = SECONDARY;
	private Runnable linkAction;
	ClickListener clickListener;

	public LinkLabel(String languagePath, String font, Runnable linkAction) {
		super(languagePath, font);
		setLinkColor(baseColor.getColor(_300));
		addListener(
				new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							setLinkColor(baseColor.getColor(_900));
							SoundAudio.CLICK_2.play();
							return true;
						}
						return false;
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							setLinkColor(baseColor.getColor(_500));
							SoundAudio.BUTTON_2.play();
						}
					}

					@Override
					public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
						if (!isDisabled && pointer == -1) {
							setLinkColor(baseColor.getColor(_500));

						}
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						if (!isDisabled && pointer == -1) {
							setLinkColor(baseColor.getColor(_300));
						}
					}
				});
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!isDisabled) {
					linkAction.run();
				}
			}
		};
			addListener(clickListener);
	}

	public void setLinkAction(Runnable linkAction){
		removeListener(clickListener);
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!isDisabled) {
					linkAction.run();
				}
			}
		};
		addListener(clickListener);
	}

	public void setBaseColor(SkinColor baseColor) {
		this.baseColor = baseColor;
		setLinkColor(baseColor.getColor(_300));
	}

	private void setLinkColor(Color color) {
		super.setColor(color);
		super.setUnderscoreColor(color);
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		setLinkColor(isDisabled ? GRAY.getColor(_700) : baseColor.getColor(_300));
	}
}
