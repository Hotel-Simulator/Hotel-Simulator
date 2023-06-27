package pl.agh.edu.model.employee;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.generator.employee_generator.EmployeeGenerator;
import pl.agh.edu.model.Hotel;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class EmployeesToHireHandler {
    private final Hotel hotel;

    private final List<Employee> employeesToHire;
    private final int employeesToHireListSize;

    private final Random random;

    public EmployeesToHireHandler(Hotel hotel) throws IOException, ParseException {
        employeesToHireListSize = JSONExtractor.getEmployeesToHireListSizeFromJSON();
        this.employeesToHire = new ArrayList<>();
        random = new Random();
        this.hotel = hotel;
        initialize();
    }



    public void initialize(){
        IntStream.range(0,employeesToHireListSize).forEach(i ->
            employeesToHire.add(EmployeeGenerator.generateCleaner())
        );
    }

    public List<Employee> getEmployeesToHire() {
        return employeesToHire;
    }

    public void update(){
        employeesToHire.removeIf((employee -> random.nextInt(20) == 0));
        IntStream.range(employeesToHire.size(),employeesToHireListSize).forEach(i -> employeesToHire.add(EmployeeGenerator.generateCleaner()));
    }

    public void remove(Employee employee){
        employeesToHire.remove(employee);
    }

    public void offerJob(Employee employee, Shift shift, BigDecimal wage, TypeOfContract typeOfContract){
        if(employee.offerJob(shift,wage,typeOfContract) == JobOfferResponse.POSITIVE){
            hotel.hireEmployee(employee);
            employeesToHire.remove(employee);
        }
    }


}
