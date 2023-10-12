package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class LanguageLabel extends Label implements LanguageChangeListener {
	private final String languagePath;

	public LanguageLabel(String languagePath, String font) {
		super("", HotelSkin.getInstance(), font);
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		this.setText(LanguageManager.get(languagePath));
	}

}
