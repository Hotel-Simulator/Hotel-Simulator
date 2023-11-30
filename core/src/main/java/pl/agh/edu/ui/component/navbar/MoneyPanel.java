package pl.agh.edu.ui.component.navbar;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinFont.H4;

import java.math.BigDecimal;

import pl.agh.edu.engine.bank.BalanceListener;
import pl.agh.edu.ui.component.label.CustomLabel;
import pl.agh.edu.ui.utils.wrapper.WrapperTable;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneyPanel extends WrapperTable implements BalanceListener {
	private final CustomLabel moneyLabel = new MoneyLabel();

	public MoneyPanel() {
		innerTable.add(moneyLabel).grow().center();
		this.size(220, 80);
		engine.bankAccountHandler.addBalanceListener(this);
		setMoney(new CustomBigDecimal(engine.bankAccountHandler.account.getBalance()));
	}

	public void setMoney(CustomBigDecimal balance) {
		moneyLabel.setText(balance.toString() + "$");
	}

	@Override
	public void onBalanceChange(BigDecimal balance) {
		setMoney(new CustomBigDecimal(balance));
	}

	private static class MoneyLabel extends CustomLabel {
		public MoneyLabel() {
			super(H4.getName());
			this.setBackground("label-money-background");
			label.setText("100$");
			label.setAlignment(center, center);
		}
	}
}
