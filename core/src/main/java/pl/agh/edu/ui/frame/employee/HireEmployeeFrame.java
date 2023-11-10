package pl.agh.edu.ui.frame.employee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.math.BigDecimal;
import pl.agh.edu.GdxGame;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class HireEmployeeFrame extends BaseFrame{
    public final GdxGame game = (GdxGame) Gdx.app.getApplicationListener();
    public HireEmployeeFrame() {
        super(new LanguageString("navbar.button.hire"));
        CustomTable<PossibleEmployee> hireEmployeeTable = new CustomTable.CustomTableBuilder<PossibleEmployee>()
                .addColumn(new LanguageString("hireEmployeeTable.column.photo"), this::createPhoto,1)
                .addColumn(new LanguageString("hireEmployeeTable.column.name"), this::createName,3)
                .addColumn(new LanguageString("hireEmployeeTable.column.position"), this::createPosition,3)
                .addColumn(new LanguageString("hireEmployeeTable.column.level"), this::createLevel,3)
                .addColumn(new LanguageString("hireEmployeeTable.column.salary"), this::createSalary,3)
                .build();

        game.engine.hotelHandler.possibleEmployeeHandler.getPossibleEmployees().forEach(hireEmployeeTable::addRow);
        mainTable.add(hireEmployeeTable);
        this.debugAll();
    }

    private Actor createPhoto(PossibleEmployee possibleEmployee){
        return new Image(skin.getDrawable("default"));
    }
    private Actor createName(PossibleEmployee possibleEmployee){
        return CustomTable.createCustomLabel(possibleEmployee.firstName + " " + possibleEmployee.lastName);
    }
    private Actor createPosition(PossibleEmployee possibleEmployee){
        return CustomTable.createLanguageLabel(possibleEmployee.profession.languageString);
    }
    private Actor createLevel(PossibleEmployee possibleEmployee){
        return new Rating(possibleEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue());
    }
    private Actor createSalary(PossibleEmployee possibleEmployee){
        return CustomTable.createCustomLabel(possibleEmployee.preferences.acceptableWage.toString() + "$");
    }
}
