package pl.agh.edu.management.hotel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.management.bank.EmployeeSalaryHandler;
import pl.agh.edu.management.employee.EmployeeHandler;
import pl.agh.edu.management.employee.PossibleEmployeeHandler;
import pl.agh.edu.management.employee.work_scheduler.CleaningScheduler;
import pl.agh.edu.management.employee.work_scheduler.ReceptionScheduler;
import pl.agh.edu.management.employee.work_scheduler.RepairScheduler;
import pl.agh.edu.management.game.GameDifficultyManager;
import pl.agh.edu.management.room.RoomManager;
import pl.agh.edu.model.Opinion;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.employee.Employee;

public class HotelHandler {
	public final CleaningScheduler cleaningScheduler = new CleaningScheduler(this);
	public final ReceptionScheduler receptionScheduler = new ReceptionScheduler(this);
	public final RepairScheduler repairScheduler = new RepairScheduler(this);

	public final PossibleEmployeeHandler possibleEmployeeHandler = new PossibleEmployeeHandler(this);
	public final EmployeeHandler employeeHandler = new EmployeeHandler();
	public final BankAccount bankAccount = new BankAccount(GameDifficultyManager.getInstance().getInitialBalance(), new BigDecimal("0.05"), BigDecimal.valueOf(2));
	public final BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
	public final EmployeeSalaryHandler employeeSalaryHandler = new EmployeeSalaryHandler(employeeHandler, bankAccountHandler);
	public final RoomManager roomManager = new RoomManager(JSONHotelDataLoader.initialRooms, bankAccountHandler);

	private final List<Opinion> opinions = new ArrayList<>();

	public HotelHandler() {}

	public double getCompetitiveness() {
		BigDecimal avgRoomStandard = BigDecimal.valueOf(0);
		BigDecimal avgWorkerHappiness = BigDecimal.valueOf(0);
		Double avgOpinionValue = 0.;

		for (Employee employee : employeeHandler.getEmployees()) {
			avgWorkerHappiness.add(employee.getSatisfaction());
		}

		for (Opinion opinion : opinions) {
			avgOpinionValue += opinion.getValue();
		}

		avgRoomStandard.divide(BigDecimal.valueOf(roomManager.getRooms().size()));
		avgWorkerHappiness.divide(BigDecimal.valueOf(employeeHandler.getEmployees().size()));
		avgOpinionValue /= opinions.size();

		return (avgOpinionValue + avgRoomStandard.doubleValue() + avgWorkerHappiness.doubleValue()) / 3;
	}
}
