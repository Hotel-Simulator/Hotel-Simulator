package pl.agh.edu.ui.screen.main;

import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.GRAVE;

import com.badlogic.gdx.InputMultiplexer;

import pl.agh.edu.engine.event.EventModalData;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.utils.LanguageString;

public class MainScreenInputAdapter extends InputMultiplexer {
	public MainScreenInputAdapter() {
		super();
	}

	@Override
	public boolean keyDown(int keycode) {
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		ModalManager modalManager = ModalManager.getInstance();
		if (keycode == ESCAPE) {
			if (modalManager.isModalActive())
				modalManager.closeModal();
			else
				modalManager.showOptionModal();
			return true;
		}
		if (keycode == GRAVE) {
			modalManager.showEventModal(
					new EventModalData(
							new LanguageString("test.test"),
							new LanguageString("test.test"),
							"event-icon-fair"));
			return true;
		}
		return false;
	}
}
