package pl.agh.edu.actor.select_menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;

public class SelectMenu extends Table {

    private Skin skin = HotelSkin.getInstance();

    private Label descriptionLabel = new SelectMenuLabel();

    private SelectBox<String> selectOption = new DropDownSelect();

    public SelectMenu(){
        this.add(descriptionLabel);
        this.add(selectOption);
        selectOption.setItems("Item 1", "Item 2", "Item 3");
        this.debugAll();

    }

    private class SelectMenuLabel extends Label{
        public SelectMenuLabel(){
            super("Test",skin.get("selectMenu", Label.LabelStyle.class));

        }

    }

    private class DropDownSelect extends SelectBox<String>{
        public DropDownSelect() {
            super(skin.get("selectMenu", SelectBox.SelectBoxStyle.class));
            this.setAlignment(Align.top);
        }

        @Override
        public void layout() {
            super.layout();
        }

        @Override
        protected GlyphLayout drawItem(Batch batch, BitmapFont font, String item, float x, float y, float width) {
            String string = "Example";
            return font.draw(batch, string, x, this.getY()+this.getHeight()/5*3, 0, string.length(), width, Align.top, false, "...");
        }




    }
}
