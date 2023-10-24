package pl.agh.edu.ui.utils.wrapper;

import static com.badlogic.gdx.math.Interpolation.fade;
import static com.badlogic.gdx.math.Interpolation.smooth;

import java.util.function.Consumer;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;

public abstract class WrapperContainer<T extends Actor> extends Container<T> implements LanguageChangeListener, ResolutionChangeListener {
	private final String languagePath;
	private Consumer<String> languageChangeHandler;
	private Runnable resolutionChangeHandler;
	private Runnable returnHandler;

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
		this.setResetAnimationPosition();
		this.resetAnimationPosition();
	}

	public void initChangeHandlers() {
		onLanguageChange();
		onResolutionChange();
	}

	public void setResetAnimationPosition(float x, float y) {
		returnHandler = () -> this.setPosition(x, y);
	}

	public void setResetAnimationPosition() {
		this.setResetAnimationPosition(this.getX(), this.getY());
	}

	public void resetAnimationPosition() {
		if (returnHandler != null)
			returnHandler.run();
	}

	private void runFadeInAnimation(float distanceX, float distanceY, float duration, Interpolation interpolation) {
		this.clearActions();
		this.addAction(
				Actions.sequence(
						Actions.parallel(
								Actions.alpha(0f),
								Actions.moveBy(distanceX, distanceY)),
						Actions.parallel(
								Actions.moveBy(-distanceX, -distanceY, duration, interpolation),
								Actions.alpha(1f, duration))));
		this.resetAnimationPosition();
	}

	private void runFadeOutAnimation(float distanceX, float distanceY, float duration, Interpolation interpolation) {
		this.clearActions();
		this.addAction(
				Actions.sequence(
						Actions.alpha(1f),
						Actions.parallel(
								Actions.alpha(0f, duration),
								Actions.moveBy(-distanceX, -distanceY, duration, interpolation)),
						Actions.moveBy(distanceX, distanceY),
						Actions.removeActor(this)));
		this.resetAnimationPosition();
	}

	public void runHorizontalFadeInAnimation() {
		this.runFadeInAnimation(50f, 0f, 0.5f, fade);
	}

	public void runHorizontalFadeOutAnimation() {
		this.runFadeOutAnimation(50f, 0f, 0.2f, fade);
	}

	public void runVerticalFadeInAnimation() {
		this.runFadeInAnimation(0f, 50f, 0.5f, fade);
	}

	public void runVerticalFadeOutAnimation() {
		this.runFadeOutAnimation(0f, 50f, 0.5f, fade);
	}

	public void runVerticalTrainInAnimation() {
		this.runFadeInAnimation(0f, GraphicConfig.getResolution().HEIGHT, 0.5f, fade);
	}

	public void runVerticalTrainOutAnimation() {
		this.runFadeOutAnimation(0f, GraphicConfig.getResolution().HEIGHT, 0.5f, fade);
	}

	public void runHorizontalTrainInAnimation() {
		this.runFadeInAnimation(GraphicConfig.getResolution().WIDTH, 0f, 0.7f, smooth);
	}

	public void runHorizontalTrainOutAnimation() {
		this.runFadeOutAnimation(GraphicConfig.getResolution().WIDTH, 0f, 0.7f, smooth);
	}
}
