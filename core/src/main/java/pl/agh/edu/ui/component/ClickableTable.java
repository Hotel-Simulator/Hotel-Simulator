package pl.agh.edu.ui.component;

import static com.badlogic.gdx.graphics.Cursor.SystemCursor.Arrow;
import static com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

public abstract class ClickableTable extends WrapperContainer<Button> {
	protected Button button = new Button(skin, "clickable-table");
	private ButtonGroup<Button> buttonGroup;

	public ClickableTable() {
		super();
		setResolutionChangeHandler(this::changeSize);
		onResolutionChange();
		button.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int butt) {
				if (!button.isChecked()) {
					if (buttonGroup != null)
						for (Button button : buttonGroup.getButtons()) {
							button.setDisabled(false);
						}
					SoundAudio.CLICK.playSound();
					return true;
				}
				return false;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int butt) {
				if (button.isChecked()) {
					selectAction();
					button.setDisabled(true);
				}
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				if (pointer == -1 && !button.isChecked()) {
					Gdx.graphics.setSystemCursor(Hand);
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (pointer == -1) {
					Gdx.graphics.setSystemCursor(Arrow);
				}
				if (button.isChecked())
					Gdx.graphics.setSystemCursor(Arrow);
			}
		});
		this.setActor(button);
	}

	protected abstract void changeSize();

	protected abstract void selectAction();

	public void setBottonGroup(ButtonGroup<Button> buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

}
