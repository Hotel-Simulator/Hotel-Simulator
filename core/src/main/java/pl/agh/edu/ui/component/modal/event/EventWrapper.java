package pl.agh.edu.ui.component.modal.event;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;
public class EventWrapper extends BaseModalWrapper {

	private final EventModalData eventModalData;

	public EventWrapper(
			ModalPreferences modalPreferences,
			EventModalData eventModalData) {
		super(modalPreferences);
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
			modalPreferences.inputMultiplexer().setProcessors(modalPreferences.modalStage());
		}
		if (!isBlurActive())
			modalPreferences.blurShader().startBlur();
		EventModal blurModal = new EventModal(eventModalData);
		this.setActor(blurModal);
		blurModal.runVerticalFadeInAnimation();
	}

	@Override
	public void closeModal() {
		if (!isModalOpen())
			return;
		if (isStageReadyToClose()) {
			modalPreferences.inputMultiplexer().setProcessors(modalPreferences.mainStage());
		}
		if (isBlurActive())
			modalPreferences.blurShader().stopBlur();
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
