package pl.agh.edu.ui.component.modal.employee;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;
public class HireEmployeeModalWrapper extends BaseModalWrapper {

	private final PossibleEmployee possibleEmployee;
	private final PossibleEmployeeHandler possibleEmployeeHandler;
	private final Runnable refreshAction;

	public HireEmployeeModalWrapper(
			ModalPreferences modalPreferences,
			PossibleEmployee possibleEmployee,
			PossibleEmployeeHandler possibleEmployeeHandler,
			Runnable refreshAction) {
		super(modalPreferences);
		this.possibleEmployee = possibleEmployee;
		this.possibleEmployeeHandler = possibleEmployeeHandler;
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
		HireEmployeeModal hireEmployeeModal = new HireEmployeeModal(possibleEmployee, possibleEmployeeHandler, refreshAction);
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
