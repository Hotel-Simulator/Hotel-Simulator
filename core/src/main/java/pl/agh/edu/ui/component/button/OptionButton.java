package pl.agh.edu.ui.component.button;

import static pl.agh.edu.ui.audio.SoundAudio.POP;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.utils.GameSkinProvider;

public class OptionButton extends Button implements GameSkinProvider {
	private final ModalManager modalManager = ModalManager.getInstance();

	public OptionButton(Skin skin, String styleName) {
		super(skin, styleName);
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				POP.playSound();
				if (modalManager.isModalActive())
					modalManager.closeModal();
				else
					modalManager.showOptionModal();
			}
		});
	}
}
