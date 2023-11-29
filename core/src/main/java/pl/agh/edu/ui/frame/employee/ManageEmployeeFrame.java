package pl.agh.edu.ui.frame.employee;

import static pl.agh.edu.ui.component.table.CustomTable.createCustomLabel;
import static pl.agh.edu.ui.component.table.CustomTable.createLanguageLabel;

import java.math.BigDecimal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.ui.component.modal.ModalManager;
import pl.agh.edu.ui.component.rating.Rating;
import pl.agh.edu.ui.component.table.CustomTable;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

public class ManageEmployeeFrame extends BaseFrame {

	private final CustomTable<HiredEmployee> manageEmployeeTable;

	public ManageEmployeeFrame() {
		super(new LanguageString("frame.title.manage"));
		manageEmployeeTable = new CustomTable.CustomTableBuilder<HiredEmployee>()
				.addColumn(new LanguageString("hireEmployeeTable.column.photo"), this::createPhoto, 2)
				.addColumn(new LanguageString("hireEmployeeTable.column.name"), this::createName, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.position"), this::createPosition, 4)
				.addColumn(new LanguageString("hireEmployeeTable.column.level"), this::createLevel, 3)
				.addColumn(new LanguageString("hireEmployeeTable.column.satisfaction"), this::createJobSatisfaction, 3)
				.build();

		refreshTable();
		mainTable.add(manageEmployeeTable).grow();
	}

	private void refreshTable() {
		manageEmployeeTable.clearTable();
		engine.hiredEmployeeHandler.getEmployees()
				.forEach(employee -> manageEmployeeTable.addRow(
						employee,
						() -> ModalManager.getInstance().showManageEmployeeModal(employee, engine.hiredEmployeeHandler,engine.employeeSalaryHandler, this::refreshTable)));
	}

	private Actor createPhoto(HiredEmployee employee) {
		Image image = new Image(skin.getDrawable("default"));
		Container<Image> container = new Container<>(image);
		container.size(50f);
		return container;
	}

	private Actor createName(HiredEmployee employee) {
		return createCustomLabel(employee.firstName + " " + employee.lastName);
	}

	private Actor createPosition(HiredEmployee employee) {
		return createLanguageLabel(employee.profession.languageString);
	}

	private Actor createLevel(HiredEmployee employee) {
		return new Rating(employee.skills.multiply(BigDecimal.valueOf(5L)).intValue());
	}

	private Actor createJobSatisfaction(HiredEmployee employee) {
		return createCustomLabel(employee.getSatisfaction().multiply(BigDecimal.valueOf(100)) + "%");
	}
}
