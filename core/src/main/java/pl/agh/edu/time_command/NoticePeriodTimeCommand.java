package pl.agh.edu.time_command;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.employee.Employee;

public class NoticePeriodTimeCommand implements TimeCommand{

    private final Hotel hotel;
    private final Employee employee;

    public NoticePeriodTimeCommand(Hotel hotel, Employee employee) {
        this.hotel = hotel;
        this.employee =employee;
    }

    @Override
    public void execute() {
        this.hotel.removeEmployee(employee);
    }
}
