package pl.agh.edu.management.employee;

import pl.agh.edu.generator.possible_employee_generator.PossibleEmployeeGenerator;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.employee.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class EmployeesToHireHandler {
    private final Hotel hotel;

    private final List<PossibleEmployee> employeesToHire;
    private final int employeesToHireListSize;
    private final double possibleEmployeeRemovalProbability;
    private final Random random;

    public EmployeesToHireHandler(Hotel hotel){
        employeesToHireListSize = JSONGameDataLoader.employeesToHireListSize;
        possibleEmployeeRemovalProbability = JSONGameDataLoader.possibleEmployeeRemovalProbability;
        this.employeesToHire = new ArrayList<>();
        random = new Random();
        this.hotel = hotel;
        initialize();
    }



    public void initialize(){
        IntStream.range(0,employeesToHireListSize).forEach(i ->
            employeesToHire.add(PossibleEmployeeGenerator.generatePossibleEmployee())
        );
    }

    public List<PossibleEmployee> getEmployeesToHire() {
        return employeesToHire;
    }

    public void dailyUpdate(){
        employeesToHire.removeIf((employee -> random.nextDouble() <= possibleEmployeeRemovalProbability));
        IntStream.range(employeesToHire.size(),employeesToHireListSize)
                .forEach(i -> employeesToHire.add(PossibleEmployeeGenerator.generatePossibleEmployee()));
    }
    public void offerJob(PossibleEmployee possibleEmployee, JobOffer jobOffer){
        if(possibleEmployee.offerJob(jobOffer) == JobOfferResponse.POSITIVE){
            hotel.hireEmployee(new Employee(possibleEmployee,jobOffer));
            employeesToHire.remove(possibleEmployee);
        }
    }


}
