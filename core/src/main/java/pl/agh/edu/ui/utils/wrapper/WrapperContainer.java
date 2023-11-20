package pl.agh.edu.ui.utils.wrapper;

import static com.badlogic.gdx.math.Interpolation.fade;
import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.scenes.scene2d.Touchable.disabled;
import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;

import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.GdxGame;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.Engine;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.utils.LanguageString;

public abstract class WrapperContainer<T extends Actor> extends Container<T> implements LanguageChangeListener, ResolutionChangeListener {
	private LanguageString languageString;
	private Consumer<String> languageChangeHandler;
	private Runnable resolutionChangeHandler;
	private Runnable returnHandler;
	protected Skin skin = GameSkin.getInstance();
	protected Engine engine = ((GdxGame) Gdx.app.getApplicationListener()).engine;

	public WrapperContainer(LanguageString languageString) {
		this.languageString = languageString;
		this.init();
	}

	public WrapperContainer() {
		this.init();
	}

	private void init() {
		this.minHeight(0f);
		this.minWidth(0f);
		this.fill();
	}

	public void setLanguageChangeHandler(Consumer<String> languageChangeHandler) {
		this.languageChangeHandler = languageChangeHandler;
		LanguageManager.addListener(this);
	}

	public void setResolutionChangeHandler(Runnable resolutionChangeHandler) {
		this.resolutionChangeHandler = resolutionChangeHandler;
		ResolutionManager.addListener(this);
	}

	public Actor onLanguageChange() {
		if (languageChangeHandler != null)
			languageChangeHandler.accept(LanguageManager.get(languageString));
		return this;
	}

	public Actor onResolutionChange() {
		if (resolutionChangeHandler != null)
			resolutionChangeHandler.run();
		this.setResetAnimationPosition();
		this.resetAnimationPosition();
		return this;
	}

	public void updateLanguageString(LanguageString languageString) {
		this.languageString = languageString;
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
						Actions.touchable(disabled),
						Actions.parallel(
								Actions.alpha(0f),
								Actions.moveBy(distanceX, distanceY)),
						Actions.parallel(
								Actions.moveBy(-distanceX, -distanceY, duration, interpolation),
								Actions.alpha(1f, duration)),
						Actions.touchable(enabled)));
		this.resetAnimationPosition();
	}

	private void runFadeOutAnimation(float distanceX, float distanceY, float duration, Interpolation interpolation) {
		this.clearActions();
		this.addAction(
				Actions.sequence(
						Actions.touchable(disabled),
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
		this.runFadeOutAnimation(0f, 50f, 0.2f, fade);
	}

	public void runVerticalTrainInAnimation() {
		this.runFadeInAnimation(0f, GraphicConfig.getResolution().HEIGHT, 0.5f, fade);
	}

	public void runVerticalTrainOutAnimation() {
		this.runFadeOutAnimation(0f, GraphicConfig.getResolution().HEIGHT, 0.2f, fade);
	}

	public void runHorizontalTrainInAnimation() {
		this.runFadeInAnimation(GraphicConfig.getResolution().WIDTH, 0f, 0.7f, smooth);
	}

	public void runHorizontalTrainOutAnimation() {
		this.runFadeOutAnimation(GraphicConfig.getResolution().WIDTH, 0f, 0.7f, smooth);
	}
}
