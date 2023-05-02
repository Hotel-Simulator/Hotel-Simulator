package pl.agh.edu;

import com.github.javafaker.Faker;
import pl.agh.edu.model.Employee;
import pl.agh.edu.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEmployeeGenerator {
    static public List<Employee> getEmployees(int n){
        Faker faker = new Faker();
        List<Employee> employeeList = new ArrayList<>();
        Random rand = new Random();
        rand.setSeed(0);

        for(int i=0;i<n;i++){
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            double skills = rand.nextDouble();
            skills = Math.round(skills*100)/100.0;
            int age = rand.nextInt(20,60);
            Employee employee = new Employee(firstName,lastName,age,skills, Role.randomRole());
            employeeList.add(employee);
        }
        return employeeList;
    }
}
