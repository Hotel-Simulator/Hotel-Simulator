package pl.agh.edu.actor.frame;

import java.math.BigDecimal;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.table.CreditTable;
import pl.agh.edu.actor.component.table.ScrollableContainer;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;
import pl.agh.edu.model.time.Time;

public class TestFrame extends BaseFrame {
	public TestFrame(String name) {
		super();
		Table root = new Table();

		BankAccount bankAccount = new BankAccount(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
		bankAccount.registerCredit(new Credit(BigDecimal.ONE, 10, BigDecimal.ONE, Time.getInstance().getTime().toLocalDate()));
		System.out.println(bankAccount.getCredits());
		CreditTable creditTable = new CreditTable(bankAccount);
		creditTable.align(Align.bottomLeft);
		root.add(new ScrollableContainer(creditTable)).growX().pad(10);
		this.add(root);
	}
}
