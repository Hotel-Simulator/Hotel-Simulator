package pl.agh.edu.ui.component;

import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public abstract class ClickableTable extends WrapperTable {
	public ClickableTable() {
		super();
		innerTable.setTouchable(enabled);
		setBackground("clickable-table-background");
		setResolutionChangeHandler(this::changeSize);
		onResolutionChange();
		innerTable.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setBackground("clickable-table-background-down");
				SoundAudio.CLICK.playSound();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SoundAudio.CLICK.playSound();
				setBackground("clickable-table-background-over");
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				if (pointer == -1)
					setBackground("clickable-table-background-over");
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (pointer == -1) {
					if (selectedCondition()) {
						setBackground("clickable-table-background-selected");
					} else {
						setBackground("clickable-table-background");
					}
				}
			}
		});

	}

	@Override
	public void layout() {
		super.layout();
		if (selectedCondition()) {
			setBackground("clickable-table-background-selected");
		}
	}

	private void changeSize() {
		size(ClickableTableStyle.getWidth(), ClickableTableStyle.getHeight());
	}

	protected abstract boolean selectedCondition();

	public abstract static class ClickableTableStyle {
		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 600f;
				case MEDIUM -> 650f;
				case LARGE -> 700f;
			};
		}

		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 300f;
				case MEDIUM -> 350f;
				case LARGE -> 400f;
			};
		}
	}
}
