package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;

public class MoneyPanel extends WrapperTable {
	private final Label moneyLabel = new MoneyLabel();

	public MoneyPanel() {
		innerTable.add(moneyLabel).grow().center();
		this.size(220, 80);
	}

	public void setMoney(String money) {
		moneyLabel.setText(money);
	}

	private static class MoneyLabel extends CustomLabel {
		public MoneyLabel() {
			super(H4.getName());
			this.setBackground("label-money-background");
			this.setText("100$");
			this.setAlignment(center, center);
		}
	}
}
