package pl.agh.edu.management.employee.work_scheduler;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class WorkScheduler<T> {


    private Shift currentShift;
    private final Profession employeesProfession;
    protected final Time time;
    protected final Hotel hotel;
    protected List<Employee> workingEmployees;
    protected final Queue<T> entitiesToExecuteService;
    protected final TimeCommandExecutor timeCommandExecutor;


    protected WorkScheduler(Hotel hotel, Queue<T> entitiesToExecuteService, Profession employeesProfession) {
        this.currentShift = Shift.NIGHT;
        this.employeesProfession = employeesProfession;
        this.time = Time.getInstance();
        this.hotel = hotel;
        this.workingEmployees = new ArrayList<>();
        this.entitiesToExecuteService = entitiesToExecuteService;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
    }

    protected boolean willEmployeeExecuteServiceBeforeShiftEnds(Employee employee){
        return employee.isAtWork(time.getTime().toLocalTime().plusMinutes(employee.getServiceExecutionTime().toMinutes()));
    }

    public void executeServiceIfPossible(Employee employee){
        if(!entitiesToExecuteService.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(employee)){
            executeService(employee, entitiesToExecuteService.remove());
        }
    }

    public void perShiftUpdate(){
        currentShift = currentShift.next();
        workingEmployees = hotel.getWorkingEmployeesByProfession(employeesProfession).stream()
                .filter(employee -> employee.getShift().equals(currentShift))
                .collect(Collectors.toList());
        workingEmployees.forEach(this::executeServiceIfPossible);

    }

    public void addEntity(T entity) {
        this.entitiesToExecuteService.add(entity);
        if (entitiesToExecuteService.size() == 1) {
            workingEmployees.stream()
                    .filter(employee -> !employee.isOccupied() && willEmployeeExecuteServiceBeforeShiftEnds(employee))
                    .findFirst()
                    .ifPresent(employee -> executeService(employee, entitiesToExecuteService.remove()));
        }
    }

    protected abstract void executeService(Employee employee, T entity);
}
