package pl.agh.edu.actor.utils;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public abstract class WrapperContainer<T extends Actor> extends Container<T> implements LanguageChangeListener, ResolutionChangeListener {
	private final String languagePath;
	private Consumer<String> languageChangeHandler;
	private Runnable resolutionChangeHandler;

	public WrapperContainer(String languagePath) {
		this.languagePath = languagePath;
		LanguageManager.addListener(this);
		ResolutionManager.addListener(this);
	}

	public WrapperContainer() {
		this.languagePath = null;
		ResolutionManager.addListener(this);
	}

	public void setLanguageChangeHandler(Consumer<String> languageChangeHandler) {
		this.languageChangeHandler = languageChangeHandler;
	}

	public void setResolutionChangeHandler(Runnable resolutionChangeHandler) {
		this.resolutionChangeHandler = resolutionChangeHandler;
	}

	public void onLanguageChange() {
		if (languageChangeHandler != null)
			languageChangeHandler.accept(LanguageManager.get(languagePath));
	}

	public void onResolutionChange() {
		if (resolutionChangeHandler != null)
			resolutionChangeHandler.run();
	}
}
