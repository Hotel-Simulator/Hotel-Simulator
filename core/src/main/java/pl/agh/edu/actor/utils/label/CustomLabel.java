package pl.agh.edu.actor.utils.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.SkinSpecialColor;

public class CustomLabel extends Label {
	private Color underscoreColor = SkinSpecialColor.TRANSPARENT.getColor();

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

		NinePatchDrawable underscoreDrawable = new NinePatchDrawable(GameSkin.getInstance().getPatch("underscore"));
		underscoreDrawable = underscoreDrawable.tint(underscoreColor);

		NinePatch underscorePatch = underscoreDrawable.getPatch();
		underscorePatch.scale(1F, (float) Math.sqrt(getHeight() / 1000) + 0.05f);
		underscorePatch.draw(batch, getX(), getY(), getWidth(), 5);

	}

	public void setFont(String font){
		LabelStyle labelStyle = new LabelStyle(this.getStyle());
		labelStyle.font = GameSkin.getInstance().getFont(font);
		this.setStyle(labelStyle);
	}
}
