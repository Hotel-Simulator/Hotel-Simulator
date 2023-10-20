package pl.agh.edu.actor.component.background;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Scaling;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.resolution.ResolutionChangeListener;
import pl.agh.edu.actor.utils.resolution.ResolutionManager;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.model.time.Time;

public class InfinityBackground extends Stack implements ResolutionChangeListener {
	private final Image firstBackground;
	private final Image secondBackground;

	public InfinityBackground(String fileName) {
		this.setFillParent(true);
		firstBackground = createImage(fileName);
		secondBackground = createImage(fileName);
		Time.getInstance().addTimeStopChangeHandler(this::stopAnimation);
		Time.getInstance().addTimeStartChangeHandler(this::startAnimation);
		this.restartAnimation();
		ResolutionManager.addListener(this);
	}

	private void setAnimation(final Image image) {
		image.addAction(
				Actions.sequence(
						Actions.forever(
								Actions.sequence(
										Actions.run(() -> {
											float movingDistance = 10.2f;
											if (image.getX() + GraphicConfig.getResolution().WIDTH <= 0) {
												image.moveBy(-movingDistance + 2 * GraphicConfig.getResolution().WIDTH, 0f);
											}
											image.moveBy(-movingDistance, 0f);
										})))));
	}

	private Image createImage(String fileName) {
		Image image = new Image(GameSkin.getInstance().getDrawable(fileName));
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
	public void onResolutionChange() {
		this.stopAnimation();
		this.restartAnimation();
	}
}
