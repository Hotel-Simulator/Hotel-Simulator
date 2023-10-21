package pl.agh.edu.actor.component.table;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.Font;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.LinkLabel;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.management.bank.BankAccountHandler;
import pl.agh.edu.model.bank.Credit;

import static pl.agh.edu.actor.component.table.CreditTable.CreditTableStyle.*;


public class CreditTable extends BaseTable {
	private final int noColumns = 3;
	private final BankAccountHandler bankAccountHandler;
	public CreditTable(BankAccountHandler bankAccountHandler) {
		super();
		this.bankAccountHandler = bankAccountHandler;
		CreditBaseRow creditHeaderRow = new CreditBaseRow("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall");
		creditHeaderRow.align(Align.bottomLeft);
		innerTable.add(creditHeaderRow).space(getRowSpacing()).growX().align(Align.bottomLeft);
		innerTable.row();
		for (Credit credit : bankAccountHandler.getCurrentCredits().keySet()) {
			BaseRow row = new CreditBaseRow(credit);
			row.align(Align.bottomLeft);
			innerTable.add(row).space(getRowSpacing()).growX().align(Align.bottomLeft);
			innerTable.row();
		}

	}

	public class CreditBaseRow extends BaseRow {
		private static final Skin skin = HotelSkin.getInstance();

		public CreditBaseRow(String... columnNames) {
			super();
			insertActorsToRow(Arrays.stream(columnNames).map(s -> new LanguageLabel(s, getTableFont())).toArray(Actor[]::new));
			this.setBackground("table-header-background");
		}
		public CreditBaseRow(Credit credit) {
			super();
			Label date = new Label(bankAccountHandler.getFinalPaymentDate(credit).toString(),skin, getFont());
			Label monthly = new Label(bankAccountHandler.getMonthlyPayment(credit).toString(), skin, getFont());
			LinkLabel payAllButton = new LinkLabel( "labeledButton.credit.payall", Font.WHITE_BODY1, ()  -> {
				// TODO: 15.10.2023 payAll(credit); 
			});
			insertActorsToRow(date, monthly, payAllButton);
			this.setBackground("table-row-background");
		}


		public void insertActorsToRow(Actor... actors){
			IntStream.range(0, noColumns).forEach(i -> {
				Actor actor = actors[i];
				Container<Actor> container = new Container<>(actor);
				container.pad(1f);
				this.innerTable.add(container).growX().uniform().center().padLeft(getCellPadding()).padRight(getCellPadding());
				if (i != noColumns - 1)
					this.innerTable.add(new Image(HotelSkin.getInstance().getPatch("table-separator-line"))).width(getSeparatorWidth()).growY().center();
			});
		}
	}


	public static class CreditTableStyle extends BaseTableStyle{
		public static float getRowHeight() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static BitmapFont getTableFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> Font.BODY2;
				case MEDIUM -> Font.BODY1;
				case LARGE -> Font.H4;
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
