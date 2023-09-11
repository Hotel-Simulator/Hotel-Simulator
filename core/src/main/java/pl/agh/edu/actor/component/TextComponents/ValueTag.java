package pl.agh.edu.actor.component.TextComponents;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;

public class ValueTag extends Table {
    private String Tag;
    private String Value;
    private Skin skin;

    private final float WIDTH;
    private final float HEIGHT;
    private final float HORIZONTAL_PADDING;

    public ValueTag(String tag, String value, ValueTagSize size) {
        WIDTH = size.getWIDTH();
        HEIGHT = size.getHEIGHT();
        HORIZONTAL_PADDING = size.getHORIZONTAL_PADDING();
        System.out.println("HEIGHT = " + HEIGHT);
        System.out.println("WIDTH = " + WIDTH);
        Stack componentStack = new Stack();
        add(componentStack).height(HEIGHT).width(WIDTH);
        skin = HotelSkin.getInstance();
        NinePatch background = skin.getPatch("valuetag-background");
        componentStack.add(new Image(background));
        componentStack.add(new ValueTagContent(tag, value));
    }

    private class ValueTagContent extends Table{
        ValueTagContent(String tag, String value){
            setDebug(true);
            this.padLeft(HORIZONTAL_PADDING).padRight(HORIZONTAL_PADDING);

            Label tagLabel = new Label(tag,skin, "body1_label");
            tagLabel.setAlignment(Align.left);
            this.add(tagLabel).growX();


            Label valueLabel = new Label(value,skin, "body1_label");

            this.add(valueLabel);

        }
    }

    public enum ValueTagSize{
        SMALL(400f,40f,40f),
        MEDIUM(500f,40f,40f),
        LARGE(600f,40f,40f);

        private final float WIDTH;
        private final float HEIGHT;
        private final float HORIZONTAL_PADDING;

        ValueTagSize(float WIDTH, float HEIGHT, float HORIZONTAL_PADDING) {
            this.WIDTH = WIDTH;
            this.HEIGHT = HEIGHT;
            this.HORIZONTAL_PADDING = HORIZONTAL_PADDING;

        }


        public float getWIDTH() {
            return WIDTH;
        }

        public float getHEIGHT() {
            return HEIGHT;
        }

        public float getHORIZONTAL_PADDING() {
            return HORIZONTAL_PADDING;
        }


    }

}
