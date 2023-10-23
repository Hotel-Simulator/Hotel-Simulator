package pl.agh.edu.actor.utils.label;

import pl.agh.edu.actor.utils.label.CustomLabel;
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

}
