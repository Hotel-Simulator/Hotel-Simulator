package pl.agh.edu.ui.component.modal;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.ui.shader.BlurShader;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public abstract class BaseModalWrapper extends WrapperContainer<WrapperTable> {
	protected final InputMultiplexer inputMultiplexer;
	protected final BlurShader blurShader;
	protected final Stage mainStage;
	protected final Stage modalStage;

	public BaseModalWrapper(
			InputMultiplexer inputMultiplexer,
			BlurShader blurShader,
			Stage mainStage,
			Stage modalStage) {
		super(new LanguageString());
		this.inputMultiplexer = inputMultiplexer;
		this.blurShader = blurShader;
		this.mainStage = mainStage;
		this.modalStage = modalStage;
		this.setFillParent(true);
	}

	public boolean isModalOpen() {
		return this.getActor() != null;
	}
	protected boolean isStageActive() {
		return ModalManager.getInstance().isModalActive();
	}

	protected boolean isStageReadyToClose() {
		return ModalManager.getInstance().isModalReadyToClose();
	}

	protected void activatedStage() {
		Time.getInstance().stop();
		inputMultiplexer.setProcessors(modalStage);
		blurShader.startBlur();
	}

	protected void deactivatedStage() {
		inputMultiplexer.setProcessors(mainStage);
		blurShader.stopBlur();
	}

	public abstract void openModal();

	public abstract void closeModal();
}
