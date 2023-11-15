package pl.agh.edu.ui.component.modal.event;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.modal.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;

public class EventWrapper extends BaseModalWrapper {

	private final EventModalData eventModalData;
	public EventWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage modalStage,
			EventModalData eventModalData
			) {
		super(inputMultiplexer, blurShader, mainStage, modalStage);
		this.eventModalData = eventModalData;
		this.setResolutionChangeHandler(this::resize);
		this.resize();
	}

	@Override
	public void openModal() {
		if (isModalOpen())
			return;
		if (!isStageActive()) {
			Time.getInstance().stop();
			inputMultiplexer.setProcessors(modalStage);
			blurShader.startBlur();
		}
		EventModal optionModal = new EventModal(eventModalData);
		this.setActor(optionModal);
		optionModal.runVerticalFadeInAnimation();
	}
	@Override
	public void closeModal() {
		if (!isModalOpen())
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
