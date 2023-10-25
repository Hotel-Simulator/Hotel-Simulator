package pl.agh.edu.actor.component.table;

import static pl.agh.edu.actor.component.table.CreditTable.CreditTableStyle.*;

import java.util.Arrays;
import java.util.List;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.Array;
import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.LinkLabel;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.model.bank.Credit;

public class CreditTable extends BaseTable {

	private final BankAccountHandler bankAccountHandler;
	public static int counter = 0;
	public CreditTable(BankAccountHandler bankAccountHandler) {
		super();
		this.bankAccountHandler = bankAccountHandler;
		CreditBaseRow creditHeaderRow = new CreditBaseRow();
//		ScrollPane sp = new ScrollPane(creditHeaderRow, GameSkin.getInstance(), "transparent");
//		addRow(creditHeaderRow, innerTable);
		creditHeaderRow.align(Align.topLeft);
		
		innerTable.add(creditHeaderRow).height(getRowHeight()+getRowSpacing()).spaceBottom(getRowSpacing()).growX().align(Align.topLeft).row();
		innerTable.getCells().get(0).padRight(scrollPane.getScrollBarWidth());
		System.out.println("scrollPane.getScrollBarWidth() = " + scrollPane.getScrollBarWidth());
		counter++;
		System.out.println("counter = " + counter);

//		innerTable.add(sp).height(getRowHeight()+2*getRowSpacing()).fill().space(getRowSpacing()).row();
//		sp.validate();
		for (Credit credit : bankAccountHandler.getCurrentCredits().keySet()) {
			BaseRow row = new CreditBaseRow(credit);
			addRow(row, contentRows);
		}
//		Drawable knobDrawable =GameSkin.getInstance().getDrawable("scroll-pane-knob");
//		Image knobImage = new Image(knobDrawable);
//		innerTable.add(knobImage);
//		innerTable.row();

		contentRows.align(Align.topLeft);

		scrollPane.setFadeScrollBars(false);
//		scrollPane.setScrollbarsVisible(true);

//		scrollPane.setScrollBarPositions(false,true);
//		System.out.println("scrollPane.getScrollWidth() = " + scrollPane.getScrollWidth());
		scrollPane.setWidth(scrollPane.getWidth()  + scrollPane.getScrollWidth());
		innerTable.add(scrollPane).growX().align(Align.topLeft).colspan(2);
		scrollPane.setForceScroll(false,true);

//		System.out.println( "scrollbarwidth "+scrollPane.getScrollBarWidth());
//		System.out.println( "scrollwidth "+scrollPane.getScrollWidth());
//		scrollPane.debugAll();
//		innerTable.debugAll();

	}

	public void layout() {
		super.layout();
		System.out.println("scrollPane.getScrollBarWidth() = " + scrollPane.getScrollBarWidth());
		innerTable.setWidth(getWidth()-scrollPane.getScrollBarWidth());
	}



	public class CreditBaseRow extends BaseRow {
		private final Skin skin = GameSkin.getInstance();

		public CreditBaseRow() {
			super(List.of("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall"));
		}

		public CreditBaseRow(Credit credit) {
			super();

			Label date = new Label(bankAccountHandler.getLastPaymentDate(credit).toString(), skin, getFont().getName());
			Label monthly = new Label(credit.monthlyPayment.toString(), skin, getFont().getName());
			LinkLabel payAllButton = new LinkLabel("lilnkLabel.payall", FontType.BUTTON_1.getWhiteVariantName(), () -> {
				bankAccountHandler.payEntireCredit(credit);
				deleteRow(this);
			});
			payAllButton.setVarArgs(bankAccountHandler.getValueLeftToPay(credit).stripTrailingZeros());
			insertActorsToRow(List.of(date, monthly, payAllButton));
			this.setBackground("table-row-background");
		}


	}



	public static class CreditTableStyle extends BaseTableStyle {
		public static float getRowHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static FontType getTableFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> FontType.BODY_2;
				case MEDIUM -> FontType.BODY_1;
				case LARGE -> FontType.H4;
			};
		}

		public static float getSeparatorWidth() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 1f;
				case MEDIUM -> 2f;
				case LARGE -> 3f;
			};
		}
	}

}
