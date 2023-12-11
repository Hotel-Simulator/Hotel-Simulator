package pl.agh.edu.ui.component.table.button_table;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import java.util.List;
import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.ui.component.label.LanguageLabel;
import pl.agh.edu.ui.component.label.ValueTag;
import pl.agh.edu.ui.utils.GameSkinProvider;
import pl.agh.edu.utils.CustomBigDecimal;
import pl.agh.edu.utils.LanguageString;

import static com.badlogic.gdx.utils.Align.center;
import static pl.agh.edu.ui.utils.SkinColor.GRAY;
import static pl.agh.edu.ui.utils.SkinFont.H4;

public class BankOfferTable extends ButtonTable<BankData> implements GameSkinProvider {
    public BankOfferTable(List<BankData> dataList) {
        super(dataList);
    }
    @Override
    public Button createButton(BankData bankData, Button emptyButton) {
        emptyButton.add(createBankNameLabel(bankData)).width(BankOfferTableStyle.getWidth()).height(200f).row();
        emptyButton.add(createCreditInterestRateComponent(bankData)).row();
        emptyButton.add(createBankAccountFeeComponent(bankData)).row();
        return emptyButton;
    }

    @Override
    public void hadnleData(BankData data) {

    }

    private Actor createBankNameLabel(BankData bankData) {
        LanguageLabel bankNameLabel = new LanguageLabel(bankData.name(), BankOfferTableStyle.getTitleFont());
        bankNameLabel.setBaseColor(GRAY);
        bankNameLabel.setAlignment(center, center);
        return bankNameLabel;
    }

    private Actor createCreditInterestRateComponent(BankData bankData) {
        return new ValueTag(new LanguageString("bank.credit.interest"), new CustomBigDecimal(bankData.accountDetails().creditInterestRate()) + "%");
    }
    private Actor createBankAccountFeeComponent(BankData bankData) {
        return new ValueTag(new LanguageString("bank.fee"), new CustomBigDecimal(bankData.accountDetails().creditInterestRate()) + "$");
    }

    private static class BankOfferTableStyle{

        private static float getWidth() {
            return switch (GraphicConfig.getResolution().SIZE) {
                case SMALL -> 900f;
                case MEDIUM -> 950f;
                case LARGE -> 1000f;
            };
        }

        private static String getTitleFont() {
            return H4.getWhiteVariantName();
        }

    }
}
