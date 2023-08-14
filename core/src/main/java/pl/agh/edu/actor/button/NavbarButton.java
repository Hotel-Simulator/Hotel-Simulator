package pl.agh.edu.actor.button;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.utils.JsonLanguageLoader;

public class NavbarButton extends Table {
	private final Image iconImage;
	private final Label label;
	private final NavbarButtonStyle navbarButtonStyle;
	private boolean disabled = false;

	public NavbarButton(String styleName, Runnable touchUpCallback) {
		Skin skin = HotelSkin.getInstance();
		navbarButtonStyle = skin.get(styleName, NavbarButtonStyle.class);

		iconImage = new Image(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));

		Label.LabelStyle labelStyle = new Label.LabelStyle(navbarButtonStyle.font, null);
		label = new Label("", labelStyle);
		label.setAlignment(Align.top);

		float topSpace = 10f;
		add().space(topSpace).row();
		add(iconImage).fill();
		row();
		float labelHeight = 30f;
		add(label).height(labelHeight).fill();

		float gap = 20f;
		pad(0, gap, 0, gap);

		setText(JsonLanguageLoader.loadLanguageData("jsons/language/en/navbar.json", "Buttons", navbarButtonStyle.text));
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
					if (touchUpCallback != null) {
						touchUpCallback.run();
					}
				}
			}
		});
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
