package pl.agh.edu.actor.button;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.utils.JsonLanguageLoader;

public class NavbarButton extends TextButton {

    private final Float width = 120f;
    private final Float height = 90f;

    private final Float labelOffsetX = 30f;

    private NavbarButtonStyle navbarButtonStyle;

    public NavbarButton(String styleName) {
        super("", HotelSkin.getInstance());

        //TODO last usage image is only show
        navbarButtonStyle = HotelSkin.getInstance().get(styleName, NavbarButtonStyle.class);

        TextButtonStyle buttonStyle = getStyle();
        buttonStyle.over= navbarButtonStyle.iconOver;
        buttonStyle.up= navbarButtonStyle.iconUp;
        buttonStyle.disabled= navbarButtonStyle.iconDisabled;
        setStyle(buttonStyle);

        Label label = getLabel();
        Label.LabelStyle style = label.getStyle();
        style.font = navbarButtonStyle.font;
        label.setStyle(style);

        //TODO placeholder for language
        setText(JsonLanguageLoader.loadLanguageData("jsons/language/en/navbar.json","Buttons",navbarButtonStyle.text));


    }

    @Override
    public void layout() {
        super.layout();

        this.setSize(width,height);

        Label label = getLabel();
        label.setSize(width,height);
        float labelX = this.getOriginX() + (getWidth() - label.getWidth()) / 2;
        float labelY = getOriginY() - labelOffsetX;
        label.setPosition(labelX, labelY);
    }
    public static class NavbarButtonStyle {
        public String text;
        public Drawable iconUp;
        public Drawable iconOver;
        public Drawable iconDisabled;
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
