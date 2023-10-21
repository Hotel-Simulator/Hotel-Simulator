package pl.agh.edu.actor.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.actor.HotelSkin;

public class SkinColor {
    private static final Skin skin = HotelSkin.getInstance();
    public static final com.badlogic.gdx.graphics.Color PRIMARY_100 = skin.getColor("primary-100");
    public static final com.badlogic.gdx.graphics.Color PRIMARY_300 = skin.getColor("primary-300");
    public static final com.badlogic.gdx.graphics.Color PRIMARY_500 = skin.getColor("primary-500");
    public static final com.badlogic.gdx.graphics.Color PRIMARY_700 = skin.getColor("primary-700");
    public static final com.badlogic.gdx.graphics.Color PRIMARY_900 = skin.getColor("primary-900");

    public static final com.badlogic.gdx.graphics.Color SECONDARY_100 = skin.getColor("secondary-100");
    public static final com.badlogic.gdx.graphics.Color SECONDARY_300 = skin.getColor("secondary-300");
    public static final com.badlogic.gdx.graphics.Color SECONDARY_500 = skin.getColor("secondary-500");
    public static final com.badlogic.gdx.graphics.Color SECONDARY_700 = skin.getColor("secondary-700");
    public static final com.badlogic.gdx.graphics.Color SECONDARY_900 = skin.getColor("secondary-900");

    public static final com.badlogic.gdx.graphics.Color ALERT_100 = skin.getColor("alert-100");
    public static final com.badlogic.gdx.graphics.Color ALERT_300 = skin.getColor("alert-300");
    public static final com.badlogic.gdx.graphics.Color ALERT_500 = skin.getColor("alert-500");
    public static final com.badlogic.gdx.graphics.Color ALERT_700 = skin.getColor("alert-700");
    public static final com.badlogic.gdx.graphics.Color ALERT_900 = skin.getColor("alert-900");

    public static final com.badlogic.gdx.graphics.Color SUCCESS_100 = skin.getColor("success-100");
    public static final com.badlogic.gdx.graphics.Color SUCCESS_300 = skin.getColor("success-300");
    public static final com.badlogic.gdx.graphics.Color SUCCESS_500 = skin.getColor("success-500");
    public static final com.badlogic.gdx.graphics.Color SUCCESS_700 = skin.getColor("success-700");
    public static final com.badlogic.gdx.graphics.Color SUCCESS_900 = skin.getColor("success-900");


    public static final com.badlogic.gdx.graphics.Color GRAY_000 = skin.getColor("gray-000");
    public static final com.badlogic.gdx.graphics.Color GRAY_100 = skin.getColor("gray-100");
    public static final com.badlogic.gdx.graphics.Color GRAY_300 = skin.getColor("gray-300");
    public static final com.badlogic.gdx.graphics.Color GRAY_500 = skin.getColor("gray-500");
    public static final com.badlogic.gdx.graphics.Color GRAY_700 = skin.getColor("gray-700");
    public static final com.badlogic.gdx.graphics.Color GRAY_900 = skin.getColor("gray-900");

    public static final com.badlogic.gdx.graphics.Color WARNING_100 = skin.getColor("warning-100");
    public static final com.badlogic.gdx.graphics.Color WARNING_300 = skin.getColor("warning-300");
    public static final com.badlogic.gdx.graphics.Color WARNING_500 = skin.getColor("warning-500");
    public static final com.badlogic.gdx.graphics.Color WARNING_700 = skin.getColor("warning-700");
    public static final com.badlogic.gdx.graphics.Color WARNING_900 = skin.getColor("warning-900");

    public static final com.badlogic.gdx.graphics.Color TRANSPARENT = skin.getColor("transparent");

    public static final com.badlogic.gdx.graphics.Color SHADOW = skin.getColor("shadow");

    enum Color2{
        	PRIMARY_300("primary-100");
            SkinColor value;
        Color2(String s) {
            skin.getColor(s);
        }


    }


}
