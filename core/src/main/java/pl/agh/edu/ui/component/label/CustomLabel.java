package pl.agh.edu.ui.component.label;

import static pl.agh.edu.ui.utils.SkinSpecialColor.TRANSPARENT;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.ui.GameSkin;

public class CustomLabel extends Label {
	private Color underscoreColor = TRANSPARENT.getColor();

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
}
