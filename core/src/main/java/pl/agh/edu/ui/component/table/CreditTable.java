package pl.agh.edu.ui.component.table;

import static pl.agh.edu.ui.component.table.CreditTable.CreditTableStyle.*;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.ui.GameSkin;
import pl.agh.edu.ui.utils.FontType;
import pl.agh.edu.ui.utils.LinkLabel;

public class CreditTable extends BaseTable {

	private final BankAccountHandler bankAccountHandler;

	public CreditTable(BankAccountHandler bankAccountHandler) {
		super();
		this.bankAccountHandler = bankAccountHandler;
		CreditBaseRow creditHeaderRow = new CreditBaseRow();
		creditHeaderRow.align(Align.topLeft);

		innerTable.add(creditHeaderRow).height(getRowHeight() + BaseTableStyle.getRowSpacing()).spaceBottom(BaseTableStyle.getRowSpacing()).growX().align(Align.left);
		innerTable.getCells().get(0).padRight(scrollPane.getScrollBarWidth());

		for (Credit credit : bankAccountHandler.getCurrentCredits().keySet()) {
			BaseRow row = new CreditBaseRow(credit);
			addRow(row, contentRows);
		}
		Drawable knobDrawable = GameSkin.getInstance().getDrawable("scroll-pane-knob");
		Image knobImage = new Image(knobDrawable);
		knobImage.setVisible(false);
		innerTable.add(knobImage).row();

		scrollPane.setFadeScrollBars(false);
		scrollPane.setWidth(scrollPane.getWidth() + scrollPane.getScrollWidth());
		innerTable.add(scrollPane).growX().colspan(2);
		scrollPane.setForceScroll(false, true);
	}

	public void layout() {
		super.layout();
		System.out.println("scrollPane.getScrollBarWidth() = " + scrollPane.getScrollBarWidth());
		innerTable.setWidth(getWidth() - scrollPane.getScrollBarWidth());
	}

	public class CreditBaseRow extends BaseRow {
		private final Skin skin = GameSkin.getInstance();

		public CreditBaseRow() {
			super(List.of("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall"));
		}

		public CreditBaseRow(Credit credit) {
			super();

			Label date = new Label(bankAccountHandler.getLastPaymentDate(credit).toString(), skin, BaseTableStyle.getFont().getName());
			Label monthly = new Label(credit.monthlyPayment.toString(), skin, BaseTableStyle.getFont().getName());
			LinkLabel payAllButton = new LinkLabel("linkLabel.payall", FontType.BUTTON_1.getWhiteVariantName(), () -> {
				bankAccountHandler.payEntireCredit(credit);
				deleteRow(this);
			});
			insertActorsToRow(List.of(date, monthly, payAllButton));

		}

	}

	public static class CreditTableStyle extends BaseTableStyle {
		public static FontType getTableFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> FontType.BODY_2;
				case MEDIUM -> FontType.BODY_1;
				case LARGE -> FontType.H4;
			};
		}

	}

}
