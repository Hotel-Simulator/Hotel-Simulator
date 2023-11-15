package pl.agh.edu.ui.component.modal.options;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.modal.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;

public class OptionsWrapper extends BaseModalWrapper {
	private final OptionModal optionModal = new OptionModal();
	public OptionsWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage modalStage) {
		super(inputMultiplexer, blurShader, mainStage, modalStage);
		this.resize();
		this.setResolutionChangeHandler(this::resize);
	}
	@Override
	public void openModal() {
		if (isModalOpen())
			return;
		if (!isStageActive())
			activatedStage();
		this.setActor(optionModal);
		optionModal.runVerticalFadeInAnimation();
	}

	@Override
	public void closeModal() {
		if (!isModalOpen())
			return;
		if (isStageReadyToClose())
			deactivatedStage();
		optionModal.runVerticalFadeOutAnimation();
	}

	public void resize() {
		this.size(OptionWrapperStyle.getWidth(), OptionWrapperStyle.getHeight());
		optionModal.validate();
		optionModal.layout();
		this.resetAnimationPosition();
	}

	private static class OptionWrapperStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 600f;
				case MEDIUM -> 700f;
				case LARGE -> 800f;
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 700f;
				case MEDIUM -> 800f;
				case LARGE -> 1000f;
			};
		}
	}

}
