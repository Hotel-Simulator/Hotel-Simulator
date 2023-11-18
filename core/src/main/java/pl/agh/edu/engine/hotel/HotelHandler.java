package pl.agh.edu.engine.hotel;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.data.loader.JSONHotelDataLoader;
import pl.agh.edu.engine.attraction.AttractionHandler;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.PossibleEmployeeHandler;
import pl.agh.edu.engine.employee.scheduler.CleaningScheduler;
import pl.agh.edu.engine.employee.scheduler.ReceptionScheduler;
import pl.agh.edu.engine.employee.scheduler.RepairScheduler;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.room.RoomManager;

public class HotelHandler {
	public final Hotel hotel = new Hotel();
	public final CleaningScheduler cleaningScheduler = new CleaningScheduler(this);
	public final ReceptionScheduler receptionScheduler = new ReceptionScheduler(this);
	public final RepairScheduler repairScheduler = new RepairScheduler(this);

	public final PossibleEmployeeHandler possibleEmployeeHandler = new PossibleEmployeeHandler(this);
	public final EmployeeHandler employeeHandler = new EmployeeHandler();
	public final BankAccount bankAccount;
	public final BankAccountHandler bankAccountHandler;
	public final EmployeeSalaryHandler employeeSalaryHandler;
	public final RoomManager roomManager;
	public final AttractionHandler attractionHandler;

	public HotelHandler(GameDifficultyManager gameDifficultyManager) {
		this.bankAccount = new BankAccount(
				gameDifficultyManager.initialBalance,
				JSONBankDataLoader.scenarios.get(0).accountDetails());
		this.bankAccountHandler = new BankAccountHandler(bankAccount);
		employeeSalaryHandler = new EmployeeSalaryHandler(employeeHandler, bankAccountHandler);
		roomManager = new RoomManager(JSONHotelDataLoader.initialRooms, bankAccountHandler);
		attractionHandler = new AttractionHandler(bankAccountHandler, roomManager);
	}
}
