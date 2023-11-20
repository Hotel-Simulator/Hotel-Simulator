package pl.agh.edu.ui.component.modal;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.ui.component.modal.employee.HireEmployeeModalWrapper;
import pl.agh.edu.ui.component.modal.event.EventWrapper;
import pl.agh.edu.ui.component.modal.options.OptionsWrapper;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;

public class ModalManager extends Stack {
	private static ModalManager instance;
	private static ModalPreferences modalPreferences;
	private final List<BaseModalWrapper> modalList = new ArrayList<>();

	private ModalManager(ModalPreferences modalPreferences) {
		super();
		this.setFillParent(true);
		ModalManager.modalPreferences = modalPreferences;
	}

	public static ModalManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException("ModalManager not initialized. Call initialize first.");
		}
		return instance;
	}

	public static ModalManager initialize(ModalPreferences modalPreferences) {
		instance = new ModalManager(modalPreferences);
		return instance;
	}

	public void showOptionModal() {
		addModal(new OptionsWrapper(modalPreferences));
	}

	public void showEventModal(EventModalData eventModalData) {
		addModal(new EventWrapper(modalPreferences, eventModalData));
	}

	public void showHireEmployeeModal(PossibleEmployee possibleEmployee, PossibleEmployeeHandler possibleEmployeeHandler,Runnable refreshAction) {
		addModal(new HireEmployeeModalWrapper(modalPreferences, possibleEmployee, possibleEmployeeHandler,refreshAction));
	}

	private void addModal(BaseModalWrapper actor) {
		actor.openModal();
		modalList.add(0, actor);
		this.add(actor);
	}

	public void closeModal() {
		BaseModalWrapper currentModal = modalList.remove(0);
		currentModal.closeModal();
	}

	public boolean isModalActive() {
		return !modalList.isEmpty();
	}

	public boolean isModalReadyToClose() {
		return modalList.size() <= 1;
	}

	public boolean isModalOpen() {
		return !this.getChildren().isEmpty();
	}

	public boolean isBlurActive() {
		return modalPreferences.blurShader.isActive();
	}

	public record ModalPreferences(InputMultiplexer inputMultiplexer, BlurShader blurShader, Stage mainStage, Stage modalStage) {
		public static ModalPreferences of(
				InputMultiplexer inputMultiplexer,
				BlurShader blurShader,
				Stage mainStage,
				Stage modalStage) {
			return new ModalPreferences(inputMultiplexer, blurShader, mainStage, modalStage);
		}
	}
}
