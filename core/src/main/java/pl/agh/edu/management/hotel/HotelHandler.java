package pl.agh.edu.management.hotel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.management.employee.EmployeeHandler;
import pl.agh.edu.management.employee.PossibleEmployeeHandler;
import pl.agh.edu.management.employee.work_scheduler.CleaningScheduler;
import pl.agh.edu.management.employee.work_scheduler.ReceptionScheduler;
import pl.agh.edu.management.employee.work_scheduler.RepairScheduler;
import pl.agh.edu.management.room.RoomHandler;
import pl.agh.edu.model.Opinion;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;

public class HotelHandler {
	public final CleaningScheduler cleaningScheduler;
	public final ReceptionScheduler receptionScheduler;
	public final RepairScheduler repairScheduler;

	public final PossibleEmployeeHandler possibleEmployeeHandler;
	public final EmployeeHandler employeeHandler;
	public final RoomHandler roomHandler;

	private final List<Opinion> opinions;

	public HotelHandler() {
		this.cleaningScheduler = new CleaningScheduler(this);
		this.receptionScheduler = new ReceptionScheduler(this);
		this.repairScheduler = new RepairScheduler(this);
		this.possibleEmployeeHandler = new PossibleEmployeeHandler(this);
		this.employeeHandler = new EmployeeHandler();
		this.roomHandler = new RoomHandler();

		opinions = new ArrayList<>();
	}

	public double getCompetitiveness() {
		BigDecimal avgRoomStandard = BigDecimal.valueOf(0);
		BigDecimal avgWorkerHappiness = BigDecimal.valueOf(0);
		Double avgOpinionValue = 0.;

		for (Room room : roomHandler.getRooms()) {
			avgRoomStandard.add(room.getStandard());
		}

		for (Employee employee : employeeHandler.getEmployees()) {
			avgWorkerHappiness.add(BigDecimal.valueOf(employee.getSatisfaction()));
		}

		for (Opinion opinion : opinions) {
			avgOpinionValue += opinion.getValue();
		}

		avgRoomStandard.divide(BigDecimal.valueOf(roomHandler.getRooms().size()));
		avgWorkerHappiness.divide(BigDecimal.valueOf(employeeHandler.getEmployees().size()));
		avgOpinionValue /= opinions.size();

		return (avgOpinionValue + avgRoomStandard.doubleValue() + avgWorkerHappiness.doubleValue()) / 3;
	}
}
