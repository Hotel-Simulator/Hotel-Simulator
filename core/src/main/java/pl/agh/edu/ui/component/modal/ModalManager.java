package pl.agh.edu.ui.component.modal;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;
import pl.agh.edu.engine.employee.possible.PossibleEmployeeHandler;
import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.ui.component.modal.employee.HireEmployeeModalWrapper;
import pl.agh.edu.ui.component.modal.employee.ManageEmployeeModalWrapper;
import pl.agh.edu.ui.component.modal.event.EventWrapper;
import pl.agh.edu.ui.component.modal.options.OptionsWrapper;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;
import pl.agh.edu.ui.utils.ShadowBackground;

public class ModalManager extends Stack {
	private static ModalManager instance;
	private static ModalPreferences modalPreferences;
	private static final List<BaseModalWrapper> modalList = new ArrayList<>();
	private static final List<ShadowBackground> backgroundList = new ArrayList<>();

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

	public void showHireEmployeeModal(Employee possibleEmployee, EmployeeHandler possibleEmployeeHandler, Runnable refreshAction) {
		addModal(new HireEmployeeModalWrapper<>(modalPreferences, possibleEmployee, possibleEmployeeHandler, refreshAction));
	}

	public void showManageEmployeeModal(HiredEmployee hiredEmployee, HiredEmployeeHandler hiredEmployeeHandler, EmployeeSalaryHandler employeeSalaryHandler, Runnable refreshAction) {
		addModal(new ManageEmployeeModalWrapper(modalPreferences, hiredEmployee, hiredEmployeeHandler,employeeSalaryHandler, refreshAction));
	}

	private void addModal(BaseModalWrapper modal) {
		modal.openModal();
		modalList.add(0, modal);
		ShadowBackground shadowBackground = new ShadowBackground(modal, this::closeModal);
		backgroundList.add(0, shadowBackground);
		this.add(shadowBackground);
		this.add(modal);
	}

	public void closeModal() {
		BaseModalWrapper currentModal = modalList.remove(0);
		ShadowBackground currentBackground = backgroundList.remove(0);
		currentModal.closeModal();
		currentBackground.remove();
	}

	public boolean isModalActive() {
		return !modalList.isEmpty();
	}

	public boolean isModalReadyToClose() {
		return modalList.isEmpty();
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
