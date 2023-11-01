package pl.agh.edu.ui.component.selection;

import static com.badlogic.gdx.utils.Align.center;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public abstract class BaseSelection<T> extends WrapperTable {
	protected final Skin skin = GameSkin.getInstance();
	private final Button leftButton = new Button(skin, "selection-left");
	private final Button rightButton = new Button(skin, "selection-right");
	private T value;
	private final Label label;

	public BaseSelection(T value, Label label, Consumer<T> action) {
		this.value = value;
		this.label = label;
		leftButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return !leftButton.isDisabled();
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				rightButton.setDisabled(false);
				previousButtonHandler();
				if (!isPreviousButtonCheck()) {
					leftButton.setDisabled(true);
				}
				updateLabel(label);
				action.accept(getValue());
			}
		});

		rightButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return !rightButton.isDisabled();
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.BUTTON_3.play();
				leftButton.setDisabled(false);
				nextButtonHandler();
				if (!isNextButtonCheck()) {
					rightButton.setDisabled(true);
				}
				updateLabel(label);
				action.accept(getValue());
			}
		});
		label.setAlignment(center, center);
		updateLabel(label);

		innerTable.setFillParent(false);

		innerTable.add().uniform();
		innerTable.add(leftButton).uniform();
		innerTable.add(label).grow();
		innerTable.add(rightButton).uniform();
		innerTable.add().uniform().row();
	}

	public void changeStyleToBig() {
		leftButton.setStyle(skin.get("selection-left-big", Button.ButtonStyle.class));
		rightButton.setStyle(skin.get("selection-right-big", Button.ButtonStyle.class));
	}

	protected abstract boolean isNextButtonCheck();

	protected abstract boolean isPreviousButtonCheck();

	protected abstract void nextButtonHandler();

	protected abstract void previousButtonHandler();

	protected abstract void updateLabel(Label label);

	public void updateLabel() {
		updateLabel(label);
	}

	public T getValue() {
		return value;
	}

	protected void setValue(T value) {
		this.value = value;
	}

	protected void checkButtons() {
		rightButton.setDisabled(!isNextButtonCheck());
		leftButton.setDisabled(!isPreviousButtonCheck());
	}

}
