package pl.agh.edu.actor.panel.navbar;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import pl.agh.edu.actor.HotelSkin;

public class MoneyPanel extends Table {
    private Label moneyLabel;
    private Skin skin;

    public MoneyPanel() {
        skin = HotelSkin.getInstance();
        Label.LabelStyle labelStyle = skin.get("navbar", Label.LabelStyle.class);
        moneyLabel = new Label("200,000$", labelStyle);
        moneyLabel.setAlignment(Align.center);

        add(moneyLabel).size(220,60).left().growX();
    }
    public void setMoney(String money) {
        moneyLabel.setText(money);
    }
}
