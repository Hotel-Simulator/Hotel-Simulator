package pl.agh.edu.actor.component.modal.event;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import pl.agh.edu.actor.shader.BlurShader;
import pl.agh.edu.actor.utils.wrapper.WrapperContainer;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.model.event.EventModalData;
import pl.agh.edu.model.time.Time;

public class EventWrapper extends WrapperContainer<EventModal> {

	private final InputMultiplexer inputMultiplexer;
	private final BlurShader blurShader;
	private final Stage mainStage;
	private final Stage eventStage;

	public EventWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage eventStage) {
		super();
		this.inputMultiplexer = inputMultiplexer;
		this.blurShader = blurShader;
		this.mainStage = mainStage;
		this.eventStage = eventStage;
		this.resize();
		this.setResolutionChangeHandler(this::resize);
		this.setFillParent(true);
	}

	public boolean isEventOpen() {
		return this.getActor() != null;
	}

	public int countContainersWithActors() {
		int count = 0;
		for (Actor actor : eventStage.getActors()) {
			if (actor instanceof Container container && container.getActor() != null) {
				count++;
			}
		}
		return count;
	}

	private boolean isStageActive() {
		return countContainersWithActors() > 0;
	}

	private boolean isStageReadyToClose() {
		return countContainersWithActors() <= 1;
	}

	public void showEvent(EventModalData eventModalData) {
		if (isEventOpen())
			return;
		if (!isStageActive()) {
			Time.getInstance().stop();
			inputMultiplexer.setProcessors(eventStage);
			blurShader.startBlur();
		}
		EventModal optionModal = new EventModal(eventModalData, this::closeOptions);
		this.setActor(optionModal);
		optionModal.runVerticalFadeInAnimation();
	}

	private void closeOptions() {
		if (!isEventOpen())
			return;
		if (isStageReadyToClose()) {
			inputMultiplexer.setProcessors(mainStage);
			blurShader.stopBlur();

		}
		this.getActor().runVerticalFadeOutAnimation();
	}

	public void resize() {
		this.size(EventWrapperStyle.getWidth(), EventWrapperStyle.getHeight());
		this.resetAnimationPosition();
	}

	private static class EventWrapperStyle {
		public static float getHeight() {
			return (float) GraphicConfig.getResolution().HEIGHT / 9 * 4;
		}

		public static float getWidth() {
			return (float) GraphicConfig.getResolution().WIDTH / 9 * 4;
		}
	}
}
