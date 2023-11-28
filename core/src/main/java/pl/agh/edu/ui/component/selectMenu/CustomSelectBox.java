package pl.agh.edu.ui.component.selectMenu;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;

import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.Null;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class CustomSelectBox extends WrapperTable {
	private final Array<SelectMenuItem> items;
	private final SelectBox<SelectMenuItem> selectOption = new DropDownSelect();
	private boolean isOpen = false;
	private boolean cursorOver = false;

	public CustomSelectBox(Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
		super();
		this.items = items;

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		innerTable.add(selectOption).pad(0f).growX().uniform().minHeight(0f);

		this.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				cursorOver = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
				cursorOver = false;
			}
		});

		this.setResolutionChangeHandler(this::changeResolutionHandler);
		this.changeResolutionHandler();
	}

	public void setItem(String name) {
		for (SelectMenuItem selectMenuItem : items) {
			if (selectMenuItem.name.equals(name)) {
				this.selectOption.setSelected(selectMenuItem);
				return;
			}
		}
	}

	private void setMaxListCount() {
		switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> selectOption.setMaxListCount(3);
			case MEDIUM -> selectOption.setMaxListCount(5);
			case LARGE -> selectOption.setMaxListCount(7);
		}
	}

	public void setListItems(Array<SelectMenuItem> items) {
		selectOption.setItems(items);
	}

	public void setFunction(Function<? super SelectMenuItem, Void> function) {
		selectOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				function.apply(items.get(selectOption.getSelectedIndex()));
			}
		});
	}

	private void changeResolutionHandler() {
		this.size(CustomSelectBox.SelectMenuStyle.getWidth(), CustomSelectBox.SelectMenuStyle.getHeight());
		this.validate();
		selectOption.setStyle(SelectMenuStyle.getSelectBoxStyle(skin));
	}

	private static class SelectMenuStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f + 2 * getPadding();
				case MEDIUM -> 35f + 2 * getPadding();
				case LARGE -> 40f + 2 * getPadding();
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 150f;
				case MEDIUM -> 200f;
				case LARGE -> 300f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}

		public static SelectBox.SelectBoxStyle getSelectBoxStyle(Skin skin){
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> skin.get("custom-select-box-small", SelectBox.SelectBoxStyle.class);
				case MEDIUM -> skin.get("custom-select-box-medium", SelectBox.SelectBoxStyle.class);
				case LARGE -> skin.get("custom-select-box-large", SelectBox.SelectBoxStyle.class);
			};
		}
	}

	private class DropDownSelect extends SelectBox<SelectMenuItem> {

		Drawable arrowUpImage = skin.getDrawable("select-box-arrow-up");
		Drawable arrowOpenImage = skin.getDrawable("select-box-arrow-open");

		public DropDownSelect() {
			super(SelectMenuStyle.getSelectBoxStyle(skin));
			setUpSelectionPane();
			this.getList().setAlignment(center);
		}

		@Override
		protected @Null Drawable getBackgroundDrawable() {
			if (isOpen && cursorOver) {
				return skin.getDrawable("full-select-box-background-open-over");
			}
			if (isOpen) {
				return skin.getDrawable("full-select-box-background-open");
			}
			if (cursorOver) {
				return skin.getDrawable("full-select-box-background-over");
			}
			return skin.getDrawable("full-select-box-background");
		}

		private void setUpSelectionPane() {
			List.ListStyle listStyle = this.getList().getStyle();

			listStyle.selection.setBottomHeight(CustomSelectBox.SelectMenuStyle.getPadding());
			listStyle.selection.setTopHeight(CustomSelectBox.SelectMenuStyle.getPadding());

			this.getList().setStyle(listStyle);
		}

		@Override
		public void showScrollPane() {
			super.showScrollPane();
			isOpen = true;
		}

		@Override
		public void hideScrollPane() {
			super.hideScrollPane();
			isOpen = false;
		}

		@Override
		protected GlyphLayout drawItem(Batch batch, BitmapFont font, SelectMenuItem item, float x, float y, float width) {
			String string = this.getSelected().toString();
			getImage().draw(batch, this.getX() + this.getWidth() - 20f - 20f, this.getY() + this.getHeight() / 2 - 10f, 20f, 20f);
			return font.draw(batch, string, this.getX(), this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), this.getWidth(), center, false, "...");
		}

		private Drawable getImage() {
			return isOpen ? arrowOpenImage : arrowUpImage;
		}

		@Override
		protected void onShow(Actor scrollPane, boolean below) {
			super.onShow(scrollPane, below);
			CLICK.playSound();
		}

		@Override
		protected void onHide(Actor scrollPane) {
			super.onHide(scrollPane);
			CLICK.playSound();
		}
	}
}
