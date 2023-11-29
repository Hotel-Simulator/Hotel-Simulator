package pl.agh.edu.engine.employee;

import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.contract.OfferResponse;

public abstract class EmployeeHandler<T extends Employee> {
	protected final List<T> employeeList;

	protected EmployeeHandler() {
		this.employeeList = new ArrayList<>();
	}

	protected EmployeeHandler(List<T> employeeList) {
		this.employeeList = employeeList;
	}

	public abstract OfferResponse offerContract(T employee, EmployeeOffer employeeOffer);
}
