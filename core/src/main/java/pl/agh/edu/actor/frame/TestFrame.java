package pl.agh.edu.actor.frame;

import pl.agh.edu.actor.component.table.CreditTable;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.LinkLabel;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.model.bank.BankAccount;

import java.math.BigDecimal;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		BankAccount bankAccount = new BankAccount(BigDecimal.ONE, BigDecimal.TEN,BigDecimal.TEN);
		BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
		CreditTable creditTable = new CreditTable(bankAccountHandler);
		mainTable.add(creditTable);
	}
}
