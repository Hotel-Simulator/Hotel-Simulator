package pl.agh.edu.actor.button;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import pl.agh.edu.actor.HotelSkin;

public class NavbarButton extends TextButton {

    public String name;
    private Drawable iconUp;
    private Drawable iconOver;
    private Drawable iconDisabled;
    private BitmapFont font;

    public NavbarButton() {
        super("", HotelSkin.getInstance());

        TextButtonStyle buttonStyle = getStyle();
        buttonStyle.over=iconOver;
        buttonStyle.up=iconUp;
        buttonStyle.disabled=iconDisabled;
        setStyle(buttonStyle);

        Label label = getLabel();
        Label.LabelStyle style = label.getStyle();
        style.font = font;
        label.setStyle(style);

        label.setPosition(getX()+getWidth()/2,getY()+(getHeight()*3)/4);
    }
}
