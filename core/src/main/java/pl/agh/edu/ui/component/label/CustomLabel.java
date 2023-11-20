package pl.agh.edu.ui.component.label;

import static pl.agh.edu.ui.audio.SoundAudio.POP;
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
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.ui.utils.SkinColor;
import pl.agh.edu.ui.utils.SkinToken;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public class CustomLabel extends WrapperContainer<Label> implements ResolutionChangeListener {
	private static final Skin skin = GameSkin.getInstance();
	private boolean hasUnderscore = false;
	private boolean isDisabled = false;
	private SkinColor baseColor = WARNING;
	private SkinColor.ColorLevel colorLevel = _300;

	protected final Label label;

	private final String token;

	private Runnable resolutionChangeHandler;

	public CustomLabel(String font) {
		super();
		this.token = "";
		this.label = new Label("", skin, font);
		this.setActor(label);

	}

	public CustomLabel(String font, SkinToken token) {
		super();
		TypingLabel label = new TypingLabel("", skin, font);
		label.setDefaultToken(token.getName());
		this.setActor(label);

		this.label = label;
		this.token = token.getName();
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void setAlignment(int labelAlign, int lineAlign) {
		label.setAlignment(labelAlign, lineAlign);
	}

	public void setStyle(Label.LabelStyle style) {
		label.setStyle(style);
	}

	public Label.LabelStyle getStyle() {
		return label.getStyle();
	}

	public void setWrap(boolean wrap) {
		label.setWrap(wrap);
	}

	public void setBackground(String backgroundPatch) {
		Label.LabelStyle labelStyle = new Label.LabelStyle(label.getStyle());
		labelStyle.background = new NinePatchDrawable(skin.getPatch(backgroundPatch));
		label.setStyle(labelStyle);
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
		Label.LabelStyle labelStyle = new Label.LabelStyle(label.getStyle());
		labelStyle.font = skin.getFont(font);
		label.setStyle(labelStyle);
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
		if (!token.isBlank()) {
			((TypingLabel) label).setDefaultToken(token + "{COLOR=#" + getCurrentColor().toString() + "}");
			((TypingLabel) label).parseTokens();
		} else {
			Label.LabelStyle labelStyle = new Label.LabelStyle(label.getStyle());
			labelStyle.fontColor = getCurrentColor();
			label.setStyle(labelStyle);
		}
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
							POP.playSound();
							return true;
						}
						return false;
					}

					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						if (!isDisabled) {
							colorLevel = _500;
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

	public void setUpResolutionChangeHandler(Runnable action) {
		resolutionChangeHandler = action;
		ResolutionManager.addListener(this);
	}

	@Override
	public Actor onResolutionChange() {
		resolutionChangeHandler.run();
		return this;
	}
}
