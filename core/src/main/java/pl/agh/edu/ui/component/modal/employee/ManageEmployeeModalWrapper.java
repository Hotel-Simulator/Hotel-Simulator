package pl.agh.edu.ui.component.modal.employee;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;

public class ManageEmployeeModalWrapper extends BaseModalWrapper {

	private final Employee employee;
	private final EmployeeHandler employeeHandler;
	private final Runnable refreshAction;

	public ManageEmployeeModalWrapper(
			ModalPreferences modalPreferences,
			Employee employee,
			EmployeeHandler employeeHandler,
			Runnable refreshAction) {
		super(modalPreferences);
		this.employee = employee;
		this.employeeHandler = employeeHandler;
		this.refreshAction = refreshAction;
		this.setResolutionChangeHandler(this::resize);
		this.resize();
	}

	@Override
	public void openModal() {
		if (isModalOpen())
			return;
		if (!isStageActive()) {
			modalPreferences.inputMultiplexer().setProcessors(modalPreferences.modalStage());
		}
		ManageEmployeeModal hireEmployeeModal = new ManageEmployeeModal(employee, employeeHandler, refreshAction);
		this.setActor(hireEmployeeModal);
		hireEmployeeModal.runVerticalFadeInAnimation();
	}

	@Override
	public void closeModal() {
		if (!isModalOpen())
			return;
		if (isStageReadyToClose()) {
			modalPreferences.inputMultiplexer().setProcessors(modalPreferences.mainStage());
		}
		this.getActor().runVerticalFadeOutAnimation();
	}

	public void resize() {
		this.size(ManageEmployeeModalWrapperStyle.getWidth(), ManageEmployeeModalWrapperStyle.getHeight());
		this.resetAnimationPosition();
	}

	private static class ManageEmployeeModalWrapperStyle {
		public static float getHeight() {
			return (float) GraphicConfig.getResolution().HEIGHT / 18 * 13;
		}

		public static float getWidth() {
			return (float) GraphicConfig.getResolution().WIDTH / 18 * 15;
		}
	}
}
