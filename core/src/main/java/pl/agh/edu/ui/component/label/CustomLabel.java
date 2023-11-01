package pl.agh.edu.ui.component.label;

import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._900;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinColor.WARNING;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.SkinColor;

public class CustomLabel extends Label {
	private final Skin skin = GameSkin.getInstance();
	private boolean hasUnderscore = false;
	private boolean isDisabled = false;
	private SkinColor baseColor = WARNING;
	private SkinColor.ColorLevel colorLevel = _300;

	public CustomLabel(String font) {
		super("", GameSkin.getInstance(), font);
	}

	public void setBackground(String backgroundPatch) {
		LabelStyle labelStyle = new LabelStyle(this.getStyle());
		labelStyle.background = new NinePatchDrawable(skin.getPatch(backgroundPatch));
		this.setStyle(labelStyle);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (!hasUnderscore)
			return;

		NinePatch underscorePatch = new NinePatchDrawable(skin.getPatch("underscore"))
				.tint(getCurrentColor())
				.getPatch();

		underscorePatch.scale(1F, (float) Math.sqrt(getHeight() / 1000) + 0.05f);
		underscorePatch.draw(batch, getX(), getY(), getWidth(), 5);
	}

	public Color getCurrentColor() {
		return getSkinColor().getColor(colorLevel);
	}

	public void setFont(String font) {
		LabelStyle labelStyle = new LabelStyle(this.getStyle());
		labelStyle.font = skin.getFont(font);
		this.setStyle(labelStyle);
	}

	private SkinColor getSkinColor() {
		if (isDisabled)
			return GRAY;
		return baseColor;
	}

	public void setBaseColor(SkinColor baseColor) {
		this.baseColor = baseColor;
		updateColor();
	}

	private void updateColor() {
		LabelStyle labelStyle = new LabelStyle(this.getStyle());
		labelStyle.fontColor = getCurrentColor();
		this.setStyle(labelStyle);
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		updateColor();
	}

	public void setUnderscore(boolean hasUnderscore) {
		this.hasUnderscore = hasUnderscore;
	}

	public void makeItLink(Runnable linkAction) {
		addListener(
				new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							colorLevel = _900;
							SoundAudio.CLICK_2.play();
							return true;
						}
						return false;
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							colorLevel = _500;
							SoundAudio.BUTTON_2.play();
						}
					}

					@Override
					public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
						if (!isDisabled && pointer == -1) {
							colorLevel = _500;
						}
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						if (!isDisabled && pointer == -1) {
							colorLevel = _300;
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

}
