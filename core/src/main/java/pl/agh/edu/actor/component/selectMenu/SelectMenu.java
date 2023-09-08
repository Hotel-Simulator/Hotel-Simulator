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

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.Size;

public class SelectMenu extends Table {

	private final Skin skin = HotelSkin.getInstance();
	private final Label descriptionLabel = new SelectMenuLabel();
	private final Array<SelectMenuItem> items;
	private final SelectBox<SelectMenuItem> selectOption;
	private final Size size;

	public SelectMenu(Size size, String description, Array<SelectMenuItem> items, Function<SelectMenuItem, Void> function) {
		this.items = items;
		this.descriptionLabel.setText(description);
		this.size = size;
		this.selectOption = new DropDownSelect(size);

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		this.selectOption.setAlignment(Align.right);
		this.add(descriptionLabel).size(SelectMenuStyle.getWidth(size), SelectMenuStyle.getHeight(size));
		this.add(selectOption).size(SelectMenuStyle.getWidth(size), SelectMenuStyle.getHeight(size));
	}

	private void setMaxListCount() {
		switch (this.size) {
		case SMALL -> selectOption.setMaxListCount(3);
		case MEDIUM -> selectOption.setMaxListCount(5);
		case LARGE -> selectOption.setMaxListCount(7);
		}
	}

	private void setListItems(Array<SelectMenuItem> items) {
		selectOption.setItems(items);
	}

	private void setFunction(Function<SelectMenuItem, Void> function) {
		selectOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				function.apply(items.get(selectOption.getSelectedIndex()));
			}
		});
	}

	private class SelectMenuLabel extends Label {
		public SelectMenuLabel() {
			super("Test", skin.get("selectMenu", Label.LabelStyle.class));
		}

	}

	private class DropDownSelect extends SelectBox<SelectMenuItem> {
		public DropDownSelect(Size size) {
			super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
			this.setSize(SelectMenuStyle.getWidth(size), SelectMenuStyle.getHeight(size));
			this.getList().setAlignment(Align.right);
			setUpSelectionPane();
		}

		private void setUpSelectionPane() {
			List.ListStyle listStyle = this.getList().getStyle();

			listStyle.background.setRightWidth(0f);
			listStyle.background.setBottomHeight(0f);
			listStyle.background.setLeftWidth(0f);
			listStyle.background.setTopHeight(0f);

			listStyle.selection.setBottomHeight(20f);
			listStyle.selection.setLeftWidth(20f);
			listStyle.selection.setTopHeight(20f);
			listStyle.selection.setRightWidth(20f);
		}

		@Override
		protected GlyphLayout drawItem(Batch batch, BitmapFont font, SelectMenuItem item, float x, float y, float width) {
			String string = this.getSelected().toString();
			return font.draw(batch, string, x, this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), width, Align.top, false, "...");
		}
	}

	private static class SelectMenuStyle {
		public static float getHeight(Size size) {
			switch (size) {
			case SMALL -> {
				return 50f;
			}
			case MEDIUM -> {
				return 70f;
			}
			case LARGE -> {
				return 80f;
			}
			}
			return 0;
		}

		public static float getWidth(Size size) {
			switch (size) {
			case SMALL -> {
				return 300f;
			}
			case MEDIUM -> {
				return 350f;
			}
			case LARGE -> {
				return 400f;
			}
			}
			return 0;
		}
	}
}
