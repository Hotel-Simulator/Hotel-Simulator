package pl.agh.edu.actor.list;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.agh.edu.actor.HotelSkin;

public class DropDownList extends List<String> {

    private Skin skin = HotelSkin.getInstance();
    private DropDownList() {
        super(HotelSkin.getInstance().get("selectMenu", ListStyle.class));
    }
}
