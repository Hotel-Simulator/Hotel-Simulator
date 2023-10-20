package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.actor.HotelSkin;

public class Fonts {
    private final static Skin skin = HotelSkin.getInstance();
    public static final BitmapFont BODY1 = skin.getFont("body1");
    public static final BitmapFont BODY2 = skin.getFont("body2");
    public static final BitmapFont H1 = skin.getFont("h1");
    public static final BitmapFont H2 = skin.getFont("h2");
    public static final BitmapFont H3 = skin.getFont("h3");
    public static final BitmapFont H4 = skin.getFont("h4");
    public static final BitmapFont SUBTITLE1 = skin.getFont("subtitle1");
    public static final BitmapFont SUBTITLE2 = skin.getFont("subtitle2");
    public static final BitmapFont BUTTON1 = skin.getFont("button1");
    public static final BitmapFont BUTTON2 = skin.getFont("button2");
    public static final BitmapFont BUTTON3 = skin.getFont("button3");

}