package pl.agh.edu.ui.frame.employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.math.BigDecimal;
import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;
import static pl.agh.edu.ui.component.table.CustomTable.createLanguageLabel;

public class HireEmployeeFrame extends BaseFrame{
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    public HireEmployeeFrame() {
        super(new LanguageString("navbar.button.hire"));
        CustomTable<PossibleEmployee> hireEmployeeTable = new CustomTable.CustomTableBuilder<PossibleEmployee>()
                .addColumn(new LanguageString("hireEmployeeTable.column.photo"), this::createPhoto,2)
                .addColumn(new LanguageString("hireEmployeeTable.column.name"), this::createName,4)
                .addColumn(new LanguageString("hireEmployeeTable.column.position"), this::createPosition,4)
                .addColumn(new LanguageString("hireEmployeeTable.column.level"), this::createLevel,3)
                .addColumn(new LanguageString("hireEmployeeTable.column.salary"), this::createSalary,3)
                .build();

        game.engine.hotelHandler.possibleEmployeeHandler.getPossibleEmployees().forEach(hireEmployeeTable::addRow);
        mainTable.add(hireEmployeeTable).growX();
    }

    private Actor createPhoto(PossibleEmployee possibleEmployee){
        Image image = new Image(skin.getDrawable("default"));
        Container<Image> container = new Container<>(image);
        container.size(50f);
        return container;
    }
    private Actor createName(PossibleEmployee possibleEmployee){
        return createCustomLabel(possibleEmployee.firstName + " " + possibleEmployee.lastName);
    }
    private Actor createPosition(PossibleEmployee possibleEmployee){
        return createLanguageLabel(possibleEmployee.profession.languageString);
    }
    private Actor createLevel(PossibleEmployee possibleEmployee){
        return new Rating(possibleEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue());
    }
    private Actor createSalary(PossibleEmployee possibleEmployee){
        return createCustomLabel(possibleEmployee.preferences.acceptableWage.toString() + "$");
    }
}
