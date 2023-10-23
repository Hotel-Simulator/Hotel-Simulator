package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.component.table.CreditTable;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.model.bank.BankAccount;

import java.math.BigDecimal;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);

		BankAccount bankAccount = new BankAccount(BigDecimal.ONE, BigDecimal.TEN,BigDecimal.TEN);
		BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
		CreditTable creditTable = new CreditTable(bankAccountHandler);
		ScrollPane scrollPane = new ScrollPane(creditTable, GameSkin.getInstance(), "transparent"	);
		scrollPane.setScrollbarsVisible(true);
		creditTable.align(Align.bottomLeft);
		scrollPane.setActor(creditTable);
		mainTable.add(scrollPane).growX().align(Align.bottomLeft);
	}
}
