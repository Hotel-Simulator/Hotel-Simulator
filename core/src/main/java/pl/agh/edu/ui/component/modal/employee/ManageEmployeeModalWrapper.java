package pl.agh.edu.ui.component.modal.employee;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.modal.utils.BaseModalWrapper;
import pl.agh.edu.ui.shader.BlurShader;

import static pl.agh.edu.ui.component.modal.ModalManager.ModalPreferences;
public class ManageEmployeeModalWrapper extends BaseModalWrapper {
	public ManageEmployeeModalWrapper(ModalPreferences modalPreferences) {
		super(modalPreferences);
	}

	@Override
	public void openModal() {

	}

	@Override
	public void closeModal() {

	}

	public void resize() {
		this.size(HireEmployeeModalWrapperStyle.getWidth(), HireEmployeeModalWrapperStyle.getHeight());
		this.resetAnimationPosition();
	}

	private static class HireEmployeeModalWrapperStyle {
		public static float getHeight() {
			return (float) GraphicConfig.getResolution().HEIGHT / 9 * 4;
		}

		public static float getWidth() {
			return (float) GraphicConfig.getResolution().WIDTH / 9 * 4;
		}
	}
}
