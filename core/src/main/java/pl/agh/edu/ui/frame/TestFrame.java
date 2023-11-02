package pl.agh.edu.ui.frame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.ui.component.bankOffer.BankOffer.BankOfferStyle;
import pl.agh.edu.ui.component.tab.TabSelector;
import pl.agh.edu.ui.component.table.CreditTable;

import java.math.BigDecimal;

import static com.badlogic.gdx.utils.Align.left;

public class TestFrame extends BaseFrame {
	public TestFrame(String languagePath) {
		super(languagePath);
//		for(BankData bankOffer: JSONBankDataLoader.scenarios){
//			System.out.println(bankOffer.name());
//		}
//		System.out.println(JSONBankDataLoader.scenarios.get(0).name());
			BankOffer bankOffer = new BankOffer(JSONBankDataLoader.scenarios.get(0));
			bankOffer.align(left);
			mainTable.add(bankOffer).space(20f).row();
			Button innerButton = new Button(GameSkin.getInstance(), "transparent");
			mainTable.add(innerButton);
//		BankAccount bankAccount = new BankAccount(BigDecimal.ONE,new BankAccountDetails(BigDecimal.TEN, BigDecimal.TEN));
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
//		creditTable.align(left);
//		mainTable.add(creditTable).grow();

//		TabSelector ts = new TabSelector("test.test", "test.test", ()->{}, ()->{});
//		mainTable.add(ts);
	}
}
