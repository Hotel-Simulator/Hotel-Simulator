package pl.agh.edu.actor.TextActors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.config.GraphicConfig;

public class ValueTag extends Table {
    private String Tag;
    private String Value;
    private final Skin skin;
    private static Size size = Size.SMALL;


    public ValueTag(String tag, String value) {
        Stack componentStack = new Stack();
        add(componentStack).height(ValueTagStyle.getHeight()).width(ValueTagStyle.getWidth());
        skin = HotelSkin.getInstance();
        NinePatch background = skin.getPatch("valuetag-background");
        componentStack.add(new Image(background));
        componentStack.add(new ValueTagContent(tag, value));
    }

    private class ValueTagContent extends Table{
        ValueTagContent(String tag, String value){
            this.padLeft(ValueTagStyle.getPadding()).padRight(ValueTagStyle.getPadding());

            Label tagLabel = new Label(tag,skin,"subtittle1");


            BitmapFont font = skin.getFont("white-subtittle1");
            Label.LabelStyle ls = new Label.LabelStyle();
            ls.font = font;

            ls.fontColor = skin.getColor("Primary_700");
            tagLabel.setStyle(ls);
            this.add(tagLabel).growX();

            Label valueLabel = new Label(value,skin,"white-subtittle1");
            valueLabel.setColor(skin.getColor("Gray_700"));

        }
    }

    private static class ValueTagStyle {
        public static float getHeight() {
            return switch (size) {
                case SMALL -> 60f;
                case MEDIUM -> 70f;
                case LARGE -> 80f;
            };
        }
        public static float getWidth() {
            return switch (size) {
                case SMALL -> 250f;
                case MEDIUM -> 270f;
                case LARGE -> 300f;
            };
        }
        public static float getPadding() {
            return switch (size) {
                case SMALL -> 250f;
                case MEDIUM -> 270f;
                case LARGE -> 300f;
            };
        }

    }

}
