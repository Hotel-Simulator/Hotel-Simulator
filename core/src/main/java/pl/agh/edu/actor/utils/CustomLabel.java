package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.GameSkin;

public class CustomLabel extends Label {
	private boolean isUnderscore = false;

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

	public void setUnderscore(boolean isUnderscore) {
		this.isUnderscore = isUnderscore;

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (isUnderscore) {
			LabelStyle labelStyle = new LabelStyle(getStyle());
			NinePatchDrawable underscore = new NinePatchDrawable(GameSkin.getInstance().getPatch("underscore"));
			// underscore = underscore.tint(SkinColor(SECONDARY, _500));
			underscore.draw(batch, getX(), getY() - 2, getWidth(), 2);
		}

	}
}
