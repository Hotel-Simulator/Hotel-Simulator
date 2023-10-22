package pl.agh.edu.actor.component.table;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.GameSkin;
import pl.agh.edu.actor.utils.FontType;
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
		private final Skin skin = GameSkin.getInstance();

		public CreditBaseRow(String... columnNames) {
			super();
			insertActorsToRow(Arrays.stream(columnNames).map(s -> new LanguageLabel(s, getTableFont().getName())).toArray(Actor[]::new));
			this.setBackground("table-header-background");
		}
		public CreditBaseRow(Credit credit) {
			super();

			Label date = new Label(bankAccountHandler.getLastPaymentDate(credit).toString(),skin, getFont().getName());
			Label monthly = new Label(credit.monthlyPayment.toString(), skin, getFont().getName());
			LinkLabel payAllButton = new LinkLabel( "lilnkLabel.payall", FontType.BUTTON_1.getWhiteVariantName(), ()  -> {
				bankAccountHandler.payEntireCredit(credit);
				deleteRow(this);
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
					this.innerTable.add(new Image(GameSkin.getInstance().getPatch("table-separator-line"))).width(getSeparatorWidth()).growY().center();
			});
		}
	}

	private void deleteRow(CreditBaseRow row){
		innerTable.getCell(row).clearActor();
		row.remove();
	}


	public static class CreditTableStyle extends BaseTableStyle{
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
