package pl.agh.edu.ui.component.label;

import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private String languagePath;

	public LanguageLabel(String languagePath, String font) {
		super(font);
		setLanguagePath(languagePath);
	}

	public LanguageLabel(String languagePath, String font, String background) {
		super(font, background);
		setLanguagePath(languagePath);
	}

	private void setLanguagePath(String languagePath) {
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		this.setText(LanguageManager.get(languagePath));
	}

}
