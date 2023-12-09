package pl.agh.edu.ui.component.background;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.resolution.ResolutionChangeListener;
import pl.agh.edu.ui.resolution.ResolutionManager;
import pl.agh.edu.ui.utils.GameSkinProvider;

public class InfinityBackground extends Stack implements ResolutionChangeListener, GameSkinProvider {
	private final Image firstBackground;
	private final Image secondBackground;

	public InfinityBackground(String fileName) {
		this(fileName, false);
	}

	public InfinityBackground(String fileName, boolean withStart) {
		this.setFillParent(true);
		this.firstBackground = createImage(fileName);
		this.secondBackground = createImage(fileName);
		Time.getInstance().addTimeStopChangeHandler(this::stopAnimation);
		Time.getInstance().addTimeStartChangeHandler(this::startAnimation);
		this.restartAnimation();
		ResolutionManager.addListener(this);
		if (withStart)
			startAnimation();
	}

	private void setAnimation(final Image image) {
		image.addAction(Actions.forever(Actions.run(() -> moveImage(image))));
	}

	private void moveImage(Image image) {
		float movingDistance = getMovingDistance();
		if (image.getX() + GraphicConfig.getResolution().WIDTH <= 0) {
			image.moveBy(-movingDistance + 2 * GraphicConfig.getResolution().WIDTH, 0f);
		}
		image.moveBy(-movingDistance, 0f);
	}

	protected float getMovingDistance() {
		return 0.5f;
	}

	private Image createImage(String fileName) {
		Image image = new Image(getGameSkin().getDrawable(fileName));
		image.setFillParent(true);
		image.setScaling(Scaling.stretch);
		this.add(image);
		return image;
	}

	public void stopAnimation() {
		firstBackground.clearActions();
		secondBackground.clearActions();
	}

	public void startAnimation() {
		this.setAnimation(firstBackground);
		this.setAnimation(secondBackground);
	}

	private void restartAnimation() {
		firstBackground.setPosition(0f, 0f);
		secondBackground.setPosition(0f, 0f);
		secondBackground.addAction(Actions.moveBy(GraphicConfig.getResolution().WIDTH, 0f));
	}

	@Override
	public Actor onResolutionChange() {
		this.stopAnimation();
		this.restartAnimation();
		return this;
	}
}
