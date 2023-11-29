package pl.agh.edu.engine.employee.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;

public abstract class WorkScheduler<T> {

	protected final Time time;
	protected final TimeCommandExecutor timeCommandExecutor;
	protected final HiredEmployeeHandler employeeHandler;
	protected final Queue<T> entitiesToExecuteService;
	protected final Profession employeesProfession;
	protected List<HiredEmployee> workingEmployees;
	protected Shift currentShift;

	protected WorkScheduler(HiredEmployeeHandler employeeHandler, Queue<T> entitiesToExecuteService, Profession employeesProfession) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.employeesProfession = employeesProfession;
		this.employeeHandler = employeeHandler;
		this.entitiesToExecuteService = entitiesToExecuteService;
		this.workingEmployees = new ArrayList<>();
		this.currentShift = Shift.EVENING;
	}

	protected WorkScheduler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			HiredEmployeeHandler employeeHandler,
			Queue<T> entitiesToExecuteService,
			Profession employeesProfession,
			List<HiredEmployee> workingEmployees,
			Shift currentShift) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.employeesProfession = employeesProfession;
		this.employeeHandler = employeeHandler;
		this.entitiesToExecuteService = entitiesToExecuteService;
		this.workingEmployees = workingEmployees;
		this.currentShift = currentShift;
	}

	protected boolean willEmployeeExecuteServiceBeforeShiftEnds(HiredEmployee employee) {
		return employee.isAtWork(time.getTime().toLocalTime().plusMinutes(employee.getServiceExecutionTime().toMinutes()));
	}

	public void executeServiceIfPossible(HiredEmployee employee) {
		if (!entitiesToExecuteService.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(employee)) {
			executeService(employee, entitiesToExecuteService.remove());
		}
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

	protected abstract void executeService(HiredEmployee employee, T entity);
}
