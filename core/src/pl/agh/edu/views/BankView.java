package pl.agh.edu.views;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.agh.edu.model.Bank;

public class BankView extends View{
    public BankView(Table root, Skin skin, Bank bank){
        super();
        this.add(new Label("Account balance: "+bank.printBalance(),skin));
        this.row();
    }
}
