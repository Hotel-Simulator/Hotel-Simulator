package pl.agh.edu.ui.component.selectMenu;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.audio.SoundAudio.CLICK;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE2;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE3;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.LanguageString;

public class SelectMenu extends WrapperTable {
	private final Array<SelectMenuItem> items;
	private final DropDownSelect selectOption = new DropDownSelect();
	private final SelectMenuLabel descriptionLabel;
	private boolean isOpen = false;
	private boolean cursorOver = false;

	public SelectMenu(LanguageString languageString, Array<SelectMenuItem> items, Function<? super SelectMenuItem, Void> function) {
		super();
		this.items = items;

		setMaxListCount();
		setListItems(items);
		setFunction(function);

		descriptionLabel = new SelectMenuLabel(languageString);
		innerTable.add(descriptionLabel).pad(0f).grow().uniform().minHeight(0f);
		innerTable.add(selectOption).pad(0f).grow().uniform().minHeight(0f);

		this.addListener(new InputListener(){
			@Override
			public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				descriptionLabel.setStateToOver();
				cursorOver = true;
			}

			@Override
			public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
				descriptionLabel.setStateToUp();
				cursorOver = false;
			}
		});

		this.setResolutionChangeHandler(this::changeResolutionHandler);
		this.changeResolutionHandler();
		this.onLanguageChange();

	}

	public void setUpListener() {
		this.setTouchable(Touchable.enabled);
		this.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
				if (isOpen)
					return;

			}
		});
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
		descriptionLabel.setFont(SelectMenuStyle.getFont());
		SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(selectOption.getStyle());
		selectBoxStyle.font = GameSkin.getInstance().getFont(SelectMenuStyle.getFont());
		selectBoxStyle.listStyle.font = GameSkin.getInstance().getFont(SelectMenuStyle.getFont());
		selectOption.setStyle(selectBoxStyle);
		this.size(SelectMenuStyle.getWidth(), SelectMenuStyle.getHeight());
		this.validate();
	}

	private static class SelectMenuLabel extends LanguageLabel {
		public SelectMenuLabel(LanguageString languageString) {
			super(languageString, SelectMenuStyle.getFont());
			this.setStateToUp();
			this.setAlignment(center, center);
		}

		public void setStateToOver() {
			this.setBackground("label-select-box-background-over");
		}

		public void setStateToUp() {
			this.setBackground("label-select-box-background-up");
		}
	}

	private static class SelectMenuStyle {
		public static float getHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 45f + 2 * getPadding();
				case MEDIUM -> 50f + 2 * getPadding();
				case LARGE -> 60f + 2 * getPadding();
			};
		}

		public static float getWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 500f;
				case MEDIUM -> 700f;
				case LARGE -> 1000f;
			};
		}

		public static float getPadding() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 5f;
				case MEDIUM -> 10f;
				case LARGE -> 15f;
			};
		}

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> SUBTITLE3.getName();
				case MEDIUM, LARGE -> SUBTITLE2.getName();
			};
		}
	}

	private class DropDownSelect extends SelectBox<SelectMenuItem> {

		Drawable arrowUpImage = skin.getDrawable("select-box-arrow-up");
		Drawable arrowOpenImage = skin.getDrawable("select-box-arrow-open");

		public DropDownSelect() {
			super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
			setUpSelectionPane();
			this.getList().setAlignment(center);

		}

		@Override
		protected @Null Drawable getBackgroundDrawable () {
			if (isDisabled()) return skin.getDrawable("select-box-background-disabled");
			if (isOpen && cursorOver) {
				return skin.getDrawable("select-box-background-open-over");
			}
			if (isOpen) {
				return skin.getDrawable("select-box-background-open-up");
			}
			if(cursorOver) {
				return skin.getDrawable("select-box-background-over");
			}
			return skin.getDrawable("select-box-background-up");
		}

		private void setUpSelectionPane() {
			List.ListStyle listStyle = this.getList().getStyle();

			listStyle.selection.setBottomHeight(SelectMenuStyle.getPadding());
			listStyle.selection.setTopHeight(SelectMenuStyle.getPadding());

			this.getList().setStyle(listStyle);
		}

		public void setStateOver() {
			SelectBoxStyle style = new SelectBoxStyle(this.getStyle());
			style.background = skin.getDrawable("select-box-background-over");
			this.setStyle(style);
		}

		public void setStateUp() {
			SelectBoxStyle style = new SelectBoxStyle(this.getStyle());
			style.background = skin.getDrawable("select-box-background-up");
			this.setStyle(style);
		}

		public void setStateOpenOver() {
			SelectBoxStyle style = new SelectBoxStyle(this.getStyle());
			style.background = skin.getDrawable("select-box-background-open-over");
			this.setStyle(style);
		}

		public void setStateOpenUp() {
			SelectBoxStyle style = new SelectBoxStyle(this.getStyle());
			style.background = skin.getDrawable("select-box-background-open-up");
			this.setStyle(style);
		}

		@Override
		public void showScrollPane() {
			super.showScrollPane();
			isOpen = true;
			this.setStateOpenOver();
		}

		@Override
		public void hideScrollPane() {
			super.hideScrollPane();
			isOpen = false;
			this.setStateOpenUp();
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
