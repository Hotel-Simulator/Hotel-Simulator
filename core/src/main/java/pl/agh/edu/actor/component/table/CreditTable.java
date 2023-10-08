package pl.agh.edu.actor.component.table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.frame.BaseFrame;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;

import java.util.Arrays;
import java.util.List;

public class CreditTable extends BaseTable{

    public CreditTable(BankAccount bankAccount) {
        super(List.of("Final payment date", "Monthly payment", "Pay All"));
        for (Credit credit:
            bankAccount.getCredits()) {
            TextButton payAllButton = new TextButton("pay all",HotelSkin.getInstance());
            payAllButton.setSize(100f,30f);
            BaseRow row = createRow(new Label(credit.getTakeOutDate().toString(), HotelSkin.getInstance(),"body1"),
                    new Label(credit.getMonthlyPayment().toString(),HotelSkin.getInstance(),"body1"),
                    new Label("pay all",HotelSkin.getInstance(),"button1"));
            row.innerTable.align(Align.bottomLeft);
            innerTable.add(row).space(rowSpacing).align(Align.bottomLeft).growX();
        }
    }

}
