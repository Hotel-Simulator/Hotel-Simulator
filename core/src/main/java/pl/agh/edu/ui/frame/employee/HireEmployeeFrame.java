package pl.agh.edu.ui.frame.employee;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.ui.component.table.employee.HireEmployeeTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class HireEmployeeFrame extends BaseFrame{
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    public HireEmployeeFrame() {
        super(new LanguageString("navbar.button.hire"));
        mainTable.add(new HireEmployeeTable(game.engine.hotelHandler.possibleEmployeeHandler)).grow();
    }
}
