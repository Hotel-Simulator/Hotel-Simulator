package pl.agh.edu.model.employee;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.generator.employee_generator.EmployeeGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class EmployeesToHireHandler {
    private static EmployeesToHireHandler instance;


    private final List<Employee> employeesToHire;
    private final int employeesToHireListSize;

    private final Random random;

    private EmployeesToHireHandler() throws IOException, ParseException {
        employeesToHireListSize = JSONExtractor.getEmployeesToHireListSizeFromJSON();
        this.employeesToHire = new ArrayList<>();
        random = new Random();
        initialize();
    }

    public static EmployeesToHireHandler getInstance() throws IOException, ParseException {
        if(instance == null) instance = new EmployeesToHireHandler();
        return instance;
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

    public static void main(String[] args) throws IOException, ParseException {
        EmployeesToHireHandler employeesToHireHandler = EmployeesToHireHandler.getInstance();
        System.out.println(instance.getEmployeesToHire());
        IntStream.range(0,10).forEach( i-> employeesToHireHandler.update());


    }
}
