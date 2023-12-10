package pl.agh.edu.ui.component.button;

import static com.badlogic.gdx.utils.Align.top;
import static pl.agh.edu.ui.utils.SkinFont.SUBTITLE2;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import pl.agh.edu.ui.component.navbar.BottomNavbarState;
import pl.agh.edu.ui.component.navbar.NavbarButtonType;
import pl.agh.edu.ui.language.LanguageChangeListener;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.ui.utils.GameSkinProvider;
import pl.agh.edu.utils.LanguageString;

public class NavbarButton extends Table implements LanguageChangeListener, GameSkinProvider {
	private final Image iconImage;
	private final Label label;
	private final NavbarButtonType type;
	private final BottomNavbarState state;
	private final NavbarButtonStyle navbarButtonStyle;
	private boolean disabled = false;
	private Runnable touchUpAction;

	public NavbarButton(NavbarButtonType type, BottomNavbarState state) {
		this.type = type;
		this.state = state;

		navbarButtonStyle = getGameSkin().get(type.getStyleName(), NavbarButtonStyle.class);
		iconImage = new Image(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));
		label = new Label("", getGameSkin(), SUBTITLE2.getName());

		label.setAlignment(top);

		add(iconImage).width(100f).padTop(10f).row();
		add(label).height(30f).width(120f);

		this.pad(0, 20f, 0, 20f);

		LanguageManager.addListener(this);
		onLanguageChange();

		addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (!disabled)
					iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconOver)));
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (!disabled)
					iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));

			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!disabled && touchUpAction != null)
					touchUpAction.run();
			}
		});
	}

	public boolean compare(NavbarButtonType type, BottomNavbarState state) {
		return this.type == type && this.state == state;
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		if (disabled) {
			iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconDisabled)));
		} else {
			iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));
		}
	}

	public void setTouchUpAction(Runnable touchUpAction) {
		this.touchUpAction = touchUpAction;
	}

	@Override
	public Actor onLanguageChange() {
		setText(LanguageManager.get(new LanguageString(navbarButtonStyle.text)));
		return this;
	}

	public static class NavbarButtonStyle {
		public String text;
		public Sprite iconUp;
		public Sprite iconOver;
		public Sprite iconDisabled;
		public BitmapFont font;

		public String name;

		public NavbarButtonStyle() {}

		public NavbarButtonStyle(NavbarButtonStyle style) {
			text = style.text;
			iconUp = style.iconUp;
			iconOver = style.iconOver;
			iconDisabled = style.iconDisabled;
			font = style.font;
			name = style.name;
		}
	}
}
