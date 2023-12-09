package pl.agh.edu.engine.employee;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class EmployeeSalaryHandler {
	private final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	private final HiredEmployeeHandler hiredEmployeeHandler;
	private final BankAccountHandler bankAccountHandler;

	public static void kryoRegister() {
		KryoConfig.kryo.register(EmployeeSalaryHandler.class, new Serializer<EmployeeSalaryHandler>() {
			@Override
			public void write(Kryo kryo, Output output, EmployeeSalaryHandler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.hiredEmployeeHandler);
				kryo.writeObject(output, object.bankAccountHandler);
			}

			@Override
			public EmployeeSalaryHandler read(Kryo kryo, Input input, Class<? extends EmployeeSalaryHandler> type) {
				return new EmployeeSalaryHandler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, HiredEmployeeHandler.class),
						kryo.readObject(input, BankAccountHandler.class));
			}
		});
	}

	public EmployeeSalaryHandler(HiredEmployeeHandler hiredEmployeeHandler, BankAccountHandler bankAccountHandler) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.hiredEmployeeHandler = hiredEmployeeHandler;
		this.bankAccountHandler = bankAccountHandler;
	}

	private EmployeeSalaryHandler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			HiredEmployeeHandler hiredEmployeeHandler,
			BankAccountHandler bankAccountHandler) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.hiredEmployeeHandler = hiredEmployeeHandler;
		this.bankAccountHandler = bankAccountHandler;
	}

	public void monthlyUpdate() {
		var salaryToPayForThisMonth = hiredEmployeeHandler.getWorkingEmployees().stream()
				.map(HiredEmployee::getWage)
				.reduce(ZERO, BigDecimal::add);

		timeCommandExecutor.addCommand(new TimeCommand(() -> bankAccountHandler.registerExpense(salaryToPayForThisMonth),
				time.getTime().plusMonths(1)
						.truncatedTo(ChronoUnit.DAYS)
						.withDayOfMonth(JSONEmployeeDataLoader.payDayOfMonth)
						.withHour(12)));
	}

	public void giveBonus(HiredEmployee hiredEmployee, BigDecimal bonus) {
		hiredEmployee.addBonus(bonus);
		bankAccountHandler.registerExpense(bonus);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> hiredEmployee.removeBonus(bonus), time.getTime().plusMonths(1)));
	}

}
