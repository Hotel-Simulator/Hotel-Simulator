package pl.agh.edu.actor.component.selectMenu;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.label.CustomLabel;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.wrapper.WrapperTable;
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.GraphicConfig;

public class SelectMenu extends WrapperTable {
	private final Skin skin = GameSkin.getInstance();
	private final SelectMenuLabel descriptionLabel = new SelectMenuLabel();
	private final Array<SelectMenuItem> items;
	private final SelectBox<SelectMenuItem> selectOption = new DropDownSelect();

	public SelectMenu(String languagePath, Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
		super(languagePath);
		this.items = items;

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		innerTable.add(descriptionLabel).pad(0f).growX().uniform().minHeight(0f);
		innerTable.add(selectOption).pad(0f).growX().uniform().minHeight(0f);

		this.setResolutionChangeHandler(this::changeResolutionHandler);
		this.setLanguageChangeHandler(descriptionLabel::setText);
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

	private void setListItems(Array<SelectMenuItem> items) {
		selectOption.setItems(items);
	}

	private void setFunction(Function<? super SelectMenuItem, Void> function) {
		selectOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				function.apply(items.get(selectOption.getSelectedIndex()));
			}
		});
	}

	private static class SelectMenuLabel extends CustomLabel {
		public SelectMenuLabel() {
			super(FontType.SUBTITLE1.getName(), "label-select-box-background");
			this.setAlignment(Align.center, Align.center);
		}

		@Override
		public void validate() {
			setHeight(SelectMenuStyle.getHeight());
			this.layout();
		}

	}

	private class DropDownSelect extends SelectBox<SelectMenuItem> {
		public DropDownSelect() {
			super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
			setUpSelectionPane();
			this.getList().setAlignment(Align.center);
		}

		private void setUpSelectionPane() {
			List.ListStyle listStyle = this.getList().getStyle();

			listStyle.selection.setBottomHeight(SelectMenuStyle.getPadding());
			listStyle.selection.setTopHeight(SelectMenuStyle.getPadding());

			this.getList().setStyle(listStyle);
		}

		@Override
		public void validate() {
			setHeight(SelectMenuStyle.getHeight());
			this.layout();
		}

		@Override
		protected GlyphLayout drawItem(Batch batch, BitmapFont font, SelectMenuItem item, float x, float y, float width) {
			String string = this.getSelected().toString();
			return font.draw(batch, string, this.getX(), this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), this.getWidth(), Align.center, false, "...");
		}

		@Override
		protected void onShow(Actor scrollPane, boolean below) {
			super.onShow(scrollPane, below);
			SoundAudio.PIP_1.play();
		}

		@Override
		protected void onHide(Actor scrollPane) {
			super.onHide(scrollPane);
			SoundAudio.PIP_1.play();
		}
	}

	private void changeResolutionHandler() {
		this.size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		this.validate();
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
				case SMALL -> 650f;
				case MEDIUM -> 750f;
				case LARGE -> 900f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}
	}
}
