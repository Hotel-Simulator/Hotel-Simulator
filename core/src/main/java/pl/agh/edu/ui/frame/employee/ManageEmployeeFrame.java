package pl.agh.edu.ui.frame.employee;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;
import static pl.agh.edu.ui.component.table.CustomTable.createLanguageLabel;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class ManageEmployeeFrame extends BaseFrame {

	private CustomTable<Employee> manageEmployeeTable;

	public ManageEmployeeFrame() {
		super(new LanguageString("frame.title.manage"));
		manageEmployeeTable = new CustomTable.CustomTableBuilder<Employee>()
				.addColumn(new LanguageString("hireEmployeeTable.column.photo"), this::createPhoto, 2)
				.addColumn(new LanguageString("hireEmployeeTable.column.name"), this::createName, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.position"), this::createPosition, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.level"), this::createLevel, 3)
				.addColumn(new LanguageString("hireEmployeeTable.column.salary"), this::createJobSatisfaction, 3)
				.build();

		refreshTable();
		mainTable.add(manageEmployeeTable).grow();
	}

	private void refreshTable() {
		manageEmployeeTable.clearTable();
		engine.hotelHandler.employeeHandler.getEmployees()
				.forEach(employee -> manageEmployeeTable.addRow(
						employee,
						() -> ModalManager.getInstance().showManageEmployeeModal(employee, engine.hotelHandler.employeeHandler, this::refreshTable)));
	}

	private Actor createPhoto(Employee possibleEmployee) {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(50f);
		return container;
	}

	private Actor createName(Employee possibleEmployee) {
		return createCustomLabel(possibleEmployee.firstName + " " + possibleEmployee.lastName);
	}

	private Actor createPosition(Employee possibleEmployee) {
		return createLanguageLabel(possibleEmployee.profession.languageString);
	}

	private Actor createLevel(Employee possibleEmployee) {
		return new Rating(possibleEmployee.skills.multiply(BigDecimal.valueOf(5L)).intValue());
	}

	private Actor createJobSatisfaction(Employee possibleEmployee) {
		return createCustomLabel(possibleEmployee.getSatisfaction().multiply(BigDecimal.valueOf(100)).toString() + "%");
	}
}
