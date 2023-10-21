package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private final String languagePath;

	public LanguageLabel(String languagePath, String font) {
		super(font);
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		this.setText(LanguageManager.get(languagePath));
	}

	public void setUnderscore() {
		LabelStyle labelStyle = new LabelStyle(getStyle());
		NinePatchDrawable underscore = new NinePatchDrawable(GameSkin.getInstance().getPatch("underscore"));

		// underscore = underscore.tint(SkinColor(SECONDARY, _500));

		labelStyle.background = underscore;
		setStyle(labelStyle);
	}
}
