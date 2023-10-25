package pl.agh.edu.ui.component.label;

import java.util.List;

import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.Pair;

public class ReplaceLanguageLabel extends CustomLabel implements LanguageChangeListener {
	public final List<Pair<String, String>> replacementList;
	protected final String languagePath;

	public ReplaceLanguageLabel(String languagePath, String font, List<Pair<String, String>> replacementList) {
		super(font);
		this.replacementList = replacementList;
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		onLanguageChange();
	}

	@Override
	public void onLanguageChange() {
		if (replacementList == null || replacementList.isEmpty())
			this.setText(LanguageManager.get(languagePath));
		else
			this.setText(LanguageManager.get(languagePath, replacementList));
	}
}
