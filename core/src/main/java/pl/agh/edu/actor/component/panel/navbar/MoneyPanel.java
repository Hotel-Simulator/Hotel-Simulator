package pl.agh.edu.actor.component.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import pl.agh.edu.actor.HotelSkin;
import pl.agh.edu.actor.utils.CustomLabel;
import pl.agh.edu.actor.utils.Font;
import pl.agh.edu.actor.utils.WrapperTable;

public class MoneyPanel extends WrapperTable {
	private final Label moneyLabel = new MoneyLabel();
	private final Skin skin = HotelSkin.getInstance();

	public MoneyPanel() {
		innerTable.add(moneyLabel).grow().center();
		this.size(220, 80);
	}

	private class MoneyLabel extends CustomLabel {
		public MoneyLabel() {
			super(Font.H4, "label-money-background");
			this.setText("100$");
			this.setAlignment(Align.center, Align.center);
		}
	}

	public void setMoney(String money) {
		moneyLabel.setText(money);
	}
}
