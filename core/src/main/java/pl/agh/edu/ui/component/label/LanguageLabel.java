package pl.agh.edu.ui.component.label;

import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;

public class LanguageLabel extends CustomLabel implements LanguageChangeListener {
	private final String languagePath;
	private Object[] varArgs;

	public LanguageLabel(String languagePath, String font) {
		super(font);
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	public void setVarArgs(Object... varArgs) {
		this.varArgs = varArgs;
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		if (varArgs != null) {
			this.setText(LanguageManager.bound(languagePath, varArgs));
		} else {
			this.setText(LanguageManager.get(languagePath));
		}
	}

}
