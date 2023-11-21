package pl.agh.edu.ui.frame.bank;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.ui.component.CustomScrollPane;
import pl.agh.edu.ui.component.bankOffer.BankOffer;
import pl.agh.edu.ui.component.button.ScenarioButton;
import pl.agh.edu.ui.frame.BaseFrame;
import pl.agh.edu.utils.LanguageString;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.utils.Align.left;

public class BankOfferFrame extends BaseFrame {

  public final List<BankOffer> buttonList = new ArrayList<>();
  public final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();

  public BankOfferFrame() {
    super(new LanguageString("navbar.button.offer"));
    Table contentRows = new Table();
    ScrollPane scrollPane = new CustomScrollPane(contentRows, skin, "transparent");
    JSONBankDataLoader.scenarios.forEach(scenario -> {
      BankOffer bankOffer = new BankOffer(scenario);
      bankOffer.align(left);

      contentRows.add(bankOffer).space(20f);
      if(JSONBankDataLoader.scenarios.indexOf(scenario) % 2 == 1) {
        contentRows.row();
      }
    });
    mainTable.add(scrollPane).grow().pad(20f);
    scrollPane.setFadeScrollBars(false);
    createButtonGroup();
  }

  public void createButtonGroup() {
    buttonGroup.setMinCheckCount(1);
    buttonGroup.setMaxCheckCount(1);
    buttonList.forEach(bankOfferButton -> buttonGroup.add((Button) bankOfferButton.getActor()));
  }

}
