package pl.agh.edu.actor.frame;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.TextActors.ValueTag;
import pl.agh.edu.actor.component.table.CreditTable;
import pl.agh.edu.actor.component.table.ScrollableContainer;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.time.Time;

import java.math.BigDecimal;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		BankAccount bankAccount = new BankAccount(BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE);
		bankAccount.registerCredit(new Credit(BigDecimal.ONE,10,BigDecimal.ONE, Time.getInstance().getTime().toLocalDate()));
		System.out.println(bankAccount.getCredits());
		CreditTable creditTable = new CreditTable(bankAccount);
		root.add(new ScrollableContainer(creditTable)).grow().pad(20f);


		this.add(root);
	}
}
