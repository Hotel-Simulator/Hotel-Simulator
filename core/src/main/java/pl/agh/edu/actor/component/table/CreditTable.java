package pl.agh.edu.actor.component.table;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;

public class CreditTable extends BaseTable {

	public CreditTable(BankAccount bankAccount) {
		super(List.of("Final payment date", "Monthly payment", "Pay All"));

		for (Credit credit : bankAccount.getCredits()) {
			LabeledButton payAllButton = new LabeledButton(Size.SMALL, "labeledButton.credit.payall", (Void v) -> {
				// TODO: 09.10.2023 payAll(credit);
			});
			payAllButton.setSize(100f, 30f);
			BaseRow row = createRow(new Label(credit.getTakeOutDate().toString(), HotelSkin.getInstance(), "body1"),
					new Label(credit.getMonthlyPayment().toString(), HotelSkin.getInstance(), "body1"),
					payAllButton);
			row.align(Align.bottomLeft);
			innerTable.add(row).space(rowSpacing).growX();
		}
	}

}
