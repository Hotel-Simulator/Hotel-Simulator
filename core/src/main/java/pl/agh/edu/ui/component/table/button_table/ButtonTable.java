package pl.agh.edu.ui.component.table.button_table;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import java.util.List;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.audio.SoundAudio;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.utils.wrapper.WrapperContainer;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

public abstract class ButtonTable<T> extends WrapperContainer<ScrollPane> {

	private final HorizontalGroup contentRows = new HorizontalGroup();
	private final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
	private final List<T> dataList;
    public ButtonTable(List<T> dataList) {
		super();
		this.dataList = dataList;
        this.setActor(new CustomScrollPane(contentRows, getGameSkin(), "transparent"));
		setUpButtonAlignGroup();
		setUpButtonGroup();
		setUpTable();

		this.setResolutionChangeHandler(this::resize);
	}

	private void setUpTable() {
		dataList.forEach(data -> {
			Button button = createButton(data, createEmptyButton(data));
			buttonGroup.add(button);
			contentRows.addActor(button);
		});
	}

	private void resize(){
		contentRows.clearChildren();
		buttonGroup.clear();
		setUpTable();
		contentRows.space(ButtonTableStyle.getSpace());
		contentRows.pad(ButtonTableStyle.getPad());
		contentRows.wrapSpace(ButtonTableStyle.getWrapSpace());
	}

	private void setUpButtonAlignGroup() {
		contentRows.align(center);
		contentRows.rowAlign(left);
		contentRows.wrap(true);
		contentRows.grow();
		contentRows.space(ButtonTableStyle.getSpace());
		contentRows.pad(ButtonTableStyle.getPad());
		contentRows.wrapSpace(ButtonTableStyle.getWrapSpace());
	}

	private void setUpButtonGroup() {
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(1);
		buttonGroup.setUncheckLast(true);
	}
	public abstract Button createButton(T data, Button emptyButton);

	public abstract void hadnleData(T data);

	private Button createEmptyButton(T data) {
		Button emptyButton = new Button(getGameSkin(), "clickable-table");
		emptyButton.pad(ButtonTableStyle.getButtonSpace());
		emptyButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int butt) {
                return !emptyButton.isChecked();
            }

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int butt) {
				CLICK.playSound();
				hadnleData(T);
			}

		});

		return emptyButton;
	}

	private static class ButtonTableStyle {
		private static float getWrapSpace() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 20f;
				case MEDIUM -> 30f;
				case LARGE -> 50f;
			};
		}
		private static float getSpace() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 20f;
				case MEDIUM -> 30f;
				case LARGE -> 50f;
			};
		}

		private static float getButtonSpace() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 40f;
				case MEDIUM -> 50f;
				case LARGE -> 70f;
			};
		}

		private static float getPad() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 50f;
				case LARGE -> 70f;
			};
		}
	}
}
