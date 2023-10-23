package pl.agh.edu.management.hotel;

import java.math.BigDecimal;

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
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.bank.BankAccount;

public class HotelHandler {
	public final Hotel hotel = new Hotel();
	public final CleaningScheduler cleaningScheduler = new CleaningScheduler(this);
	public final ReceptionScheduler receptionScheduler = new ReceptionScheduler(this);
	public final RepairScheduler repairScheduler = new RepairScheduler(this);

	public final PossibleEmployeeHandler possibleEmployeeHandler = new PossibleEmployeeHandler(this);
	public final EmployeeHandler employeeHandler = new EmployeeHandler();
	public final BankAccount bankAccount = new BankAccount(GameDifficultyManager.getInstance().getInitialBalance(), new BigDecimal("0.05"), BigDecimal.valueOf(2));
	public final BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
	public final EmployeeSalaryHandler employeeSalaryHandler = new EmployeeSalaryHandler(employeeHandler, bankAccountHandler);
	public final RoomManager roomManager = new RoomManager(JSONHotelDataLoader.initialRooms, bankAccountHandler);

	public HotelHandler() {}
}
