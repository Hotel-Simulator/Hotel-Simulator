package pl.agh.edu.engine.employee.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;

public abstract class WorkScheduler<T> {

	protected final Time time = Time.getInstance();
	protected final HotelHandler hotelHandler;
	protected final Queue<T> entitiesToExecuteService;
	protected final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final Profession employeesProfession;
	protected List<Employee> workingEmployees = new ArrayList<>();
	private Shift currentShift = Shift.EVENING;

	protected WorkScheduler(HotelHandler hotelHandler, Queue<T> entitiesToExecuteService, Profession employeesProfession) {
		this.employeesProfession = employeesProfession;
		this.hotelHandler = hotelHandler;
		this.entitiesToExecuteService = entitiesToExecuteService;
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
