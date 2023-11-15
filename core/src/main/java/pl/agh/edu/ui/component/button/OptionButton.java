package pl.agh.edu.ui.component.button;

import static pl.agh.edu.ui.audio.SoundAudio.POP;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.modal.ModalManager;

public class OptionButton extends Button {
	private final ModalManager modalManager = ModalManager.getInstance();

	public OptionButton() {
		super(GameSkin.getInstance(), "options");
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				POP.playSound();
				System.out.println(modalManager.isModalActive());
				if (modalManager.isModalActive())
					modalManager.closeModal();
				else
					modalManager.setUpOptionModal();
			}
		});
	}
}
