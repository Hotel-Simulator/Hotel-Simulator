package pl.agh.edu.actor.component.table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.component.button.LabeledButton;
import pl.agh.edu.actor.utils.LanguageLabel;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.bank.Credit;

import static pl.agh.edu.actor.component.table.CreditTable.CreditTableStyle.*;


public class CreditTable extends BaseTable {
	private final int noColumns = 3;
	public CreditTable(BankAccount bankAccount) {
		super();
		CreditBaseRow creditHeaderRow = new CreditBaseRow("creditTable.column.date", "creditTable.column.monthly", "creditTable.column.payall");
		creditHeaderRow.align(Align.bottomLeft);
		innerTable.add(creditHeaderRow).space(getRowSpacing()).growX().align(Align.bottomLeft);
		innerTable.row();
		for (Credit credit : bankAccount.getCredits()) {
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
			insertActorsToRow(Arrays.stream(columnNames).map(s -> new LanguageLabel(s, getFont())).toArray(Actor[]::new));
			this.setBackground("table-header-background");
		}
		public CreditBaseRow(Credit credit) {
			super();
			Label date = new Label(credit.getTakeOutDate().toString(),skin, getFont());
			Label monthly = new Label(credit.getMonthlyPayment().toString(), skin, getFont());
			LabeledButton payAllButton = new LabeledButton(Size.SMALL, "labeledButton.credit.payall", () -> {
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
				this.innerTable.add(container).growX().uniform().height(getRowHeight()).center().padLeft(getCellPadding()).padRight(getCellPadding());
				if (i != noColumns - 1)
					this.innerTable.add(new Image(HotelSkin.getInstance().getPatch("table-separator-line"))).width(getSeparatorWidth()).growY().center();
			});
		}
	}


	public static class CreditTableStyle {
		public static float getCellPadding() {
			return switch (pl.agh.edu.config.GraphicConfig.getResolution().SIZE) {
				case SMALL -> 15f;
				case MEDIUM -> 20f;
				case LARGE -> 30f;
			};
		}

		public static float getRowSpacing() {
			return switch (pl.agh.edu.config.GraphicConfig.getResolution().SIZE) {
				case SMALL -> 10f;
				case MEDIUM -> 15f;
				case LARGE -> 20f;
			};
		}

		public static float getRowHeight() {
			return switch (pl.agh.edu.config.GraphicConfig.getResolution().SIZE) {
				case SMALL -> 30f;
				case MEDIUM -> 40f;
				case LARGE -> 50f;
			};
		}

		public static String getFont() {
			return switch (GraphicConfig.getResolution().SIZE) {
				case SMALL -> "body2";
				case MEDIUM -> "body1";
				case LARGE -> "h4";
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
