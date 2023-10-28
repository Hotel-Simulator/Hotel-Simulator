package pl.agh.edu.ui.component.label;

import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._300;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._500;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._700;
import static pl.agh.edu.ui.utils.SkinColor.ColorLevel._900;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinColor.SECONDARY;
import static pl.agh.edu.ui.utils.SkinSpecialColor.TRANSPARENT;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import com.badlogic.gdx.utils.Null;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.SkinColor;

public class CustomLabel extends Label {
	private Color underscoreColor = TRANSPARENT.getColor();
	private boolean isDisabled = true;
	private SkinColor baseColor = SECONDARY;

	public CustomLabel(String font, String backgroundPatch) {
		this(font);
		getStyle().background = new NinePatchDrawable(GameSkin.getInstance().getPatch(backgroundPatch));
	}

	public CustomLabel(String font) {
		super("", GameSkin.getInstance());
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = GameSkin.getInstance().getFont(font);
		this.setStyle(labelStyle);
	}

	public void setUnderscoreColor(Color color) {
		this.underscoreColor = color;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		NinePatch underscorePatch = new NinePatchDrawable(GameSkin.getInstance().getPatch("underscore"))
				.tint(underscoreColor)
				.getPatch();

		underscorePatch.scale(1F, (float) Math.sqrt(getHeight() / 1000) + 0.05f);
		underscorePatch.draw(batch, getX(), getY(), getWidth(), 5);

	}

	public void setFont(String font) {
		LabelStyle labelStyle = new LabelStyle(this.getStyle());
		labelStyle.font = GameSkin.getInstance().getFont(font);
		this.setStyle(labelStyle);
	}
	public void setBaseColor(SkinColor baseColor) {
		this.baseColor = baseColor;
		setLinkColor(baseColor.getColor(_300));
	}

	private void setLinkColor(Color color) {
		setColor(color);
		setUnderscoreColor(color);
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		setLinkColor(isDisabled ? GRAY.getColor(_700) : baseColor.getColor(_300));
	}

	public void makeItLink(Runnable linkAction){
		setLinkColor(baseColor.getColor(_300));
		setDisabled(false);

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
