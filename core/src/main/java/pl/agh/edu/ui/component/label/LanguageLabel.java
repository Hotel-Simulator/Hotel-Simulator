package pl.agh.edu.ui.component.label;

import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private String languagePath;

	public LanguageLabel(String languagePath, String font) {
		super(font);
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	public void updateLanguagePath(String languagePath) {
		this.languagePath = languagePath;
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		this.setText(LanguageManager.get(languagePath));
	}

}
