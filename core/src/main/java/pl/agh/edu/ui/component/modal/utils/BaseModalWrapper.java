package pl.agh.edu.ui.component.modal.utils;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.shader.BlurShader;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;

public abstract class BaseModalWrapper extends WrapperContainer<WrapperTable> {
	protected final ModalPreferences modalPreferences;

	public BaseModalWrapper(
			ModalManager.ModalPreferences modalPreferences) {
		super(new LanguageString());
		this.modalPreferences = modalPreferences;
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

	protected boolean isBlurActive() {
		return ModalManager.getInstance().isBlurActive();
	}

	public abstract void openModal();

	public abstract void closeModal();
}
