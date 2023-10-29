package pl.agh.edu.ui.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.utils.Align;

import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.ui.component.tab.TabSelector;
import pl.agh.edu.ui.component.table.CreditTable;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);


		// CUSTOM AND LANGUAGE LABELS
		//
		// CustomLabel label = new CustomLabel("body1");
		// label.setText("test");
		// mainTable.add(label);
		// mainTable.row().row();
		//
		// CustomLabel label2 = new CustomLabel(H1.getWhiteVariantName());
		// label2.setUnderscoreColor(SkinColor.SECONDARY.getColor(SkinColor.ColorLevel._500));
		// label2.setText("test");
		// label2.makeItLink(() -> System.out.println("h1"));
		// mainTable.add(label2);
		//
		// LanguageLabel languageLabel1 = new LanguageLabel("test.test", BODY_1.getWhiteVariantName());
		// mainTable.add(languageLabel1).row();
		// languageLabel1.makeItLink(() -> System.out.println("test"));
		// languageLabel1.setDisabled(true);


//		CREDIT TABLE
//
//		BankAccount bankAccount = new BankAccount(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.TEN);
//		BankAccountHandler bankAccountHandler = new BankAccountHandler(bankAccount);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		bankAccountHandler.registerCredit(BigDecimal.valueOf(100000), 10);
//		CreditTable creditTable = new CreditTable(bankAccountHandler);
//
//		creditTable.align(Align.left);
//		mainTable.add(creditTable).grow().align(Align.topLeft);

		mainTable.add(new TabSelector("test.test", "test.test", () -> System.out.println("left"), () -> System.out.println("right"))).row();


	}
}
