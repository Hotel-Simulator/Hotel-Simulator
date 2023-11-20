package pl.agh.edu.ui.frame.employee;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;
import static pl.agh.edu.ui.component.table.CustomTable.createLanguageLabel;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class HireEmployeeFrame extends BaseFrame {

	private CustomTable<PossibleEmployee> hireEmployeeTable;
	public HireEmployeeFrame() {
		super(new LanguageString("navbar.button.hire"));
		hireEmployeeTable = new CustomTable.CustomTableBuilder<PossibleEmployee>()
				.addColumn(new LanguageString("hireEmployeeTable.column.photo"), this::createPhoto, 2)
				.addColumn(new LanguageString("hireEmployeeTable.column.name"), this::createName, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.position"), this::createPosition, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.level"), this::createLevel, 3)
				.addColumn(new LanguageString("hireEmployeeTable.column.salary"), this::createSalary, 3)
				.build();

		this.refreshTable();
		mainTable.add(hireEmployeeTable).grow();
	}

	private void clickAction(PossibleEmployee possibleEmployee) {
		System.out.println(possibleEmployee.firstName + " " + possibleEmployee.lastName);
	}

	private void refreshTable(){
		hireEmployeeTable.clearTable();
		engine.hotelHandler.possibleEmployeeHandler.getPossibleEmployees()
				.forEach(possibleEmployee -> hireEmployeeTable.addRow(
								possibleEmployee,
								() -> ModalManager.getInstance().showHireEmployeeModal(possibleEmployee, engine.hotelHandler.possibleEmployeeHandler,this::refreshTable)
						)
				);
	}

	private Actor createPhoto(PossibleEmployee possibleEmployee) {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(50f);
		return container;
	}

	private Actor createName(PossibleEmployee possibleEmployee) {
		return createCustomLabel(possibleEmployee.firstName + " " + possibleEmployee.lastName);
	}

	private Actor createPosition(PossibleEmployee possibleEmployee) {
		return createLanguageLabel(possibleEmployee.profession.languageString);
	}

	private Actor createLevel(PossibleEmployee possibleEmployee) {
		return new Rating(possibleEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue());
	}

	private Actor createSalary(PossibleEmployee possibleEmployee) {
		return createCustomLabel(possibleEmployee.preferences.acceptableWage.toString() + "$");
	}
}
