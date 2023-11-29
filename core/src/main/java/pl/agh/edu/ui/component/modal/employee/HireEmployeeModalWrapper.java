package pl.agh.edu.ui.component.modal.employee;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;

public class HireEmployeeModalWrapper<ExtendedEmployee extends Employee> extends BaseModalWrapper {

	private final ExtendedEmployee employee;
	private final EmployeeHandler<ExtendedEmployee> employeeHandler;
	private final Runnable refreshAction;

	public HireEmployeeModalWrapper(
			ModalPreferences modalPreferences,
			ExtendedEmployee employee,
			EmployeeHandler<ExtendedEmployee> possibleEmployeeHandler,
			Runnable refreshAction) {
		super(modalPreferences);
		this.employee = employee;
		this.employeeHandler = possibleEmployeeHandler;
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
		HireEmployeeModal<ExtendedEmployee> hireEmployeeModal = new HireEmployeeModal<>(employee, employeeHandler, refreshAction);
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
		this.size(HireEmployeeModalWrapperStyle.getWidth(), HireEmployeeModalWrapperStyle.getHeight());
		this.resetAnimationPosition();
	}

	private static class HireEmployeeModalWrapperStyle {
		public static float getHeight() {
			return (float) GraphicConfig.getResolution().HEIGHT / 18 * 13;
		}

		public static float getWidth() {
			return (float) GraphicConfig.getResolution().WIDTH / 18 * 15;
		}
	}
}
