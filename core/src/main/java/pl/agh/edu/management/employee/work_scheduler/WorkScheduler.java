package pl.agh.edu.management.employee.work_scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommandExecutor;

public abstract class WorkScheduler<T> {

	private Shift currentShift;
	private final Profession employeesProfession;
	protected final Time time;
	protected final HotelHandler hotelHandler;
	protected List<Employee> workingEmployees;
	protected final Queue<T> entitiesToExecuteService;
	protected final TimeCommandExecutor timeCommandExecutor;

	protected WorkScheduler(HotelHandler hotelHandler, Queue<T> entitiesToExecuteService, Profession employeesProfession) {
		this.currentShift = Shift.NIGHT;
		this.employeesProfession = employeesProfession;
		this.time = Time.getInstance();
		this.hotelHandler = hotelHandler;
		this.workingEmployees = new ArrayList<>();
		this.entitiesToExecuteService = entitiesToExecuteService;
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
	}

	protected boolean willEmployeeExecuteServiceBeforeShiftEnds(Employee employee) {
		return employee.isAtWork(time.getTime().toLocalTime().plusMinutes(employee.getServiceExecutionTime().toMinutes()));
	}

	public void executeServiceIfPossible(Employee employee) {
		if (!entitiesToExecuteService.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(employee)) {
			executeService(employee, entitiesToExecuteService.remove());
		}
	}

	public void perShiftUpdate() {
		currentShift = currentShift.next();
		workingEmployees = hotelHandler.employeeHandler.getWorkingEmployeesByProfession(employeesProfession).stream()
				.filter(employee -> employee.shift.equals(currentShift))
				.collect(Collectors.toList());
		workingEmployees.forEach(this::executeServiceIfPossible);

	}

	public void addEntity(T entity) {
		boolean wasEmpty = entitiesToExecuteService.isEmpty();
		this.entitiesToExecuteService.add(entity);
		if (wasEmpty) {
			tryToAssignEmployeeToEntity();
		}
	}

	private void tryToAssignEmployeeToEntity() {
		workingEmployees.stream()
				.filter(employee -> !employee.isOccupied() && willEmployeeExecuteServiceBeforeShiftEnds(employee))
				.findFirst()
				.ifPresent(employee -> executeService(employee, entitiesToExecuteService.remove()));
	}

	protected abstract void executeService(Employee employee, T entity);
}
