package pl.agh.edu.ui.component.modal.employee;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;

public class ManageEmployeeModalWrapper extends BaseModalWrapper {

	private final HiredEmployee hiredEmployee;
	private final HiredEmployeeHandler hiredEmployeeHandler;
	private final EmployeeSalaryHandler employeeSalaryHandler;
	private final Runnable refreshAction;

	public ManageEmployeeModalWrapper(
			ModalPreferences modalPreferences,
			HiredEmployee hiredEmployee,
			HiredEmployeeHandler hiredEmployeeHandler,
			EmployeeSalaryHandler employeeSalaryHandler,
			Runnable refreshAction) {
		super(modalPreferences);
		this.hiredEmployee = hiredEmployee;
		this.hiredEmployeeHandler = hiredEmployeeHandler;
		this.employeeSalaryHandler = employeeSalaryHandler;
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
		ManageEmployeeModal hireEmployeeModal = new ManageEmployeeModal(hiredEmployee, hiredEmployeeHandler, employeeSalaryHandler, refreshAction);
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
