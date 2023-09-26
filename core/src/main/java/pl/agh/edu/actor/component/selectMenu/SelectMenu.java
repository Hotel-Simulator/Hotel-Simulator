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
import pl.agh.edu.audio.SoundAudio;
import pl.agh.edu.config.GraphicConfig;

public class SelectMenu extends Table {

	private final Skin skin = HotelSkin.getInstance();
	private final Label descriptionLabel = new SelectMenuLabel();
	private final Array<SelectMenuItem> items;
	private final SelectBox<SelectMenuItem> selectOption = new DropDownSelect();

	public SelectMenu(String description, Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
		this.items = items;
		this.descriptionLabel.setText(description);

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		this.selectOption.setAlignment(Align.right);
		this.add(descriptionLabel).size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		this.add(selectOption).size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
	}

	public void setItem(String text) {
		for (SelectMenuItem selectMenuItem : this.items) {
			if (selectMenuItem.text.equals(text)) {
				this.selectOption.setSelected(selectMenuItem);
				break;
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

	@Override
	public void layout() {
		super.layout();
		descriptionLabel.setSize(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		selectOption.setSize(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		this.getCells().forEach(cell -> cell.size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight()));
	}

	private class SelectMenuLabel extends Label {
		public SelectMenuLabel() {
			super("Test", skin.get("selectMenu", Label.LabelStyle.class));
			this.setAlignment(Align.center);
		}

	}

	private class DropDownSelect extends SelectBox<SelectMenuItem> {
		public DropDownSelect() {
			super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
			this.setSize(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
			this.getList().setAlignment(Align.center);
			setUpSelectionPane();
		}

		private void setUpSelectionPane() {
			List.ListStyle listStyle = this.getList().getStyle();

			listStyle.background.setRightWidth(0f);
			listStyle.background.setBottomHeight(0f);
			listStyle.background.setLeftWidth(0f);
			listStyle.background.setTopHeight(0f);

			listStyle.selection.setBottomHeight(SelectMenuStyle.getPadding());
			listStyle.selection.setLeftWidth(SelectMenuStyle.getPadding());
			listStyle.selection.setTopHeight(SelectMenuStyle.getPadding());
			listStyle.selection.setRightWidth(SelectMenuStyle.getPadding());
		}

		@Override
		protected GlyphLayout drawItem(Batch batch, BitmapFont font, SelectMenuItem item, float x, float y, float width) {
			String string = this.getSelected().toString();
			return font.draw(batch, string, x, this.getY() + (this.getHeight() + font.getXHeight()) / 2, 0, string.length(), width, Align.center, false, "...");
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

	private static class SelectMenuStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 40f;
				case MEDIUM -> 50f;
				case LARGE -> 60f;
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 250f + 2*getPadding();
				case MEDIUM -> 300f + 2*getPadding();
				case LARGE -> 400f + 2*getPadding();
			};
		}

		public static float getPadding(){
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 20f;
				case MEDIUM -> 20f;
				case LARGE -> 20f;
			};
		}
	}
}
