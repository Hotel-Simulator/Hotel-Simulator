package pl.agh.edu.actor.utils.label;

import java.util.List;

import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;
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
