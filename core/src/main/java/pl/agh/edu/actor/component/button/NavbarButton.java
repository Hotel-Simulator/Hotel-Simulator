package pl.agh.edu.actor.component.button;

import static pl.agh.edu.actor.utils.FontType.SUBTITLE2;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.navbar.NavbarButtonType;
import pl.agh.edu.enums.BottomNavbarState;
import pl.agh.edu.language.LanguageChangeListener;
import pl.agh.edu.language.LanguageManager;

public class NavbarButton extends Table implements LanguageChangeListener {
	private final Image iconImage;
	private final Label label;
	private final NavbarButtonType type;
	private final BottomNavbarState state;
	private final NavbarButtonStyle navbarButtonStyle;
	private boolean disabled = false;

	private Runnable touchUpAction;

	public NavbarButton(NavbarButtonType type, BottomNavbarState state) {
		Skin skin = GameSkin.getInstance();
		this.type = type;
		this.state = state;
		navbarButtonStyle = skin.get(type.getStyleName(), NavbarButtonStyle.class);
		iconImage = new Image(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));

		label = new Label("", GameSkin.getInstance(), SUBTITLE2.getName());

		label.setAlignment(Align.top);

		float topSpace = 10f;
		add().space(topSpace).row();
		add(iconImage).width(100f);
		row();
		float labelHeight = 30f;
		add(label).height(labelHeight).width(120f);

		float gap = 20f;
		pad(0, gap, 0, gap);

		setText(LanguageManager.get(navbarButtonStyle.text));
		LanguageManager.addListener(this);

		addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (!disabled) {
					iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconOver)));
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (!disabled) {
					iconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!disabled) {
					if (touchUpAction != null) {
						touchUpAction.run();
					}
				}
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
	public void onLanguageChange() {
		setText(LanguageManager.get(navbarButtonStyle.text));
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
