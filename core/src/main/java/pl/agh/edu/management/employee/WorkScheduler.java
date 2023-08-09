package pl.agh.edu.management.employee;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public abstract class WorkScheduler {


    private Shift currentShift;
    private final Profession employeesProfession;
    protected final Time time;
    protected final Hotel hotel;
    protected List<Employee> workingEmployees;
    protected final Queue<Room> roomsToExecuteService;
    protected final TimeCommandExecutor timeCommandExecutor;


    protected WorkScheduler(Hotel hotel, Queue<Room> roomsToExecuteService, Profession employeesProfession) {
        this.currentShift = Shift.NIGHT;
        this.employeesProfession = employeesProfession;
        this.time = Time.getInstance();
        this.hotel = hotel;
        this.workingEmployees = new ArrayList<>();
        this.roomsToExecuteService = roomsToExecuteService;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
    }

    protected boolean willEmployeeExecuteServiceBeforeShiftEnds(Employee employee){
        return employee.isAtWork(time.getTime().toLocalTime().plusMinutes(employee.getServiceExecutionTime().toMinutes()));
    }

    public void executeServiceIfPossible(Employee employee){
        if(!roomsToExecuteService.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(employee)){
            executeService(employee, roomsToExecuteService.remove());
        }
    }

    public void perShiftUpdate(){
        currentShift = currentShift.next();
        workingEmployees = hotel.getWorkingEmployeesByProfession(employeesProfession).stream()
                .filter(employee -> employee.getShift().equals(currentShift))
                .collect(Collectors.toList());
        workingEmployees.forEach(this::executeServiceIfPossible);

    }

    public void addRoom(Room room) {
        this.roomsToExecuteService.add(room);
        if (roomsToExecuteService.size() == 1) {
            workingEmployees.stream()
                    .filter(employee -> !employee.isOccupied() && willEmployeeExecuteServiceBeforeShiftEnds(employee))
                    .findFirst()
                    .ifPresent(employee -> executeService(employee, roomsToExecuteService.remove()));
        }
    }

    protected abstract void executeService(Employee employee, Room room);
}
