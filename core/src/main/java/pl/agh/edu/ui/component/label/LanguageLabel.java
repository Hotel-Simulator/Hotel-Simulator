package pl.agh.edu.ui.component.label;

import com.badlogic.gdx.scenes.scene2d.Actor;

import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.SkinToken;
import pl.agh.edu.utils.LanguageString;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private LanguageString languageString;

	public LanguageLabel(LanguageString languageString, String font) {
		super(font);
		this.languageString = languageString;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	public LanguageLabel(LanguageString languageString, String font, SkinToken token) {
		super(font, token);
		this.languageString = languageString;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public Actor onLanguageChange() {
		this.setText(LanguageManager.get(languageString));
		return this;
	}

	public void updateLanguageString(LanguageString languageString) {
		this.languageString = languageString;
		onLanguageChange();
	}
}
