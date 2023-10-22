package pl.agh.edu.actor.component.modal.options;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.actor.component.modal.BaseModalWrapper;
import pl.agh.edu.actor.shader.BlurShader;

public class OptionsWrapper extends BaseModalWrapper{
	private final OptionModal optionModal = new OptionModal(this::closeModal);

	public OptionsWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage optionsStage
	) {
		super(inputMultiplexer, blurShader, mainStage, optionsStage);
		this.setFillParent(true);
	}
	public Runnable getOptionHandler() {
		return () -> {
			if (isModalOpen())
				closeModal();
			else
				openModal();
		};
	}

	@Override
	public void openModal() {
		if (isModalOpen()) return;
		if(!isStageActive())activatedStage();
		this.setActor(optionModal);
		optionModal.runVerticalFadeInAnimation();
	}

	@Override
	public void closeModal() {
		if (!isModalOpen()) return;
		if(isStageReadyToClose()) deactivatedStage();
		optionModal.runVerticalFadeOutAnimation();
	}
}
