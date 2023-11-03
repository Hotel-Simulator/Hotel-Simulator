package pl.agh.edu.ui.component.selectMenu;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.PIP_1;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE1;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class SelectMenu extends WrapperTable {
	private final Array<SelectMenuItem> items;
	private final SelectBox<SelectMenuItem> selectOption = new DropDownSelect();

	public SelectMenu(LanguageString languageString, Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
		super(languageString);
		this.items = items;

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		SelectMenuLabel descriptionLabel = new SelectMenuLabel();
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

	private void changeResolutionHandler() {
		this.size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		this.validate();
	}

	private static class SelectMenuLabel extends CustomLabel {
		public SelectMenuLabel() {
			super(SUBTITLE1.getName());
			this.setBackground("label-select-box-background");
			this.setAlignment(center, center);
		}

		@Override
		public void validate() {
			setHeight(SelectMenuStyle.getHeight());
			this.layout();
		}

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

	private class DropDownSelect extends SelectBox<SelectMenuItem> {
		public DropDownSelect() {
			super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
			setUpSelectionPane();
			this.getList().setAlignment(center);
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
			return font.draw(batch, string, this.getX(), this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), this.getWidth(), center, false, "...");
		}

		@Override
		protected void onShow(Actor scrollPane, boolean below) {
			super.onShow(scrollPane, below);
			PIP_1.playAudio();
		}

		@Override
		protected void onHide(Actor scrollPane) {
			super.onHide(scrollPane);
			PIP_1.playAudio();
		}
	}
}
