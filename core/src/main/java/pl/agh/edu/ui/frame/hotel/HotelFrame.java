package pl.agh.edu.ui.frame.hotel;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.HotelTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

import java.util.OptionalDouble;

public class HotelFrame extends BaseFrame {

    public HotelFrame() {
        super(new LanguageString("navbar.button.hotel"));
        mainTable.add(new HotelTable()).grow();
    }
}
