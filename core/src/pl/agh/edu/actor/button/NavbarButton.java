package pl.agh.edu.actor.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.utils.JsonLanguageLoader;

import java.util.function.Function;

public class NavbarButton extends Table {

    private final float width = 120f;
    private final float height = 90f;

    private final float labelOffsetX = 0f;

    private Image iconImage;
    private Label label;

    private NavbarButtonStyle navbarButtonStyle;

    private boolean disabled = false;

    public NavbarButton(String styleName, Runnable touchUpCallback) {
        super();

        Skin skin = new Skin(Gdx.files.internal("skin/skin.json"));
        navbarButtonStyle = skin.get(styleName, NavbarButtonStyle.class);

        // Icon Image for the button
        iconImage = new Image(new TextureRegionDrawable(new TextureRegion(navbarButtonStyle.iconUp)));
        iconImage.setSize(width, height - 10f);
        iconImage.setOrigin(iconImage.getWidth() / 2, iconImage.getHeight() / 2);
        iconImage.setScale(1.0f);

        // Label for the button text
        Label.LabelStyle labelStyle = new Label.LabelStyle(navbarButtonStyle.font, null);
        label = new Label("", labelStyle);
        label.setAlignment(Align.center);

        // Add the icon and label to the button
        add(iconImage).size(width, height).center().padBottom(labelOffsetX);
        row();
        add(label).expand().fill();

        // Set the button's size
        setSize(width, height);

        // Set the text (TODO: placeholder for language)
        setText(JsonLanguageLoader.loadLanguageData("jsons/language/en/navbar.json", "Buttons", navbarButtonStyle.text));

        pad(0,10,10,10);

        // Add input listener for hover and disabled effect
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

        public NavbarButtonStyle() {
        }

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