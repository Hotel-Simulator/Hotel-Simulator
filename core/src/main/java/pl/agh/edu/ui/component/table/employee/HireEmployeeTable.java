package pl.agh.edu.ui.component.table.employee;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.BaseTable;
import pl.agh.edu.utils.LanguageString;

import static pl.agh.edu.ui.utils.SkinFont.BODY_1;

public class HireEmployeeTable extends BaseTable {

        private final PossibleEmployeeHandler possibleEmployeeHandler;
        private static final List<LanguageString> creditTableColumnNames = Stream.of(
                "hireEmployeeTable.column.photo",
                "hireEmployeeTable.column.name",
                "hireEmployeeTable.column.position",
                "hireEmployeeTable.column.level",
                "hireEmployeeTable.column.salary"
        ).map(LanguageString::new).toList();

        public HireEmployeeTable(PossibleEmployeeHandler possibleEmployeeHandler) {
            super(creditTableColumnNames);
            this.possibleEmployeeHandler = possibleEmployeeHandler;
            for (PossibleEmployee possibleEmployee : possibleEmployeeHandler.getPossibleEmployees()) {
                addRow(new HireEmployeeRow(possibleEmployee));
            }
        }

        protected static class HireEmployeeRow extends BaseTable.BaseRow {
            public HireEmployeeRow(PossibleEmployee possibleEmployee) {
                super();
                Image photo = new Image(skin.getDrawable("default"));

                CustomLabel nameLabel = new CustomLabel(BODY_1.getName());
                nameLabel.setText(possibleEmployee.firstName + " " + possibleEmployee.lastName);

                LanguageLabel positionLabel = new LanguageLabel(possibleEmployee.profession.languageString, BODY_1.getName());

                Rating levelLabel = new Rating(possibleEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue());

                CustomLabel salaryLabel = new CustomLabel(BODY_1.getName());
                salaryLabel.setText(possibleEmployee.preferences.acceptableWage.toString() + "$");

                insertActorsToRow(List.of(photo, nameLabel, positionLabel,levelLabel,salaryLabel));
            }
        }
}
